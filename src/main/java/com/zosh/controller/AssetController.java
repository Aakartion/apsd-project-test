package com.zosh.controller;

import com.zosh.model.Asset;
import com.zosh.model.Users;
import com.zosh.service.AssetService;
import com.zosh.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {

    @Autowired
    private final AssetService assetService;
    @Autowired
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Asset> getAssetById(@PathVariable Long assetId) throws Exception {
        Asset asset = assetService.getAssetById(assetId);
        return ResponseEntity.ok(asset);
    }

    @GetMapping("/coin/{coinId}/user")
    public ResponseEntity<Asset> getAssetByUserIdAndCoinId(@RequestHeader("Authorization") String jwtToken,
                                                           @PathVariable String coinId) throws Exception {
        Users user = userService.findUserByJwt(jwtToken);
        Asset asset = assetService.findAssetByUserIdAndCoinId(user.getId(), coinId);
        return ResponseEntity.ok(asset);
    }

    @GetMapping
    public ResponseEntity<List<Asset>> getAllAssetForUser(@RequestHeader("Authorization") String jwtToken) throws Exception {
        Users user = userService.findUserByJwt(jwtToken);
        List<Asset> assets = assetService.getAssetsByUserId(user.getId());
        return ResponseEntity.ok(assets);
    }




}
