package personal.calebcordell.coinnection.domain.interactor.impl;

import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.data.globalmarketdata.GlobalMarketDataRepositoryImpl;
import personal.calebcordell.coinnection.domain.interactor.base.FlowableInteractor1;
import personal.calebcordell.coinnection.domain.model.GlobalMarketData;
import personal.calebcordell.coinnection.domain.repository.GlobalMarketDataRepository;

import io.reactivex.Flowable;


public class GetGlobalMarketDataInteractor extends FlowableInteractor1<GlobalMarketData> {
    private static final String TAG = GetGlobalMarketDataInteractor.class.getSimpleName();

    private final GlobalMarketDataRepository mGlobalMarketDataRepository;

    public GetGlobalMarketDataInteractor() {
        mGlobalMarketDataRepository = new GlobalMarketDataRepositoryImpl();
    }

    protected Flowable<GlobalMarketData> buildFlowable() {
        return mGlobalMarketDataRepository.getGlobalMarketData()
                .observeOn(AndroidSchedulers.mainThread());
    }
}