package com.zosh.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "coins")
@Data
@NoArgsConstructor
public class Coins {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @JsonProperty("id")
    private String id; // Example: "bitcoin"

    @Column(name = "symbol", nullable = false)
    @JsonProperty("symbol")
    private String symbol; // Example: "btc"

    @Column(name = "name", nullable = false)
    @JsonProperty("name")
    private String name; // Example: "Bitcoin"

    @Column(name = "image_url")
    @JsonProperty("image")
    private String image; // Example: URL for image

    @Column(name = "current_price", nullable = false)
    @JsonProperty("current_price")
    private Double currentPrice; // Example: 90686

    @Column(name = "market_cap", nullable = false)
    @JsonProperty("market_cap")
    private Long marketCap; // Example: 1791022964984

    @Column(name = "market_cap_rank", nullable = false)
    @JsonProperty("market_cap_rank")
    private Integer marketCapRank; // Example: 1

    @Column(name = "fully_diluted_valuation")
    @JsonProperty("fully_diluted_valuation")
    private Long fullyDilutedValuation; // Example: 1901131619777

    @Column(name = "total_volume", nullable = false)
    @JsonProperty("total_volume")
    private Long totalVolume; // Example: 49951785346

    @Column(name = "high_24h", nullable = false)
    @JsonProperty("high_24h")
    private Double high24h; // Example: 91439

    @Column(name = "low_24h", nullable = false)
    @JsonProperty("low_24h")
    private Double low24h; // Example: 88774

    @Column(name = "price_change_24h")
    @JsonProperty("price_change_24h")
    private Double priceChange24h; // Example: 352.43

    @Column(name = "price_change_percentage_24h")
    @JsonProperty("price_change_percentage_24h")
    private Double priceChangePercentage24h; // Example: 0.39014

    @Column(name = "market_cap_change_24h")
    @JsonProperty("market_cap_change_24h")
    private Long marketCapChange24h; // Example: 7583944383

    @Column(name = "market_cap_change_percentage_24h")
    @JsonProperty("market_cap_change_percentage_24h")
    private Double marketCapChangePercentage24h; // Example: 0.42524

    @Column(name = "circulating_supply")
    @JsonProperty("circulating_supply")
    private Double circulatingSupply; // Example: 19783734

    @Column(name = "total_supply")
    @JsonProperty("total_supply")
    private Double totalSupply; // Example: 21000000

    @Column(name = "max_supply")
    @JsonProperty("max_supply")
    private Double maxSupply; // Example: 21000000

    @Column(name = "ath") // All Time High
    @JsonProperty("ath")
    private Double ath; // Example: 93477

    @Column(name = "ath_change_percentage")
    @JsonProperty("ath_change_percentage")
    private Double athChangePercentage; // Example: -3.01525

    @Column(name = "ath_date")
    @JsonProperty("ath_date")
    private LocalDateTime athDate; // Example: "2024-11-13T16:15:35.520Z"

    @Column(name = "atl") // All Time Low
    @JsonProperty("atl")
    private Double atl; // Example: 67.81

    @Column(name = "atl_change_percentage")
    @JsonProperty("atl_change_percentage")
    private Double atlChangePercentage; // Example: 133596.92098

    @Column(name = "atl_date")
    @JsonProperty("atl_date")
    private LocalDateTime atlDate; // Example: "2013-07-06T00:00:00.000Z"

    @Column(name = "roi")
    @JsonProperty("roi")
    private Double roi; // Example: null

    @Column(name = "last_updated")
    @JsonProperty("last_updated")
    private LocalDateTime lastUpdated; // Example: "2024-11-18T04:04:46.262Z"

}