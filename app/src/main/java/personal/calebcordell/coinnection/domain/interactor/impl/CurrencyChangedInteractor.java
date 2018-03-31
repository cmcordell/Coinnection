package personal.calebcordell.coinnection.domain.interactor.impl;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor1;
import personal.calebcordell.coinnection.domain.repository.AssetRepository;
import personal.calebcordell.coinnection.domain.repository.GlobalMarketDataRepository;


public class CurrencyChangedInteractor extends CompletableInteractor1 {
    private static final String TAG = CurrencyChangedInteractor.class.getSimpleName();

    private final AssetRepository mAssetRepository;
    private final GlobalMarketDataRepository mGlobalMarketDataRepository;

    @Inject
    public CurrencyChangedInteractor(AssetRepository assetRepository,
                                     GlobalMarketDataRepository globalMarketDataRepository) {
        mAssetRepository = assetRepository;
        mGlobalMarketDataRepository = globalMarketDataRepository;
    }

    protected Completable buildCompletable() {
        return mAssetRepository.fetchAllAssets(true)
                .andThen(mGlobalMarketDataRepository.fetchGlobalMarketData())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
