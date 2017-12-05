package personal.calebcordell.coinnection.domain.interactor.impl;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.data.watchlistassetdata.WatchlistAssetRepositoryImpl;
import personal.calebcordell.coinnection.domain.interactor.base.FlowableInteractor;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;
import personal.calebcordell.coinnection.domain.repository.WatchlistAssetRepository;


public class GetWatchlistAssetByIdInteractor extends FlowableInteractor<String, WatchlistAsset> {
    private static final String TAG = GetWatchlistAssetByIdInteractor.class.getSimpleName();

    private WatchlistAssetRepository mWatchlistAssetRepository;

    public GetWatchlistAssetByIdInteractor() {
        mWatchlistAssetRepository = new WatchlistAssetRepositoryImpl();
    }

    protected Flowable<WatchlistAsset> buildFlowable(String id) {
        return mWatchlistAssetRepository.getWatchlistAsset(id)
                .observeOn(AndroidSchedulers.mainThread());
    }
}