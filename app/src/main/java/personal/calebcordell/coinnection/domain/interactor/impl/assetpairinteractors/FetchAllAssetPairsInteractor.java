package personal.calebcordell.coinnection.domain.interactor.impl.assetpairinteractors;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor1;
import personal.calebcordell.coinnection.domain.repository.AssetPairRepository;


public class FetchAllAssetPairsInteractor extends CompletableInteractor1 {
    private static final String TAG = FetchAllAssetPairsInteractor.class.getSimpleName();

    private final AssetPairRepository mAssetPairRepository;

    @Inject
    public FetchAllAssetPairsInteractor(AssetPairRepository assetPairRepository) {
        mAssetPairRepository = assetPairRepository;
    }

    protected Completable buildCompletable() {
        return mAssetPairRepository.fetchAllAssetPairs()
                .observeOn(AndroidSchedulers.mainThread());
    }
}