package personal.calebcordell.coinnection.domain.interactor.impl;

import java.util.List;

import io.reactivex.Completable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.data.watchlistassetdata.WatchlistAssetRepositoryImpl;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;
import personal.calebcordell.coinnection.domain.repository.WatchlistAssetRepository;


public class ReorderWatchlistAssetsInteractor extends CompletableInteractor<List<WatchlistAsset>> {
    private static final String TAG = ReorderWatchlistAssetsInteractor.class.getSimpleName();

    private final WatchlistAssetRepository mWatchlistAssetRepository;

    public ReorderWatchlistAssetsInteractor() {
        mWatchlistAssetRepository = new WatchlistAssetRepositoryImpl();
    }

    protected Completable buildCompletable(final List<WatchlistAsset> watchlistAssets) {
        return mWatchlistAssetRepository.reorderWatchlist(watchlistAssets)
                .observeOn(AndroidSchedulers.mainThread());
    }
}