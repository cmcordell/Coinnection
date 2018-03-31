package personal.calebcordell.coinnection.domain.interactor.impl.watchlistassetinteractors;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.FlowableInteractor;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;
import personal.calebcordell.coinnection.domain.repository.WatchlistAssetRepository;


public class GetWatchlistAssetByIdInteractor extends FlowableInteractor<String, WatchlistAsset> {
    private static final String TAG = GetWatchlistAssetByIdInteractor.class.getSimpleName();

    private final WatchlistAssetRepository mWatchlistAssetRepository;

    @Inject
    public GetWatchlistAssetByIdInteractor(WatchlistAssetRepository watchlistAssetRepository) {
        mWatchlistAssetRepository = watchlistAssetRepository;
    }

    protected Flowable<WatchlistAsset> buildFlowable(@NonNull final String id) {
        return mWatchlistAssetRepository.getWatchlistAsset(id)
                .observeOn(AndroidSchedulers.mainThread());
    }
}