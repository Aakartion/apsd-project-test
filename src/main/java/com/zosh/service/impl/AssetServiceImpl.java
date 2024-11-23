package com.zosh.service.impl;

import com.zosh.model.Asset;
import com.zosh.model.Coins;
import com.zosh.model.Users;
import com.zosh.repository.AssetRepository;
import com.zosh.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

    @Autowired
    private final AssetRepository assetRepository;


    @Override
    public Asset createAsset(Users user, Coins coin, double quantity) {
        Asset asset = new Asset();
        asset.setUser(user);
        asset.setCoins(coin);
        asset.setAssetQuantity(quantity);
        asset.setAssetBuyPrice(asset.getAssetQuantity());
        return assetRepository.save(asset);
    }

    @Override
    public Asset getAssetById(Long assetId) throws Exception {

        return assetRepository.findById(assetId).orElseThrow(()-> new Exception("Asset Not Found"));
    }

    @Override
    public Asset getAssetByUserIdAndAssetId(Long userId, Long assetId) {

        return null;
    }

    @Override
    public List<Asset> getAssetsByUserId(Long userId) {
        return assetRepository.findAssetsByUserId(userId);
    }

    @Override
    public Asset updateAsset(Long assetId, double quantity) throws Exception {
        Asset oldAsset =  getAssetById(assetId);
        oldAsset.setAssetQuantity(quantity + oldAsset.getAssetQuantity());
        return assetRepository.save(oldAsset);
    }

    @Override
    public Asset findAssetByUserIdAndCoinId(Long userId, String coinId) {

        return assetRepository.findAssetsByUserIdAndCoinsId(userId, coinId);
    }

    @Override
    public void deleteAsset(Long assetId) {
        assetRepository.deleteById(assetId);
    }
}
