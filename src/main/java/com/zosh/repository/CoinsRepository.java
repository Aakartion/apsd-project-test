package com.zosh.repository;

import com.zosh.model.Coins;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinsRepository extends JpaRepository<Coins, String> {
}
