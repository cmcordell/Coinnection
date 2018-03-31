package personal.calebcordell.coinnection.domain.interactor.impl.watchlistassetinteractors;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor;
import personal.calebcordell.coinnection.domain.repository.WatchlistAssetRepository;


public class RemoveAssetsFromWatchlistInteractor extends CompletableInteractor<List<String>> {
    private static final String TAG = AddAssetToWatchlistInteractor.class.getSimpleName();

    private final WatchlistAssetRepository mWatchlistAssetRepository;

    @Inject
    public RemoveAssetsFromWatchlistInteractor(WatchlistAssetRepository watchlistAssetRepository) {
        mWatchlistAssetRepository = watchlistAssetRepository;
    }

    protected Completable buildCompletable(@NonNull final List<String> assetIds) {
        return mWatchlistAssetRepository.removeAssetsFromWatchlist(assetIds)
                .observeOn(AndroidSchedulers.mainThread());
    }
}