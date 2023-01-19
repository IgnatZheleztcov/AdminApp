package com.game.controller;

import com.game.entity.Createform;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.Rep;
import com.game.service.Service;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.swing.text.html.HTMLDocument;
import java.lang.reflect.Field;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

@RestController
@RequestMapping("/rest/players")
public class Controller {
    @Autowired
    private Rep rep ;

   private Service service = new Service();

    @GetMapping()
    public List<Player> getPlayers(@RequestParam(value = "name",required = false) String name,
                                @RequestParam(value = "title",required = false) String title,
                                @RequestParam(value = "race",required = false) Race race,
                                @RequestParam(value = "profession", required = false) Profession profession,
                                @RequestParam(value = "after",required = false) Long after,
                                @RequestParam(value = "before",required = false) Long before,
                                @RequestParam(value = "banned",required = false) Boolean banned,
                                @RequestParam(value = "minExperience",required = false) Integer minExperience,
                                @RequestParam(value = "maxExperience",required = false) Integer maxExperience,
                                @RequestParam(value = "minLevel",required = false) Integer minLevel,
                                @RequestParam(value = "maxLevel",required = false) Integer maxLevel,
                                @RequestParam(value = "order" , required = false,defaultValue = "ID") PlayerOrder order,
                                @RequestParam(value =  "pageNumber", required = false,defaultValue ="0") Integer pageNumber,
                                @RequestParam(value =  "pageSize", required = false,defaultValue = "3") Integer pageSize

    ) throws SQLException {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Specification<Player> specification = new Specification<Player>() {
            @Override
            public Predicate toPredicate(Root<Player> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (name != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("name"), "%" + name + "%")));
                }
                if (title != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("title"), "%" + title + "%")));
                }
                if (race != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("race"), race)));
                }
                if (profession != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("profession"), profession)));
                }
                if (after != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("birthday"), new Date(after))));
                }
                if (before != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.lessThan(root.get("birthday"), new Date(before))));
                }
                if (banned != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("banned"), banned)));
                }
                if (minExperience != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("experience"), minExperience)));
                }
                if (maxExperience != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("experience"), maxExperience)));
                }
                if (minLevel != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("level"), minLevel)));
                }
                if (maxLevel != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("level"), maxLevel)));
                }
                if (order != null) {
                    query.orderBy(criteriaBuilder.asc(root.get(order.getFieldName())));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return rep.findAll(specification,pageable).getContent();

    }
    @GetMapping("/count")
    public int count(@RequestParam(value = "name",required = false) String name,
                     @RequestParam(value = "title",required = false) String title,
                     @RequestParam(value = "race",required = false) Race race,
                     @RequestParam(value = "profession", required = false) Profession profession,
                     @RequestParam(value = "after",required = false) Long after,
                     @RequestParam(value = "before",required = false) Long before,
                     @RequestParam(value = "banned",required = false) Boolean banned,
                     @RequestParam(value = "minExperience",required = false) Integer minExperience,
                     @RequestParam(value = "maxExperience",required = false) Integer maxExperience,
                     @RequestParam(value = "minLevel",required = false) Integer minLevel,
                     @RequestParam(value = "maxLevel",required = false) Integer maxLevel){
        int count = 0;
        for (Player z: rep.findAll())
         {
            if (service.check(name,title,race,profession,after,before,banned,minExperience,maxExperience,minLevel,maxLevel,z)){
                count++;}
        }
        return count;


    }
    @GetMapping("/{id}")

    public Player getPlayer(@PathVariable Long id){
        if (id <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

try{
Optional<Player> ff = rep.findById(id);
    return  ff.get();
}
catch (Exception e){
    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
}



    }
    @PostMapping()
    public Player createPlayer(@RequestBody Createform createform){
        Player p;
        try {

            if (createform.isEmpty()){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST);
            }
            if (createform.getBirthday() <=(new SimpleDateFormat("yyyy").parse("2000").getTime()) || createform.getBirthday() >= (new SimpleDateFormat("yyyy").parse("3000").getTime()))
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST
                );
            if (createform.getExperience() < 0 || createform.getExperience() > 10000000){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST);
            }
            if (createform.getTitle().length() > 30){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST);
            }

             p = new Player(createform.getName(), createform.getTitle(), createform.getRace(),createform.getProfession(), createform.getBirthday(), createform.getBanned(), createform.getExperience());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return rep.save(p);


    }
    @PostMapping("/{id}")
    public Player updatePlayer(@RequestBody Createform createform,@PathVariable(value = "id") Long id){
            Player p = getPlayer(id);
        if (createform.getOptionalName().isPresent()) {
            p.setName(createform.getName());
        }
        if (createform.getOptionalTitle().isPresent()) {
            p.setTitle(createform.getTitle());
        }
        if (createform.getOptionalRace().isPresent()) {
            p.setRace(createform.getRace());
        }
        if (createform.getOptionalProfession().isPresent()) {
            p.setProfession(createform.getProfession());
        }
        if (createform.getOptionalBirthday().isPresent()) {
            try {
                if (createform.getBirthday() <=(new SimpleDateFormat("yyyy").parse("2000").getTime()) || createform.getBirthday() >= (new SimpleDateFormat("yyyy").parse("3000").getTime()))
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST
                    );
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            p.setBirthday(createform.getBirthday());
        }
        if (createform.getOptionalBanned().isPresent()) {
            p.setBanned(createform.getBanned());
        }
        if (createform.getOptionalExperience().isPresent()) {
            if (createform.getExperience() < 0 || createform.getExperience() > 10000000)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            p.setExperience(createform.getExperience());
            p.setLevel(p.levelCalculation(createform.getExperience()));
            p.setUntilNextLevel(p.untilNextLevelCalculation(p.getLevel(),p.getExperience()));
        }

            return rep.save(p);






    }
    @DeleteMapping("/{id}")
    public HttpStatus deletePlayer(@PathVariable Long id){

        if (id <=0) throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST
        );

        if (id > count(null,null,null,null,null,null,null,null,null,null,null)) throw new ResponseStatusException(
                HttpStatus.NOT_FOUND
        );
        rep.delete(getPlayer(id));

        return  HttpStatus.OK;
    }

}
