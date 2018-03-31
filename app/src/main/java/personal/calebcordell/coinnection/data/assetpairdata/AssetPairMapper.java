package personal.calebcordell.coinnection.data.assetpairdata;

import javax.inject.Inject;
import javax.inject.Singleton;

import personal.calebcordell.coinnection.data.assetdata.AssetEntity;
import personal.calebcordell.coinnection.data.base.BaseMapper;
import personal.calebcordell.coinnection.domain.model.AssetPair;
import personal.calebcordell.coinnection.domain.model.LogoUtil;


@Singleton
public class AssetPairMapper extends BaseMapper<AssetPair, AssetPairEntity> {
    private static final String TAG = AssetPairMapper.class.getSimpleName();

    private static final double PERCENT_MODIFIER = 100.0;

    @Inject
    public AssetPairMapper() {}

    public AssetPair mapUp(AssetPairEntity entity) {
        AssetPair assetPair = new AssetPair();

        assetPair.setId(entity.getId());
        assetPair.setQuoteCurrencySymbol(entity.getQuoteCurrencySymbol());
        assetPair.setPosition(entity.getPosition());
        assetPair.setName(entity.getName());
        assetPair.setSymbol(entity.getSymbol());
        assetPair.setRank(entity.getRank());
        assetPair.setLogo(LogoUtil.getLogo(entity.getId()));
        assetPair.setPrice(entity.getPrice());
        assetPair.setVolume24Hour(entity.getVolume24Hour());
        assetPair.setMarketCap(entity.getMarketCap());
        assetPair.setAvailableSupply(entity.getAvailableSupply());
        assetPair.setTotalSupply(entity.getTotalSupply());
        assetPair.setMaxSupply(entity.getMaxSupply());
        assetPair.setPercentChange1Hour(entity.getPercentChange1h());
        assetPair.setPercentChange24Hour(entity.getPercentChange24h());
        assetPair.setPercentChange7Day(entity.getPercentChange7d());
        assetPair.setLastUpdated(entity.getLastUpdated());

        return assetPair;
    }

    public AssetPairEntity mapDown(AssetPair assetPair) {
        AssetPairEntity assetPairEntity = new AssetPairEntity(assetPair.getId(), assetPair.getQuoteCurrencySymbol());

        assetPairEntity.setPosition(assetPair.getPosition());
        assetPairEntity.setName(assetPair.getName());
        assetPairEntity.setSymbol(assetPair.getSymbol());
        assetPairEntity.setRank(assetPair.getRank());
        assetPairEntity.setPrice(assetPair.getPrice());
        assetPairEntity.setVolume24Hour(assetPair.getVolume24Hour());
        assetPairEntity.setMarketCap(assetPair.getMarketCap());
        assetPairEntity.setAvailableSupply(assetPair.getAvailableSupply());
        assetPairEntity.setTotalSupply(assetPair.getTotalSupply());
        assetPairEntity.setMaxSupply(assetPair.getMaxSupply());
        assetPairEntity.setPercentChange1h(assetPair.getPercentChange1Hour());
        assetPairEntity.setPercentChange24h(assetPair.getPercentChange24Hour());
        assetPairEntity.setPercentChange7d(assetPair.getPercentChange7Day());
        assetPairEntity.setLastUpdated(assetPair.getLastUpdated());

        return assetPairEntity;
    }

    public static AssetPairEntity map(AssetEntity assetEntity, String quoteCurrencySymbol) {
        AssetPairEntity assetPairEntity = new AssetPairEntity(assetEntity.getId(), quoteCurrencySymbol);
        assetPairEntity.setPosition(-1);
        assetPairEntity.setName(assetEntity.getName());
        assetPairEntity.setSymbol(assetEntity.getSymbol());
        assetPairEntity.setRank(assetEntity.getRank());
        assetPairEntity.setPrice(assetEntity.getPrice());
        assetPairEntity.setVolume24Hour(assetEntity.getVolume24Hour());
        assetPairEntity.setMarketCap(assetEntity.getMarketCap());
        assetPairEntity.setAvailableSupply(assetEntity.getAvailableSupply());
        assetPairEntity.setTotalSupply(assetEntity.getTotalSupply());
        assetPairEntity.setMaxSupply(assetEntity.getMaxSupply());
        assetPairEntity.setPercentChange1h(assetEntity.getPercentChange1h() / PERCENT_MODIFIER);
        assetPairEntity.setPercentChange24h(assetEntity.getPercentChange24h() / PERCENT_MODIFIER);
        assetPairEntity.setPercentChange7d(assetEntity.getPercentChange7d() / PERCENT_MODIFIER);
        assetPairEntity.setLastUpdated(assetEntity.getLastUpdated());

        return assetPairEntity;
    }
}