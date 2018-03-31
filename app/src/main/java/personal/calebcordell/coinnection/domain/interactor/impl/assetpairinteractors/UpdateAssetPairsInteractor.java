package personal.calebcordell.coinnection.domain.interactor.impl.assetpairinteractors;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor;
import personal.calebcordell.coinnection.domain.model.AssetPair;
import personal.calebcordell.coinnection.domain.repository.AssetPairRepository;


public class UpdateAssetPairsInteractor extends CompletableInteractor<List<AssetPair>> {
    private static final String TAG = UpdateAssetPairsInteractor.class.getSimpleName();

    private final AssetPairRepository mAssetPairRepository;

    @Inject
    public UpdateAssetPairsInteractor(AssetPairRepository assetPairRepository) {
        mAssetPairRepository = assetPairRepository;
    }

    protected Completable buildCompletable(@NonNull final List<AssetPair> assetPairs) {
        return mAssetPairRepository.updateAssetPairs(assetPairs)
                .observeOn(AndroidSchedulers.mainThread());
    }
}