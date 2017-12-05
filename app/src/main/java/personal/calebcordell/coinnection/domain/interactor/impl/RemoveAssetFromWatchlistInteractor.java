package personal.calebcordell.coinnection.domain.interactor.impl;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.data.watchlistassetdata.WatchlistAssetRepositoryImpl;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;
import personal.calebcordell.coinnection.domain.repository.WatchlistAssetRepository;


public class RemoveAssetFromWatchlistInteractor extends CompletableInteractor<String> {
    private static final String TAG = AddAssetToWatchlistInteractor.class.getSimpleName();

    private final WatchlistAssetRepository mWatchlistAssetRepository;

    public RemoveAssetFromWatchlistInteractor() {
        mWatchlistAssetRepository = new WatchlistAssetRepositoryImpl();
    }

    protected Completable buildCompletable(final String assetId) {
        return mWatchlistAssetRepository.removeAssetFromWatchlist(assetId)
                .observeOn(AndroidSchedulers.mainThread());
    }
}