package personal.calebcordell.coinnection.domain.interactor.impl;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.data.watchlistassetdata.WatchlistAssetRepositoryImpl;
import personal.calebcordell.coinnection.domain.interactor.base.SingleInteractor;
import personal.calebcordell.coinnection.domain.repository.WatchlistAssetRepository;


public class CheckAssetOnWatchlistInteractor extends SingleInteractor<String, Boolean> {
    private static final String TAG = CheckAssetOnWatchlistInteractor.class.getSimpleName();

    private WatchlistAssetRepository mWatchlistAssetRepository;

    public CheckAssetOnWatchlistInteractor() {
        mWatchlistAssetRepository = new WatchlistAssetRepositoryImpl();
    }

    protected Single<Boolean> buildSingle(String id) {
        return mWatchlistAssetRepository.isAssetOnWatchlist(id)
                .observeOn(AndroidSchedulers.mainThread());
    }
}