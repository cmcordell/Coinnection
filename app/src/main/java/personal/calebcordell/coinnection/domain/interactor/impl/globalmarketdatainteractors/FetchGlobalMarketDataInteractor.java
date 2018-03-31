package personal.calebcordell.coinnection.domain.interactor.impl.globalmarketdatainteractors;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor1;
import personal.calebcordell.coinnection.domain.repository.GlobalMarketDataRepository;


public class FetchGlobalMarketDataInteractor extends CompletableInteractor1 {
    private static final String TAG = FetchGlobalMarketDataInteractor.class.getSimpleName();

    private final GlobalMarketDataRepository mGlobalMarketDataRepository;

    @Inject
    public FetchGlobalMarketDataInteractor(GlobalMarketDataRepository globalMarketDataRepository) {
        mGlobalMarketDataRepository = globalMarketDataRepository;
    }

    protected Completable buildCompletable() {
        return mGlobalMarketDataRepository.fetchGlobalMarketData()
                .observeOn(AndroidSchedulers.mainThread());
    }
}