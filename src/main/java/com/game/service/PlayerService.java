package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.PlayerEntity;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exception.PlayerInvalidDataException;
import com.game.repository.PlayerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepo playerRepo;

    public List<PlayerEntity> getPlayers(Map<String, String> allParams) {
        PlayerParams playerParams = new PlayerParams(allParams);
        playerRepo.findAll(nameLike(playerParams.getNameValue()));

        return playerRepo.findAll(Specification
                .where(nameLike(playerParams.getNameValue()))
                .and(titleLike(playerParams.getTitleValue()))
                .and(raceEq(playerParams.getRaceValue()))
                .and(professionEq(playerParams.getProfessionValue()))
                .and(birthdayAfter(playerParams.getAfterBirthdayValue()))
                .and(birthdayBefore(playerParams.getBeforeBirthdayValue()))
                .and(bannedEq(playerParams.getBannedValue()))
                .and(filterMinExp(playerParams.getMinExperience()))
                .and(filterMaxExp(playerParams.getMaxExperience()))
                .and(filterMinLevel(playerParams.getMinLevel()))
                .and(filterMaxLevel(playerParams.getMaxLevel())),
                playerParams.getPaginationAndSort()
        ).getContent();
    }

    public Long getPlayersCount(Map<String, String> allParams) {
        PlayerParams playerParams = new PlayerParams(allParams);

        return playerRepo.count(Specification
                .where(nameLike(playerParams.getNameValue()))
                .and(titleLike(playerParams.getTitleValue()))
                .and(raceEq(playerParams.getRaceValue()))
                .and(professionEq(playerParams.getProfessionValue()))
                .and(birthdayAfter(playerParams.getAfterBirthdayValue()))
                .and(birthdayBefore(playerParams.getBeforeBirthdayValue()))
                .and(bannedEq(playerParams.getBannedValue()))
                .and(filterMinExp(playerParams.getMinExperience()))
                .and(filterMaxExp(playerParams.getMaxExperience()))
                .and(filterMinLevel(playerParams.getMinLevel()))
                .and(filterMaxLevel(playerParams.getMaxLevel()))
        );
    }

    public PlayerEntity createPlayer(PlayerEntity player) {
        return playerRepo.save(player);
    }

    public PlayerEntity getPlayerById(Long id) {
        return playerRepo.findById(id).orElse(null);
    }

    public PlayerEntity updatePlayer(PlayerEntity foundPlayer, PlayerEntity newPlayer) throws PlayerInvalidDataException {
        String name = newPlayer.getName();
        String title = newPlayer.getTitle();
        Race race = newPlayer.getRace();
        Profession profession = newPlayer.getProfession();
        Date birthday = newPlayer.getBirthday();
        Boolean banned = newPlayer.getBanned();
        Integer experience = newPlayer.getExperience();

        if (name != null) foundPlayer.setName(name);
        if (title != null) foundPlayer.setTitle(title);
        if (race != null) foundPlayer.setRace(race);
        if (profession != null) foundPlayer.setProfession(profession);
        if (birthday != null) foundPlayer.setBirthday(birthday);
        if (banned != null) foundPlayer.setBanned(banned);
        if (experience != null) foundPlayer.setExperience(experience);

        foundPlayer.setLevelData();
        if (!foundPlayer.validateInputData()) throw new PlayerInvalidDataException();
        return playerRepo.save(foundPlayer);
    }

    public void deletePlayer(PlayerEntity player) {
        playerRepo.delete(player);
    }

    private Specification<PlayerEntity> nameLike(String name) {
        return ((root, query, criteriaBuilder) ->
                        name != null ? criteriaBuilder.like(root.get(PlayerParams.name), "%" + name + "%") : null);
    }

    private Specification<PlayerEntity> titleLike(String title) {
        return ((root, query, criteriaBuilder) ->
                        title != null ? criteriaBuilder.like(root.get(PlayerParams.title), "%" + title + "%") : null);
    }

    private Specification<PlayerEntity> raceEq(Race race) {
        return ((root, query, criteriaBuilder) ->
                race != null ? criteriaBuilder.equal(root.get(PlayerParams.race), race) : null);
    }

    private Specification<PlayerEntity> professionEq(Profession profession) {
        return ((root, query, criteriaBuilder) ->
                profession != null ? criteriaBuilder.equal(root.get(PlayerParams.profession), profession) : null);
    }

    private Specification<PlayerEntity> birthdayAfter(Date birthday) {
        return ((root, query, criteriaBuilder) ->
                birthday != null ? criteriaBuilder.greaterThan(root.get(PlayerParams.birthday), birthday) : null);
    }

    private Specification<PlayerEntity> birthdayBefore(Date birthday) {
        return ((root, query, criteriaBuilder) ->
                birthday != null ? criteriaBuilder.lessThan(root.get(PlayerParams.birthday), birthday) : null);
    }

    private Specification<PlayerEntity> bannedEq(Boolean banned) {
        return ((root, query, criteriaBuilder) ->
                banned != null ? criteriaBuilder.equal(root.get(PlayerParams.banned), banned) : null);
    }

    private Specification<PlayerEntity> filterMinExp(Integer minExperience) {
        return ((root, query, criteriaBuilder) ->
                minExperience != null ? criteriaBuilder.greaterThanOrEqualTo(root.get(PlayerParams.experience), minExperience) : null);
    }

    private Specification<PlayerEntity> filterMaxExp(Integer maxExperience) {
        return ((root, query, criteriaBuilder) ->
                maxExperience != null ? criteriaBuilder.lessThanOrEqualTo(root.get(PlayerParams.experience), maxExperience) : null);
    }

    private Specification<PlayerEntity> filterMinLevel(Integer minLevel) {
        return ((root, query, criteriaBuilder) ->
                minLevel != null ? criteriaBuilder.greaterThanOrEqualTo(root.get(PlayerParams.level), minLevel) : null);
    }

    private Specification<PlayerEntity> filterMaxLevel(Integer maxLevel) {
        return ((root, query, criteriaBuilder) ->
                maxLevel != null ? criteriaBuilder.lessThanOrEqualTo(root.get(PlayerParams.level), maxLevel) : null);
    }

    class PlayerParams {
        private Map<String, String> allParams;
        public static final String name = "name";
        public static final String title = "title";
        public static final String race = "race";
        public static final String profession = "profession";
        public static final String after = "after";
        public static final String before = "before";
        public static final String birthday = "birthday";
        public static final String banned = "banned";
        public static final String experience = "experience";
        public static final String minExperience = "minExperience";
        public static final String maxExperience = "maxExperience";
        public static final String level = "level";
        public static final String minLevel = "minLevel";
        public static final String maxLevel = "maxLevel";
        public static final String order = "order";
        public static final String pageNumber = "pageNumber";
        public static final String pageSize = "pageSize";

        public PlayerParams(Map<String, String> allParams) {
            this.allParams = allParams;
        }

        public String getNameValue() {
            return allParams.get(PlayerParams.name);
        }

        public String getTitleValue() {
            return allParams.get(PlayerParams.title);
        }

        public Race getRaceValue() {
            try {
                return Race.valueOf(allParams.get(PlayerParams.race));
            } catch (Exception e) {
                return null;
            }
        }

        public Profession getProfessionValue() {
            try {
                return Profession.valueOf(allParams.get(PlayerParams.profession));
            } catch (Exception e) {
                return null;
            }
        }

        public Date getAfterBirthdayValue() {
            try {
                long ms = Long.parseLong(allParams.get(PlayerParams.after));
                return new Date(ms);
            } catch (Exception e) {
                return null;
            }
        }

        public Date getBeforeBirthdayValue() {
            try {
                long ms = Long.parseLong(allParams.get(PlayerParams.before));
                return new Date(ms);
            } catch (Exception e) {
                return null;
            }
        }

        public Boolean getBannedValue() {
            if ("true".equals(allParams.get(PlayerParams.banned))) return true;
            if ("false".equals(allParams.get(PlayerParams.banned))) return false;
            return null;
        }

        public Integer getMinExperience() {
            try {
                return Integer.parseInt(allParams.get(PlayerParams.minExperience));
            } catch (Exception e) {
                return null;
            }
        }

        public Integer getMaxExperience() {
            try {
                return Integer.parseInt(allParams.get(PlayerParams.maxExperience));
            } catch (Exception e) {
                return null;
            }
        }

        public Integer getMinLevel() {
            try {
                return Integer.parseInt(allParams.get(PlayerParams.minLevel));
            } catch (Exception e) {
                return null;
            }
        }

        public Integer getMaxLevel() {
            try {
                return Integer.parseInt(allParams.get(PlayerParams.maxLevel));
            } catch (Exception e) {
                return null;
            }
        }

        public Pageable getPaginationAndSort() {
            try {
                int pageSize = Integer.parseInt(allParams.get(PlayerParams.pageSize));
                int pageNumber = Integer.parseInt(allParams.get(PlayerParams.pageNumber));
                PlayerOrder playerOrder = allParams.get(PlayerParams.order) != null ?
                        PlayerOrder.valueOf(allParams.get(PlayerParams.order)) :
                        PlayerOrder.ID;

                return PageRequest.of(pageNumber, pageSize, Sort.by(playerOrder.getFieldName()).ascending());
            } catch (Exception e) {
                return PageRequest.of(0, 3, Sort.by(PlayerOrder.ID.getFieldName()).ascending());
            }
        }
    }
}
