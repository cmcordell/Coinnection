package personal.calebcordell.coinnection.domain.interactor.impl.assetpairinteractors;

import android.support.annotation.NonNull;
import android.util.Pair;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor;
import personal.calebcordell.coinnection.domain.repository.AssetPairRepository;


public class FetchAssetPairInteractor extends CompletableInteractor<Pair<String, String>> {
    private static final String TAG = FetchAssetPairInteractor.class.getSimpleName();

    private final AssetPairRepository mAssetPairRepository;

    @Inject
    public FetchAssetPairInteractor(AssetPairRepository assetPairRepository) {
        mAssetPairRepository = assetPairRepository;
    }

    protected Completable buildCompletable(@NonNull final Pair<String, String> arguments) {
        return mAssetPairRepository.fetchAssetPair(arguments.first, arguments.second)
                .observeOn(AndroidSchedulers.mainThread());
    }
}