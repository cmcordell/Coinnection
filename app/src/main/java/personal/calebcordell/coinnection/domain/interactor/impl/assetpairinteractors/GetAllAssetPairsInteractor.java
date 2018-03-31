package personal.calebcordell.coinnection.domain.interactor.impl.assetpairinteractors;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.FlowableInteractor1;
import personal.calebcordell.coinnection.domain.model.AssetPair;
import personal.calebcordell.coinnection.domain.repository.AssetPairRepository;


public class GetAllAssetPairsInteractor extends FlowableInteractor1<List<AssetPair>> {
    private static final String TAG = GetAllAssetPairsInteractor.class.getSimpleName();

    private final AssetPairRepository mAssetPairRepository;

    @Inject
    public GetAllAssetPairsInteractor(AssetPairRepository assetPairRepository) {
        mAssetPairRepository = assetPairRepository;
    }

    protected Flowable<List<AssetPair>> buildFlowable() {
        return mAssetPairRepository.getAllAssetPairs()
                .observeOn(AndroidSchedulers.mainThread());
    }
}