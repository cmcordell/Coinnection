package personal.calebcordell.coinnection.data.assetpairdata;

import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.data.assetdata.AssetNetworkDataStore;
import personal.calebcordell.coinnection.domain.model.AssetPair;
import personal.calebcordell.coinnection.domain.repository.AssetPairRepository;


@Singleton
public class AssetPairRepositoryImpl implements AssetPairRepository {
    private String TAG = AssetPairRepositoryImpl.class.getSimpleName();

    private final AssetPairDiskDataStore mAssetPairDiskDataStore;
    private final AssetNetworkDataStore mAssetNetworkDataStore;
    private final AssetPairMapper mAssetPairMapper;

    @Inject
    public AssetPairRepositoryImpl(AssetPairDiskDataStore assetPairDiskDataStore,
                                   AssetNetworkDataStore assetNetworkDataStore,
                                   AssetPairMapper assetPairMapper) {
        mAssetPairDiskDataStore = assetPairDiskDataStore;
        mAssetNetworkDataStore = assetNetworkDataStore;
        mAssetPairMapper = assetPairMapper;
    }

    public Completable updateAssetPairs(@NonNull final List<AssetPair> assetPairs) {
        return mAssetPairDiskDataStore.replaceAll(assetPairs);
    }

    public Completable addAssetPair(@NonNull final AssetPair assetPair) {
        return mAssetPairDiskDataStore.storeSingular(assetPair);
    }

    public Flowable<AssetPair> getAssetPair(@NonNull final String baseAssetId, @NonNull final String quoteCurrencySymbol) {
        return mAssetPairDiskDataStore.getSingular(baseAssetId, quoteCurrencySymbol);
    }

    public Flowable<List<AssetPair>> getAllAssetPairs() {
        return mAssetPairDiskDataStore.getAll();
    }


    public Completable removeAssetPair(@NonNull final String baseAssetId, final @NonNull String quoteCurrencySymbol) {
        return mAssetPairDiskDataStore.removeSingular(baseAssetId, quoteCurrencySymbol);
    }

    public Completable removeAssetPairs(@NonNull final List<Pair<String, String>> arguments) {
        return mAssetPairDiskDataStore.removeMultiple(arguments);
    }

    public Completable fetchAllAssetPairs() {
        return mAssetPairDiskDataStore.getAllIds()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .flatMap(mAssetNetworkDataStore::assetPairs)
                .map(mAssetPairMapper::mapUp)
                .flatMapCompletable(mAssetPairDiskDataStore::updateAll);
    }

    public Completable fetchAssetPair(@NonNull final String baseAssetId, @NonNull final String quoteCurrencySymbol) {
        return mAssetNetworkDataStore.assetPair(new Pair<>(baseAssetId, quoteCurrencySymbol))
                .subscribeOn(Schedulers.io())
                .map(mAssetPairMapper::mapUp)
                .flatMapCompletable(mAssetPairDiskDataStore::updateSingular);
    }
}