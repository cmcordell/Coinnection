package personal.calebcordell.coinnection.domain.repository;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import personal.calebcordell.coinnection.domain.model.GlobalMarketData;


public interface GlobalMarketDataRepository {
    Flowable<GlobalMarketData> getGlobalMarketData();

    Completable fetchGlobalMarketData();
}