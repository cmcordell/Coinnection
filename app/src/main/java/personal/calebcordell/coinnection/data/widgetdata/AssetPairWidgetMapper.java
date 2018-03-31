package personal.calebcordell.coinnection.data.widgetdata;

import javax.inject.Inject;
import javax.inject.Singleton;

import personal.calebcordell.coinnection.data.base.BaseMapper;
import personal.calebcordell.coinnection.domain.model.AssetPair;
import personal.calebcordell.coinnection.domain.model.AssetPairWidget;
import personal.calebcordell.coinnection.domain.model.LogoUtil;


@Singleton
public class AssetPairWidgetMapper extends BaseMapper<AssetPairWidget, AssetPairWidgetEntity> {

    @Inject
    public AssetPairWidgetMapper() {}

    public AssetPairWidget mapUp(AssetPairWidgetEntity entity) {
        AssetPair assetPair = new AssetPair();
        assetPair.setId(entity.getAssetId());
        assetPair.setQuoteCurrencySymbol(entity.getQuoteCurrencySymbol());
        assetPair.setLogo(LogoUtil.getLogo(entity.getAssetId()));
        assetPair.setName(entity.getName());
        assetPair.setSymbol(entity.getSymbol());
        assetPair.setRank(entity.getRank());
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

        return new AssetPairWidget(entity.getId(), assetPair);
    }

    public AssetPairWidgetEntity mapDown(AssetPairWidget assetPairWidget) {
        AssetPair assetPair = assetPairWidget.getAssetPair();

        return new AssetPairWidgetEntity(assetPairWidget.getId(),
                assetPair.getId(),
                assetPair.getQuoteCurrencySymbol(),
                assetPair.getName(),
                assetPair.getSymbol(),
                assetPair.getRank(),
                assetPair.getPrice(),
                assetPair.getVolume24Hour(),
                assetPair.getMarketCap(),
                assetPair.getAvailableSupply(),
                assetPair.getTotalSupply(),
                assetPair.getMaxSupply(),
                assetPair.getPercentChange1Hour(),
                assetPair.getPercentChange24Hour(),
                assetPair.getPercentChange7Day(),
                assetPair.getLastUpdated());
    }
}
