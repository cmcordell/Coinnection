package personal.calebcordell.coinnection.domain.interactor.impl.assetinteractors;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.FlowableInteractor;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.repository.AssetRepository;


public class GetAssetByIdInteractor extends FlowableInteractor<String, Asset> {
    private static final String TAG = GetAssetByIdInteractor.class.getSimpleName();

    private final AssetRepository mAssetRepository;

    @Inject
    public GetAssetByIdInteractor(AssetRepository assetRepository) {
        mAssetRepository = assetRepository;
    }

    protected Flowable<Asset> buildFlowable(@NonNull final String id) {
        return mAssetRepository.getAsset(id)
                .observeOn(AndroidSchedulers.mainThread());
    }
}