package personal.calebcordell.coinnection.domain.interactor.impl.watchlistassetinteractors;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;
import personal.calebcordell.coinnection.domain.repository.WatchlistAssetRepository;


public class ReorderWatchlistAssetsInteractor extends CompletableInteractor<List<WatchlistAsset>> {
    private static final String TAG = ReorderWatchlistAssetsInteractor.class.getSimpleName();

    private final WatchlistAssetRepository mWatchlistAssetRepository;

    @Inject
    public ReorderWatchlistAssetsInteractor(WatchlistAssetRepository watchlistAssetRepository) {
        mWatchlistAssetRepository = watchlistAssetRepository;
    }

    protected Completable buildCompletable(@NonNull final List<WatchlistAsset> watchlistAssets) {
        return mWatchlistAssetRepository.reorderWatchlist(watchlistAssets)
                .observeOn(AndroidSchedulers.mainThread());
    }
}