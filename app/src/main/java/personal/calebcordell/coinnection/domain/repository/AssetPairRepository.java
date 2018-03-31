package personal.calebcordell.coinnection.domain.repository;

import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import personal.calebcordell.coinnection.domain.model.AssetPair;


public interface AssetPairRepository {
    Completable updateAssetPairs(@NonNull List<AssetPair> assetPairs);

    Completable addAssetPair(@NonNull AssetPair assetPair);

    Flowable<AssetPair> getAssetPair(@NonNull String baseAssetId, @NonNull String quoteCurrencySymbol);

    Flowable<List<AssetPair>> getAllAssetPairs();

    Completable fetchAllAssetPairs();

    Completable fetchAssetPair(@NonNull String baseAssetId, @NonNull String quoteCurrencySymbol);

    Completable removeAssetPair(@NonNull String baseAssetId, @NonNull String quoteCurrencySymbol);

    Completable removeAssetPairs(@NonNull List<Pair<String, String>> arguments);
}