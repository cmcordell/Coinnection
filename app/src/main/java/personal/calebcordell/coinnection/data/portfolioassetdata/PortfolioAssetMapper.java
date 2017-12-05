package personal.calebcordell.coinnection.data.portfolioassetdata;

import personal.calebcordell.coinnection.data.base.BaseMapper;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;


public class PortfolioAssetMapper extends BaseMapper<PortfolioAsset, PortfolioAssetEntity> {
    
    public PortfolioAsset mapUp(PortfolioAssetEntity entity) {
        PortfolioAsset portfolioAsset = new PortfolioAsset();
        
        portfolioAsset.setId(entity.getId());
        portfolioAsset.setBalance(entity.getBalance());
        portfolioAsset.setPosition(entity.getPosition());
        portfolioAsset.setName(entity.getName());
        portfolioAsset.setSymbol(entity.getSymbol());
        portfolioAsset.setRank(entity.getRank());
        portfolioAsset.setPrice(entity.getPrice());
        portfolioAsset.setVolume24Hour(entity.getVolume24Hour());
        portfolioAsset.setMarketCap(entity.getMarketCap());
        portfolioAsset.setAvailableSupply(entity.getAvailableSupply());
        portfolioAsset.setTotalSupply(entity.getTotalSupply());
        portfolioAsset.setPercentChange1Hour(entity.getPercentChange1h());
        portfolioAsset.setPercentChange24Hour(entity.getPercentChange24h());
        portfolioAsset.setPercentChange7Day(entity.getPercentChange7d());
        portfolioAsset.setLastUpdated(entity.getLastUpdated());
        
        return portfolioAsset;
    }
    
    public PortfolioAssetEntity mapDown(PortfolioAsset asset) {
        PortfolioAssetEntity portfolioAssetEntity = new PortfolioAssetEntity(asset.getId());

        portfolioAssetEntity.setBalance(asset.getBalance());
        portfolioAssetEntity.setPosition(asset.getPosition());
        portfolioAssetEntity.setName(asset.getName());
        portfolioAssetEntity.setSymbol(asset.getSymbol());
        portfolioAssetEntity.setRank(asset.getRank());
        portfolioAssetEntity.setPrice(asset.getPrice());
        portfolioAssetEntity.setVolume24Hour(asset.getVolume24Hour());
        portfolioAssetEntity.setMarketCap(asset.getMarketCap());
        portfolioAssetEntity.setAvailableSupply(asset.getAvailableSupply());
        portfolioAssetEntity.setTotalSupply(asset.getTotalSupply());
        portfolioAssetEntity.setPercentChange1h(asset.getPercentChange1Hour());
        portfolioAssetEntity.setPercentChange24h(asset.getPercentChange24Hour());
        portfolioAssetEntity.setPercentChange7d(asset.getPercentChange7Day());
        portfolioAssetEntity.setLastUpdated(asset.getLastUpdated());

        return portfolioAssetEntity;
    }
}