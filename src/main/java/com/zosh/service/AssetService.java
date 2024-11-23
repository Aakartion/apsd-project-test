package com.zosh.service;

import com.zosh.model.Asset;
import com.zosh.model.Coins;
import com.zosh.model.Users;

import java.util.List;

public interface AssetService {

    Asset createAsset(Users user, Coins coin, double quantity);

    Asset getAssetById(Long assetId) throws Exception;
    Asset getAssetByUserIdAndAssetId(Long userId, Long assetId);
    List<Asset> getAssetsByUserId(Long userId);
    Asset updateAsset(Long assetId, double quantity) throws Exception;

    Asset findAssetByUserIdAndCoinId(Long userId, String coinId);

    void deleteAsset(Long assetId);
}
