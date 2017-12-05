package personal.calebcordell.coinnection.domain.interactor.impl;

import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.data.assetdata.AssetRepositoryImpl;
import personal.calebcordell.coinnection.domain.interactor.base.FlowableInteractor;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.repository.AssetRepository;

import io.reactivex.Flowable;


public class GetAssetByIdInteractor extends FlowableInteractor<String, Asset> {
    private static final String TAG = GetAssetByIdInteractor.class.getSimpleName();

    private AssetRepository mAssetRepository;

    public GetAssetByIdInteractor() {
        mAssetRepository = new AssetRepositoryImpl();
    }

    protected Flowable<Asset> buildFlowable(String id) {
        return mAssetRepository.getAsset(id)
                .observeOn(AndroidSchedulers.mainThread());
    }
}