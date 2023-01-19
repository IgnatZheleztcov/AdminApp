package com.game.entity;

import java.util.Optional;

public class Createform {
    String name;
    String title;
    Race race;
    Profession profession;
    Long birthday;
    Boolean banned = false;
    Integer experience;

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public Race getRace() {
        return race;
    }

    public Profession getProfession() {
        return profession;
    }

    public Long getBirthday() {
        return birthday;
    }

    public Boolean getBanned() {
        return banned;
    }

    public Integer getExperience() {
        return experience;
    }
    public boolean isEmpty(){
        return name == null ||
                title == null ||
                race == null ||
                profession == null ||
                birthday == null ||
                banned == null ||
                experience == null;
    }
    public Optional getOptionalName() {
        return Optional.ofNullable(name);
    }

    public Optional getOptionalTitle() {
        return Optional.ofNullable(title);
    }

    public Optional getOptionalRace() {
        return Optional.ofNullable(race);
    }

    public Optional getOptionalProfession() {
        return Optional.ofNullable(profession);
    }

    public Optional getOptionalBirthday() {
        return Optional.ofNullable(birthday);
    }

    public Optional getOptionalBanned() {
        return Optional.ofNullable(banned);
    }

    public Optional getOptionalExperience() {
        return Optional.ofNullable(experience) ;
    }

}
