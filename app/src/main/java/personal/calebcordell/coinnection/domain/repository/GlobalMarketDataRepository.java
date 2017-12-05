package personal.calebcordell.coinnection.domain.repository;

import io.reactivex.Completable;
import personal.calebcordell.coinnection.domain.model.GlobalMarketData;

import io.reactivex.Flowable;


public interface GlobalMarketDataRepository {
    Flowable<GlobalMarketData> getGlobalMarketData();

    Completable fetchGlobalMarketData();
}