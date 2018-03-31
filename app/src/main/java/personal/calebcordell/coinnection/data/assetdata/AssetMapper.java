package personal.calebcordell.coinnection.data.assetdata;

import javax.inject.Inject;
import javax.inject.Singleton;

import personal.calebcordell.coinnection.data.base.BaseMapper;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.LogoUtil;


@Singleton
public class AssetMapper extends BaseMapper<Asset, AssetEntity> {
    private static final double PERCENT_MODIFIER = 100.0;

    @Inject
    public AssetMapper() {
    }

    public Asset mapUp(AssetEntity entity) {
        Asset asset = new Asset();

        asset.setId(entity.getId());
        asset.setName(entity.getName());
        asset.setSymbol(entity.getSymbol());
        asset.setRank(entity.getRank());
        asset.setLogo(LogoUtil.getLogo(entity.getId()));
        asset.setPrice(entity.getPrice());
        asset.setVolume24Hour(entity.getVolume24Hour());
        asset.setMarketCap(entity.getMarketCap());
        asset.setAvailableSupply(entity.getAvailableSupply());
        asset.setTotalSupply(entity.getTotalSupply());
        asset.setMaxSupply(entity.getMaxSupply());
        asset.setPercentChange1Hour(entity.getPercentChange1h() / PERCENT_MODIFIER);
        asset.setPercentChange24Hour(entity.getPercentChange24h() / PERCENT_MODIFIER);
        asset.setPercentChange7Day(entity.getPercentChange7d() / PERCENT_MODIFIER);
        asset.setLastUpdated(entity.getLastUpdated());

        return asset;
    }

    public AssetEntity mapDown(Asset asset) {
        AssetEntity assetEntity = new AssetEntity();

        assetEntity.setId(asset.getId());
        assetEntity.setName(asset.getName());
        assetEntity.setSymbol(asset.getSymbol());
        assetEntity.setRank(asset.getRank());
        assetEntity.setPrice(asset.getPrice());
        assetEntity.setVolume24Hour(asset.getVolume24Hour());
        assetEntity.setMarketCap(asset.getMarketCap());
        assetEntity.setAvailableSupply(asset.getAvailableSupply());
        assetEntity.setTotalSupply(asset.getTotalSupply());
        assetEntity.setMaxSupply(asset.getMaxSupply());
        assetEntity.setPercentChange1h(asset.getPercentChange1Hour());
        assetEntity.setPercentChange24h(asset.getPercentChange24Hour());
        assetEntity.setPercentChange7d(asset.getPercentChange7Day());
        assetEntity.setLastUpdated(asset.getLastUpdated());

        return assetEntity;
    }
}