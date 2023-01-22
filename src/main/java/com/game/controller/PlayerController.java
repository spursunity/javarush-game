package com.game.controller;

import com.game.entity.PlayerEntity;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/rest")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/players")
    public ResponseEntity getPlayers(@RequestParam Map<String, String> allParams) {
        try {
            return ResponseEntity.ok(playerService.getPlayers(allParams));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Getting players Error");
        }
    }

    @GetMapping("/players/{id}")
    public ResponseEntity getPlayerById(@PathVariable("id") Long id) {
        try {
            if (id == null || id < 0) {
                return ResponseEntity.badRequest().body("Invalid ID");
            }
            PlayerEntity player = playerService.getPlayerById(id);
            if (player == null) return ResponseEntity.notFound().build();

            return ResponseEntity.ok(player);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Getting player by id Error");
        }
    }

    @GetMapping("/players/count")
    public ResponseEntity getPlayersCount(@RequestParam Map<String, String> allParams) {
        try {
            return ResponseEntity.ok(playerService.getPlayersCount(allParams));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Getting players count Error");
        }
    }

    @PostMapping("/players")
    public ResponseEntity createPlayer(@RequestBody PlayerEntity player) {
        try {
            player.setLevelData();
            if (player.validateInputData()) {
                PlayerEntity createdPlayer = playerService.createPlayer(player);
                return ResponseEntity.ok(createdPlayer);
            }
            return ResponseEntity.badRequest().body("Invalid Data");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Player creation Error");
        }
    }

    @PostMapping("/players/{id}")
    public ResponseEntity updatePlayer(@RequestBody PlayerEntity player, @PathVariable("id") Long id) {
        try {
            if (id == null || id < 0) {
                return ResponseEntity.badRequest().body("Invalid ID");
            }
            PlayerEntity foundPlayer = playerService.getPlayerById(id);
            if (foundPlayer == null) return ResponseEntity.notFound().build();

            return ResponseEntity.ok(playerService.updatePlayer(foundPlayer, player));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Player update Error");
        }
    }
}
