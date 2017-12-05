package personal.calebcordell.coinnection.domain.repository;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;


public interface PortfolioAssetRepository {
    Single<List<String>> getPortfolioAssetIds();

    Completable reorderPortfolio(@NonNull List<PortfolioAsset> portfolioAssetsInOrder);

    Completable addAssetToPortfolio(@NonNull PortfolioAsset portfolioAsset);

    Completable removeAssetFromPortfolio(@NonNull String id);

    Single<Boolean> isAssetInPortfolio(@NonNull String id);

    Flowable<PortfolioAsset> getPortfolioAsset(@NonNull String id);

    Flowable<List<PortfolioAsset>> getAllPortfolioAssets();

    Completable fetchAllPortfolioAssets();

    Completable fetchPortfolioAsset(@NonNull String id);
}