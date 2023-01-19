package com.game.entity;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.game.controller.Controller;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.game.entity.Profession.*;

@Entity
@Table(name = "player")
public class Player {
    public Player( String name, String title, Race race, Profession profession, Long birthday, Boolean banned, Integer experience) {
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.experience = experience;
        this.level = levelCalculation(experience);
        this.untilNextLevel = untilNextLevelCalculation(levelCalculation(experience),experience);
        this.birthday = new Date(birthday);
        this.banned = banned;
    }

    public Player( String name, String title, Race race, Profession profession, Long birthday, Integer experience) {
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.experience = experience;
        this.level = levelCalculation(experience);
        this.untilNextLevel = untilNextLevelCalculation(levelCalculation(experience),experience);
        this.birthday = new Date(birthday);
        this.banned = false;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
    private String name;
    private String title;
    @Enumerated(EnumType.STRING)
    private Race race;
    @Enumerated(EnumType.STRING)
    private Profession profession;
    private Date birthday;
    private Boolean banned;
    private Integer experience;
    private Integer level;
    private Integer untilNextLevel;



    public Player() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title != null) {
            this.title = title;
        }
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        if (race != null){
        this.race = race;}
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        if (profession != null){
        this.profession = profession;}
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        if (experience != null){
        this.experience = experience;}

    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        if (birthday != null) {
            this.birthday = new Date(birthday);
        }
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
         if (banned != null)
        this.banned = banned;
    }

    public Long getId() {
        return id;
    }
    public Integer levelCalculation(Integer experience){

        Double root = Double.valueOf(2500 + 200 * experience);
        Integer square = (int)Math.round(Math.sqrt(root)) ;
        return (square - 50) /100;
    }
    public Integer untilNextLevelCalculation(Integer level,Integer experience){
        return 50 * (level + 1) * (level + 2) - experience;}

    }






