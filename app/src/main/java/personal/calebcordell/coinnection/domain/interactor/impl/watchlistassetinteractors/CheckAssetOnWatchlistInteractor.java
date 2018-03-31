package personal.calebcordell.coinnection.domain.interactor.impl.watchlistassetinteractors;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.SingleInteractor;
import personal.calebcordell.coinnection.domain.repository.WatchlistAssetRepository;


public class CheckAssetOnWatchlistInteractor extends SingleInteractor<String, Boolean> {
    private static final String TAG = CheckAssetOnWatchlistInteractor.class.getSimpleName();

    private final WatchlistAssetRepository mWatchlistAssetRepository;

    @Inject
    public CheckAssetOnWatchlistInteractor(WatchlistAssetRepository watchlistAssetRepository) {
        mWatchlistAssetRepository = watchlistAssetRepository;
    }

    protected Single<Boolean> buildSingle(@NonNull final String id) {
        return mWatchlistAssetRepository.isAssetOnWatchlist(id)
                .observeOn(AndroidSchedulers.mainThread());
    }
}