package com.zosh.repository;

import com.zosh.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    List<Asset> findAssetsByUserId(Long userId);

    Asset findAssetsByUserIdAndCoinsId (Long userId, String coinId);

}
