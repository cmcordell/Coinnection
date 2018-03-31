package personal.calebcordell.coinnection.domain.repository;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;


public interface WatchlistAssetRepository {
    Single<List<String>> getWatchlistAssetIds();

    Completable reorderWatchlist(@NonNull List<WatchlistAsset> watchlistAssetsInOrder);

    Completable addAssetToWatchlist(@NonNull WatchlistAsset watchlistAsset);

    Completable removeAssetFromWatchlist(@NonNull String id);

    Completable removeAssetsFromWatchlist(@NonNull List<String> ids);

    Single<Boolean> isAssetOnWatchlist(@NonNull String id);

    Flowable<WatchlistAsset> getWatchlistAsset(@NonNull String id);

    Flowable<List<WatchlistAsset>> getAllWatchlistAssets();

    Completable fetchAllWatchlistAssets();

    Completable fetchWatchlistAsset(@NonNull String id);
}