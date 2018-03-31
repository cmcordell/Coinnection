package personal.calebcordell.coinnection.domain.interactor.impl.watchlistassetinteractors;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor;
import personal.calebcordell.coinnection.domain.repository.WatchlistAssetRepository;


public class RemoveAssetFromWatchlistInteractor extends CompletableInteractor<String> {
    private static final String TAG = AddAssetToWatchlistInteractor.class.getSimpleName();

    private final WatchlistAssetRepository mWatchlistAssetRepository;

    @Inject
    public RemoveAssetFromWatchlistInteractor(WatchlistAssetRepository watchlistAssetRepository) {
        mWatchlistAssetRepository = watchlistAssetRepository;
    }

    protected Completable buildCompletable(@NonNull final String assetId) {
        return mWatchlistAssetRepository.removeAssetFromWatchlist(assetId)
                .observeOn(AndroidSchedulers.mainThread());
    }
}