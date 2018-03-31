package personal.calebcordell.coinnection.domain.repository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import personal.calebcordell.coinnection.domain.model.Asset;


public interface AssetRepository {

    Flowable<Asset> getAsset(String id);

    Flowable<List<Asset>> getAllAssets();

    Completable fetchAllAssets(boolean forceFetch);

    Completable fetchAsset(String id);
}