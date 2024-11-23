package com.zosh.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zosh.model.Coins;
import com.zosh.service.CoinsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coins")
@RequiredArgsConstructor
public class CoinController {

    @Autowired
    private final CoinsService coinsService;;
    @Autowired
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<Coins>> getCoinList(@RequestParam("page") int page) throws Exception {
        List<Coins> coinsList = coinsService.getCoinList(page);
        return new ResponseEntity<>(coinsList, HttpStatus.OK);
    }

    @GetMapping("/{coinId}/chart")
    public ResponseEntity<JsonNode> getMarketChart(@PathVariable String coinId,
                                                      @RequestParam("days") int days) throws Exception {
        String response = coinsService.getMarketChart(coinId, days);
        JsonNode jsonNode = objectMapper.readTree(response);
        return new ResponseEntity<>(jsonNode, HttpStatus.OK);
    }


    @GetMapping("/search")
    public ResponseEntity<JsonNode> searchCoin(@RequestParam("q") String keyword) throws Exception {
        String coin = coinsService.searchCoin(keyword);
        JsonNode jsonNode = objectMapper.readTree(coin);
        return new ResponseEntity<>(jsonNode, HttpStatus.OK);
    }

    @GetMapping("/top50")
    public ResponseEntity<JsonNode> getTop50CoinByMarketCapRank() throws Exception {
        String coin = coinsService.getTop50CoinsByMarketCapRank();
        JsonNode jsonNode = objectMapper.readTree(coin);
        return new ResponseEntity<>(jsonNode, HttpStatus.OK);
    }

    @GetMapping("/trading")
    public ResponseEntity<JsonNode> getTradingCoin() throws Exception {
        String coin = coinsService.getTradingCoins();
        JsonNode jsonNode = objectMapper.readTree(coin);
        return new ResponseEntity<>(jsonNode, HttpStatus.OK);
    }

    @GetMapping("/trading")
    public ResponseEntity<JsonNode> getCoinDetails(@PathVariable String coinId) throws Exception {
        String coin = coinsService.getCoinDetails(coinId);
        JsonNode jsonNode = objectMapper.readTree(coin);
        return new ResponseEntity<>(jsonNode, HttpStatus.OK);
    }
}
