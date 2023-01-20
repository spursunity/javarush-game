package com.game.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Table(name = "player")
public class PlayerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String title;
    @Enumerated(EnumType.STRING)
    private Race race;
    @Enumerated(EnumType.STRING)
    private Profession profession;
    private Integer experience;
    private Integer level;
    private Integer untilNextLevel;
    private Date birthday;
    private Boolean banned;

    public PlayerEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getLevel() {
        return level;
    }

    private void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    private void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public void setLevelData() {
        int currentLevel = (int) ((Math.pow((double) 2500 + (double) (200 * experience), 0.5) - 50) / 100);
        setLevel(currentLevel);
        int untilNextLvl = 50 * (currentLevel + 1) * (currentLevel + 2) - experience;
        setUntilNextLevel(untilNextLvl);
    }

    public boolean validateInputData() {
        LocalDate startDate = LocalDate.of(1999, 12, 31);
        LocalDate endDate = LocalDate.of(3001, 1, 1);
        LocalDate birthdayLocalTime = birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return name != null && title != null && race != null && profession != null && birthday != null &&
            experience != null && !"".equals(name) && name.length() <= 12 && title.length() <= 30 &&
            birthday.getTime() > 0 && experience > 0 && experience <= 10_000_000 &&
            startDate.isBefore(birthdayLocalTime) && endDate.isAfter(birthdayLocalTime);
    }
}
