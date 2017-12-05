package personal.calebcordell.coinnection.domain.interactor.impl;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.data.assetdata.AssetRepositoryImpl;
import personal.calebcordell.coinnection.domain.interactor.base.FlowableInteractor1;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.repository.AssetRepository;

import java.util.List;


public class GetAllAssetsInteractor extends FlowableInteractor1<List<Asset>> {
    private static final String TAG = GetAllAssetsInteractor.class.getSimpleName();

    private AssetRepository mAssetRepository;

    public GetAllAssetsInteractor() {
        mAssetRepository = new AssetRepositoryImpl();
    }

    protected Flowable<List<Asset>> buildFlowable() {
        return mAssetRepository.getAllAssets()
                .observeOn(AndroidSchedulers.mainThread());
    }
}