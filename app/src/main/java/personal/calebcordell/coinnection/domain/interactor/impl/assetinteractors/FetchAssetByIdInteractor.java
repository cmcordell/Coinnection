package personal.calebcordell.coinnection.domain.interactor.impl.assetinteractors;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor;
import personal.calebcordell.coinnection.domain.repository.AssetRepository;


public class FetchAssetByIdInteractor extends CompletableInteractor<String> {
    private static final String TAG = FetchAllAssetsInteractor.class.getSimpleName();

    private final AssetRepository mAssetRepository;

    @Inject
    public FetchAssetByIdInteractor(AssetRepository assetRepository) {
        mAssetRepository = assetRepository;
    }

    protected Completable buildCompletable(@NonNull final String id) {
        return mAssetRepository.fetchAsset(id)
                .observeOn(AndroidSchedulers.mainThread());
    }
}