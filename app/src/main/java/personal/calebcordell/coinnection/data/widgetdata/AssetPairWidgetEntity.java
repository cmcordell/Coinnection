package personal.calebcordell.coinnection.data.widgetdata;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "asset_pair_widgets")
public class AssetPairWidgetEntity {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "asset_id")
    private String assetId;

    @ColumnInfo(name = "quote_currency_symbol")
    private String quoteCurrencySymbol;

    private String name;

    private String symbol;

    private int rank;

    private double price;

    @ColumnInfo(name = "volume_24h")
    private double volume24Hour;

    @ColumnInfo(name = "market_cap")
    private double marketCap;

    @ColumnInfo(name = "available_supply")
    private double availableSupply;

    @ColumnInfo(name = "total_supply")
    private double totalSupply;

    @ColumnInfo(name = "max_supply")
    private double maxSupply;

    @ColumnInfo(name = "percent_change_1h")
    private double percentChange1h;

    @ColumnInfo(name = "percent_change_24h")
    private double percentChange24h;

    @ColumnInfo(name = "percent_change_7d")
    private double percentChange7d;

    @ColumnInfo(name = "last_update")
    private long lastUpdated;

    public AssetPairWidgetEntity(int id,
                                 String assetId,
                                 String quoteCurrencySymbol,
                                 String name,
                                 String symbol,
                                 int rank,
                                 double price,
                                 double volume24Hour,
                                 double marketCap,
                                 double availableSupply,
                                 double totalSupply,
                                 double maxSupply,
                                 double percentChange1h,
                                 double percentChange24h,
                                 double percentChange7d,
                                 long lastUpdated) {
        this.id = id;
        this.assetId = assetId;
        this.quoteCurrencySymbol = quoteCurrencySymbol;
        this.name = name;
        this.symbol = symbol;
        this.rank = rank;
        this.price = price;
        this.volume24Hour = volume24Hour;
        this.marketCap = marketCap;
        this.availableSupply = availableSupply;
        this.totalSupply = totalSupply;
        this.maxSupply = maxSupply;
        this.percentChange1h = percentChange1h;
        this.percentChange24h = percentChange24h;
        this.percentChange7d = percentChange7d;
        this.lastUpdated = lastUpdated;
    }


    public int getId() {
        return id;
    }

    public String getAssetId() {
        return assetId;
    }

    public String getQuoteCurrencySymbol() {
        return quoteCurrencySymbol;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getRank() {
        return rank;
    }

    public double getPrice() {
        return price;
    }

    public double getVolume24Hour() {
        return volume24Hour;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public double getAvailableSupply() {
        return availableSupply;
    }

    public double getTotalSupply() {
        return totalSupply;
    }

    public double getMaxSupply() {
        return maxSupply;
    }

    public double getPercentChange1h() {
        return percentChange1h;
    }

    public double getPercentChange24h() {
        return percentChange24h;
    }

    public double getPercentChange7d() {
        return percentChange7d;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }
}