package personal.calebcordell.coinnection.domain.interactor.impl.globalmarketdatainteractors;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.FlowableInteractor1;
import personal.calebcordell.coinnection.domain.model.GlobalMarketData;
import personal.calebcordell.coinnection.domain.repository.GlobalMarketDataRepository;


public class GetGlobalMarketDataInteractor extends FlowableInteractor1<GlobalMarketData> {
    private static final String TAG = GetGlobalMarketDataInteractor.class.getSimpleName();

    private final GlobalMarketDataRepository mGlobalMarketDataRepository;

    @Inject
    public GetGlobalMarketDataInteractor(GlobalMarketDataRepository globalMarketDataRepository) {
        mGlobalMarketDataRepository = globalMarketDataRepository;
    }

    protected Flowable<GlobalMarketData> buildFlowable() {
        return mGlobalMarketDataRepository.getGlobalMarketData()
                .observeOn(AndroidSchedulers.mainThread());
    }
}