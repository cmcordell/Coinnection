package personal.calebcordell.coinnection.domain.repository;

import io.reactivex.Completable;
import personal.calebcordell.coinnection.domain.model.Asset;

import java.util.List;

import io.reactivex.Flowable;


public interface AssetRepository {

    Flowable<Asset> getAsset(String id);

    Flowable<List<Asset>> getAllAssets();

    Completable fetchAllAssets(boolean forceFetch);

    Completable fetchAsset(String id);
}