package personal.calebcordell.coinnection.data.assetdata;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Parses JSON data from CoinMarketCap.com v1 into an AssetEntity object.  We need this because
 * API v1 JSON data has dynamic naming for price_***, 24h_volume_***, and market_cap_***
 */
@Singleton
public class AssetJsonDeserializer implements JsonDeserializer<AssetEntity> {

    @Inject
    public AssetJsonDeserializer() {}

    @Override
    public AssetEntity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();

        AssetEntity assetEntity = new AssetEntity();
        double priceBTC = 0;

        for (Map.Entry<String, JsonElement> entry : entries) {
            if (entry.getKey().contains("price")) {
                if (entry.getKey().equals("price_btc")) {
                    priceBTC = getNullAsEmptyDouble(entry.getValue());
                } else {
                    assetEntity.setPrice(getNullAsEmptyDouble(entry.getValue()));
                }
            } else if (entry.getKey().contains("24h_volume")) {
                assetEntity.setVolume24Hour(getNullAsEmptyDouble(entry.getValue()));
                if (entry.getKey().equals("24h_volume_btc")) {
                    assetEntity.setPrice(priceBTC);
                }
            } else if (entry.getKey().contains("market_cap")) {
                assetEntity.setMarketCap(getNullAsEmptyDouble(entry.getValue()));
            } else {
                switch (entry.getKey()) {
                    case "id":
                        assetEntity.setId(getNullAsEmptyString(entry.getValue()));
                        break;
                    case "name":
                        assetEntity.setName(getNullAsEmptyString(entry.getValue()));
                        break;
                    case "symbol":
                        assetEntity.setSymbol(getNullAsEmptyString(entry.getValue()));
                        break;
                    case "rank":
                        assetEntity.setRank(getNullAsEmptyInt(entry.getValue()));
                        break;
                    case "available_supply":
                        assetEntity.setAvailableSupply(getNullAsEmptyDouble(entry.getValue()));
                        break;
                    case "total_supply":
                        assetEntity.setTotalSupply(getNullAsEmptyDouble(entry.getValue()));
                        break;
                    case "max_supply":
                        assetEntity.setMaxSupply(getNullAsEmptyDouble(entry.getValue()));
                        break;
                    case "percent_change_1h":
                        assetEntity.setPercentChange1h(getNullAsEmptyDouble(entry.getValue()));
                        break;
                    case "percent_change_24h":
                        assetEntity.setPercentChange24h(getNullAsEmptyDouble(entry.getValue()));
                        break;
                    case "percent_change_7d":
                        assetEntity.setPercentChange7d(getNullAsEmptyDouble(entry.getValue()));
                        break;
                    case "last_updated":
                        assetEntity.setLastUpdated(System.currentTimeMillis());
                        break;
                }
            }
        }

        return assetEntity;
    }


    private String getNullAsEmptyString(JsonElement jsonElement) {
        return jsonElement.isJsonNull() ? "" : jsonElement.getAsString();
    }

    private double getNullAsEmptyDouble(JsonElement jsonElement) {
        return jsonElement.isJsonNull() ? 0 : jsonElement.getAsDouble();
    }

    private int getNullAsEmptyInt(JsonElement jsonElement) {
        return jsonElement.isJsonNull() ? 0 : jsonElement.getAsInt();
    }

    private long getNullAsEmptyLong(JsonElement jsonElement) {
        return jsonElement.isJsonNull() ? 0 : jsonElement.getAsLong();
    }
}