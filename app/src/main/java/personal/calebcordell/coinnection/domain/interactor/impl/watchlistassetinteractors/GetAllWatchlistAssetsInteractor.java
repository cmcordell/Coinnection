package personal.calebcordell.coinnection.domain.interactor.impl.watchlistassetinteractors;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.FlowableInteractor1;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;
import personal.calebcordell.coinnection.domain.repository.WatchlistAssetRepository;


public class GetAllWatchlistAssetsInteractor extends FlowableInteractor1<List<WatchlistAsset>> {
    private static final String TAG = GetAllWatchlistAssetsInteractor.class.getSimpleName();

    private final WatchlistAssetRepository mWatchlistAssetRepository;

    @Inject
    public GetAllWatchlistAssetsInteractor(WatchlistAssetRepository watchlistAssetRepository) {
        mWatchlistAssetRepository = watchlistAssetRepository;
    }

    protected Flowable<List<WatchlistAsset>> buildFlowable() {
        return mWatchlistAssetRepository.getAllWatchlistAssets()
                .observeOn(AndroidSchedulers.mainThread());
    }
}