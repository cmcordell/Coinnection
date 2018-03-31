package personal.calebcordell.coinnection.data.base;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import javax.inject.Singleton;

import personal.calebcordell.coinnection.data.assetdata.AssetDao;
import personal.calebcordell.coinnection.data.assetpairdata.AssetPairDao;
import personal.calebcordell.coinnection.data.assetpairdata.AssetPairEntity;
import personal.calebcordell.coinnection.data.widgetdata.AssetPairWidgetDao;
import personal.calebcordell.coinnection.data.widgetdata.AssetPairWidgetEntity;
import personal.calebcordell.coinnection.data.globalmarketdata.GlobalMarketDataDao;
import personal.calebcordell.coinnection.data.portfolioassetdata.PortfolioAssetDao;
import personal.calebcordell.coinnection.data.portfolioassetdata.PortfolioAssetEntity;
import personal.calebcordell.coinnection.data.portfoliodata.PortfolioDao;
import personal.calebcordell.coinnection.data.watchlistassetdata.WatchlistAssetDao;
import personal.calebcordell.coinnection.data.watchlistassetdata.WatchlistAssetEntity;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.GlobalMarketData;
import personal.calebcordell.coinnection.domain.model.Portfolio;


@Singleton
@Database(entities = {Asset.class, Portfolio.class, PortfolioAssetEntity.class,
        WatchlistAssetEntity.class, GlobalMarketData.class, AssetPairEntity.class,
        AssetPairWidgetEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AssetDao assetDao();

    public abstract PortfolioDao portfolioDao();

    public abstract PortfolioAssetDao portfolioAssetDao();

    public abstract WatchlistAssetDao watchlistAssetDao();

    public abstract GlobalMarketDataDao globalMarketDataDao();

    public abstract AssetPairDao assetPairDao();

    public abstract AssetPairWidgetDao assetPairWidgetDao();
}