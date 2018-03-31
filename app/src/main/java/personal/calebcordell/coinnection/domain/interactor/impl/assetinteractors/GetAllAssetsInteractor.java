package personal.calebcordell.coinnection.domain.interactor.impl.assetinteractors;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.FlowableInteractor1;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.repository.AssetRepository;


public class GetAllAssetsInteractor extends FlowableInteractor1<List<Asset>> {
    private static final String TAG = GetAllAssetsInteractor.class.getSimpleName();

    private final AssetRepository mAssetRepository;

    @Inject
    public GetAllAssetsInteractor(AssetRepository assetRepository) {
        mAssetRepository = assetRepository;
    }

    protected Flowable<List<Asset>> buildFlowable() {
        return mAssetRepository.getAllAssets()
                .observeOn(AndroidSchedulers.mainThread());
    }
}