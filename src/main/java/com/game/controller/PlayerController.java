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
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Cannot get all players");
        }
    }

    @GetMapping("/players/count")
    public ResponseEntity getPlayersCount(@RequestParam Map<String, String> allParams) {
        try {
            return ResponseEntity.ok(playerService.getPlayersCount(allParams));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cannot get all players count");
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
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Player creation Error");
        }
    }
}
