package personal.calebcordell.coinnection.data.globalmarketdata;

import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.functions.Function;
import personal.calebcordell.coinnection.domain.model.GlobalMarketData;


@Singleton
public class GlobalMarketDataMapperFunction implements Function<GlobalMarketDataEntity, GlobalMarketData> {
    private static final double PERCENT_MODIFIER = 100.0;

    @Inject
    public GlobalMarketDataMapperFunction() {
    }

    @Override
    public GlobalMarketData apply(@NonNull final GlobalMarketDataEntity globalMarketDataEntity) {

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