package com.game.controller;

import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
