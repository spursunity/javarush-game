package com.game.repository;

import com.game.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PlayerRepo extends JpaRepository<PlayerEntity, Long>, JpaSpecificationExecutor<PlayerEntity> {
}
