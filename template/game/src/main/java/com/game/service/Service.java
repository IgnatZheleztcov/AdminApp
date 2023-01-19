package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.Rep;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@org.springframework.stereotype.Service
public class Service {
    public Map<String, Object> getmap(Player p) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", p.getId());
        map.put("name", p.getName());
        map.put("title", p.getTitle());
        map.put("race", p.getRace());
        map.put("profession", p.getProfession());
        map.put("birthday", p.getBirthday());
        map.put("banned", p.getBanned());
        map.put("experience", p.getExperience());
        map.put("level", p.getLevel());
        map.put("untilNextLevel", p.getUntilNextLevel());
        return map;
    }
    public boolean check(String name,
                         String title,
                         Race race,
                         Profession profession,
                         Long after,
                         Long before,
                         Boolean banned,
                         Integer minExperience,
                         Integer maxExperience,
                         Integer minLevel,
                         Integer maxLevel,Player player) {
        if (name == null || player.getName().contains(name)) {
            if (title == null || player.getTitle().contains(title)) {
                if (race == null || player.getRace().equals(race)) {
                    if (profession == null || player.getProfession().equals(profession)) {
                        if (after == null || player.getBirthday().getTime() > after) {
                            if (before == null || player.getBirthday().getTime() < before) {
                                if (banned == null || player.getBanned() == banned) {
                                    if (minExperience == null || player.getExperience() > minExperience) {
                                        if (maxExperience == null || player.getExperience() < maxExperience) {
                                            if (minLevel == null || player.getLevel() > minLevel) {
                                                if (maxLevel == null || player.getLevel() < maxLevel) {
                                                    return true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

}
