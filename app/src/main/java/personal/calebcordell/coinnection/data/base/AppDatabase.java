package personal.calebcordell.coinnection.data.base;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import personal.calebcordell.coinnection.data.assetdata.AssetDao;
import personal.calebcordell.coinnection.data.globalmarketdata.GlobalMarketDataDao;
import personal.calebcordell.coinnection.data.portfolioassetdata.PortfolioAssetDao;
import personal.calebcordell.coinnection.data.portfolioassetdata.PortfolioAssetEntity;
import personal.calebcordell.coinnection.data.portfoliodata.PortfolioDao;
import personal.calebcordell.coinnection.data.watchlistassetdata.WatchlistAssetDao;
import personal.calebcordell.coinnection.data.watchlistassetdata.WatchlistAssetEntity;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.GlobalMarketData;
import personal.calebcordell.coinnection.domain.model.Portfolio;


@Database(entities = {Asset.class, Portfolio.class, PortfolioAssetEntity.class,
        WatchlistAssetEntity.class, GlobalMarketData.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "coinnection_db").build();
        }
        return INSTANCE;
    }

    public abstract AssetDao assetDao();
    public abstract PortfolioDao portfolioDao();
    public abstract PortfolioAssetDao portfolioAssetDao();
    public abstract WatchlistAssetDao watchlistAssetDao();
    public abstract GlobalMarketDataDao globalMarketDataDao();
}
