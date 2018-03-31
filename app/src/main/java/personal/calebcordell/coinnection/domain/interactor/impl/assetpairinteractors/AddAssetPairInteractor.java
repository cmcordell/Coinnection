package personal.calebcordell.coinnection.domain.interactor.impl.assetpairinteractors;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor;
import personal.calebcordell.coinnection.domain.model.AssetPair;
import personal.calebcordell.coinnection.domain.repository.AssetPairRepository;


public class AddAssetPairInteractor extends CompletableInteractor<AssetPair> {
    private static final String TAG = AddAssetPairInteractor.class.getSimpleName();

    private final AssetPairRepository mAssetPairRepository;

    @Inject
    public AddAssetPairInteractor(AssetPairRepository assetPairRepository) {
        mAssetPairRepository = assetPairRepository;
    }

    protected Completable buildCompletable(@NonNull final AssetPair assetPair) {
        return mAssetPairRepository.addAssetPair(assetPair)
                .andThen(mAssetPairRepository.fetchAssetPair(assetPair.getId(), assetPair.getQuoteCurrencySymbol()))
                .observeOn(AndroidSchedulers.mainThread());
    }
}