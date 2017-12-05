package personal.calebcordell.coinnection.domain.interactor.impl;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.data.watchlistassetdata.WatchlistAssetRepositoryImpl;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor1;
import personal.calebcordell.coinnection.domain.repository.WatchlistAssetRepository;


public class FetchAllWatchlistAssetsInteractor extends CompletableInteractor1 {
    private static final String TAG = FetchAllWatchlistAssetsInteractor.class.getSimpleName();

    private final WatchlistAssetRepository mWatchlistAssetRepository;

    public FetchAllWatchlistAssetsInteractor() {
        mWatchlistAssetRepository = new WatchlistAssetRepositoryImpl();
    }

    protected Completable buildCompletable() {
        return mWatchlistAssetRepository.fetchAllWatchlistAssets()
                .observeOn(AndroidSchedulers.mainThread());
    }
}