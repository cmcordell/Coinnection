package personal.calebcordell.coinnection.domain.interactor.impl;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.data.assetdata.AssetRepositoryImpl;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor;
import personal.calebcordell.coinnection.domain.repository.AssetRepository;


public class FetchAllAssetsInteractor extends CompletableInteractor<Boolean> {
    private static final String TAG = FetchAllAssetsInteractor.class.getSimpleName();

    private final AssetRepository mAssetRepository;

    public FetchAllAssetsInteractor() {
        mAssetRepository = new AssetRepositoryImpl();
    }

    protected Completable buildCompletable(final Boolean forceUpdate) {
        return mAssetRepository.fetchAllAssets(forceUpdate)
                .observeOn(AndroidSchedulers.mainThread());
    }
}