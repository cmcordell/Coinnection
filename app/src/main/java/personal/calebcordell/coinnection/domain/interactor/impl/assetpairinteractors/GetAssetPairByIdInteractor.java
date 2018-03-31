package personal.calebcordell.coinnection.domain.interactor.impl.assetpairinteractors;

import android.support.annotation.NonNull;
import android.util.Pair;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.FlowableInteractor;
import personal.calebcordell.coinnection.domain.model.AssetPair;
import personal.calebcordell.coinnection.domain.repository.AssetPairRepository;


public class GetAssetPairByIdInteractor extends FlowableInteractor<Pair<String, String>, AssetPair> {
    private static final String TAG = GetAssetPairByIdInteractor.class.getSimpleName();

    private final AssetPairRepository mAssetPairRepository;

    @Inject
    public GetAssetPairByIdInteractor(AssetPairRepository assetPairRepository) {
        mAssetPairRepository = assetPairRepository;
    }

    protected Flowable<AssetPair> buildFlowable(@NonNull final Pair<String, String> arguments) {
        return mAssetPairRepository.getAssetPair(arguments.first, arguments.second)
                .observeOn(AndroidSchedulers.mainThread());
    }
}