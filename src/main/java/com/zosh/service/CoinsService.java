package com.zosh.service;

import com.zosh.model.Coins;

import java.util.List;

public interface CoinsService {

    List<Coins> getCoinList(int page) throws Exception;

    String getMarketChart(String coinId, int days) throws Exception;

    String getCoinDetails(String coinId) throws Exception;

    Coins findById(String coinId) throws Exception;

    String searchCoin(String keyWord) throws Exception;

    String getTop50CoinsByMarketCapRank() throws Exception;

    String getTradingCoins() throws Exception;
}
