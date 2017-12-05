package personal.calebcordell.coinnection.domain.interactor.impl;

import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.data.assetdata.AssetRepositoryImpl;
import personal.calebcordell.coinnection.data.globalmarketdata.GlobalMarketDataRepositoryImpl;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor1;
import personal.calebcordell.coinnection.domain.repository.AssetRepository;
import personal.calebcordell.coinnection.domain.repository.GlobalMarketDataRepository;

import io.reactivex.Completable;


public class CurrencyChangedInteractor extends CompletableInteractor1 {
    private static final String TAG = CurrencyChangedInteractor.class.getSimpleName();

    private AssetRepository mAssetRepository;
    private GlobalMarketDataRepository mGlobalMarketDataRepository;

    public CurrencyChangedInteractor() {
        mAssetRepository = new AssetRepositoryImpl();
        mGlobalMarketDataRepository = new GlobalMarketDataRepositoryImpl();
    }

    protected Completable buildCompletable() {
        return mAssetRepository.fetchAllAssets(true)
                .andThen(mGlobalMarketDataRepository.fetchGlobalMarketData())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
