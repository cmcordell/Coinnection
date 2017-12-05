package personal.calebcordell.coinnection.data.globalmarketdata;

import android.support.annotation.NonNull;

import io.reactivex.functions.Function;
import personal.calebcordell.coinnection.domain.model.GlobalMarketData;


public class GlobalMarketDataMapperFunction implements Function<GlobalMarketDataEntity, GlobalMarketData> {
    private static final double PERCENT_MODIFIER = 100.0;

    public GlobalMarketDataMapperFunction() {
    }

    @Override
    public GlobalMarketData apply(@NonNull final GlobalMarketDataEntity globalMarketDataEntity) throws Exception {

        GlobalMarketData globalMarketData = new GlobalMarketData();
        globalMarketData.setTotalMarketCap(globalMarketDataEntity.getTotalMarketCap());
        globalMarketData.setTotalVolume24hour(globalMarketDataEntity.getTotalVolume24hour());
        globalMarketData.setBitcoinPercentageOfMarketCap(globalMarketDataEntity.getBitcoinPercentageOfMarketCap() / PERCENT_MODIFIER);
        globalMarketData.setActiveCurrencies(globalMarketDataEntity.getActiveCurrencies());
        globalMarketData.setActiveAssets(globalMarketDataEntity.getActiveAssets());
        globalMarketData.setActiveMarkets(globalMarketDataEntity.getActiveMarkets());
        globalMarketData.setLastUpdated(System.currentTimeMillis());

        return globalMarketData;
    }
}