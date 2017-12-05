package personal.calebcordell.coinnection.data.watchlistassetdata;

import personal.calebcordell.coinnection.data.base.BaseMapper;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;


public class WatchlistAssetMapper extends BaseMapper<WatchlistAsset, WatchlistAssetEntity> {

    public WatchlistAsset mapUp(WatchlistAssetEntity entity) {
        WatchlistAsset watchlistAsset = new WatchlistAsset();

        watchlistAsset.setId(entity.getId());
        watchlistAsset.setPosition(entity.getPosition());
        watchlistAsset.setName(entity.getName());
        watchlistAsset.setSymbol(entity.getSymbol());
        watchlistAsset.setRank(entity.getRank());
        watchlistAsset.setPrice(entity.getPrice());
        watchlistAsset.setVolume24Hour(entity.getVolume24Hour());
        watchlistAsset.setMarketCap(entity.getMarketCap());
        watchlistAsset.setAvailableSupply(entity.getAvailableSupply());
        watchlistAsset.setTotalSupply(entity.getTotalSupply());
        watchlistAsset.setPercentChange1Hour(entity.getPercentChange1h());
        watchlistAsset.setPercentChange24Hour(entity.getPercentChange24h());
        watchlistAsset.setPercentChange7Day(entity.getPercentChange7d());
        watchlistAsset.setLastUpdated(entity.getLastUpdated());

        return watchlistAsset;
    }

    public WatchlistAssetEntity mapDown(WatchlistAsset asset) {
        WatchlistAssetEntity watchlistAssetEntity = new WatchlistAssetEntity(asset.getId());

        watchlistAssetEntity.setPosition(asset.getPosition());
        watchlistAssetEntity.setName(asset.getName());
        watchlistAssetEntity.setSymbol(asset.getSymbol());
        watchlistAssetEntity.setRank(asset.getRank());
        watchlistAssetEntity.setPrice(asset.getPrice());
        watchlistAssetEntity.setVolume24Hour(asset.getVolume24Hour());
        watchlistAssetEntity.setMarketCap(asset.getMarketCap());
        watchlistAssetEntity.setAvailableSupply(asset.getAvailableSupply());
        watchlistAssetEntity.setTotalSupply(asset.getTotalSupply());
        watchlistAssetEntity.setPercentChange1h(asset.getPercentChange1Hour());
        watchlistAssetEntity.setPercentChange24h(asset.getPercentChange24Hour());
        watchlistAssetEntity.setPercentChange7d(asset.getPercentChange7Day());
        watchlistAssetEntity.setLastUpdated(asset.getLastUpdated());

        return watchlistAssetEntity;
    }
}