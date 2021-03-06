package personal.calebcordell.coinnection.domain.interactor.impl.watchlistassetinteractors;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.SingleInteractor1;
import personal.calebcordell.coinnection.domain.repository.WatchlistAssetRepository;


public class GetWatchlistAssetIdsInteractor extends SingleInteractor1<List<String>> {
    private static final String TAG = GetWatchlistAssetIdsInteractor.class.getSimpleName();

    private final WatchlistAssetRepository mWatchlistAssetRepository;

    @Inject
    public GetWatchlistAssetIdsInteractor(WatchlistAssetRepository watchlistAssetRepository) {
        mWatchlistAssetRepository = watchlistAssetRepository;
    }

    public Single<List<String>> buildSingle() {
        return mWatchlistAssetRepository.getWatchlistAssetIds()
                .observeOn(AndroidSchedulers.mainThread());
    }
}