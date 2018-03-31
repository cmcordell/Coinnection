package personal.calebcordell.coinnection.dagger.module;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import personal.calebcordell.coinnection.data.assetdata.AssetDao;
import personal.calebcordell.coinnection.data.assetpairdata.AssetPairDao;
import personal.calebcordell.coinnection.data.widgetdata.AssetPairWidgetDao;
import personal.calebcordell.coinnection.data.base.AppDatabase;
import personal.calebcordell.coinnection.data.globalmarketdata.GlobalMarketDataDao;
import personal.calebcordell.coinnection.data.portfolioassetdata.PortfolioAssetDao;
import personal.calebcordell.coinnection.data.portfoliodata.PortfolioDao;
import personal.calebcordell.coinnection.data.watchlistassetdata.WatchlistAssetDao;


@Module
public class DiskModule {
    private static final String DATABASE_NAME = "coinnection_db";

    @Provides
    @Singleton
    AppDatabase provideAppDatabase(@Named(AppModule.APP_CONTEXT) Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
    }

    @Provides
    @Singleton
    AssetDao provideAssetDao(AppDatabase appDatabase) {
        return appDatabase.assetDao();
    }

    @Provides
    @Singleton
    PortfolioDao providePortfolioDao(AppDatabase appDatabase) {
        return appDatabase.portfolioDao();
    }

    @Provides
    @Singleton
    PortfolioAssetDao providePortfolioAssetDao(AppDatabase appDatabase) {
        return appDatabase.portfolioAssetDao();
    }

    @Provides
    @Singleton
    WatchlistAssetDao provideWatchlistAssetDao(AppDatabase appDatabase) {
        return appDatabase.watchlistAssetDao();
    }

    @Provides
    @Singleton
    GlobalMarketDataDao provideGlobalMarketDataDao(AppDatabase appDatabase) {
        return appDatabase.globalMarketDataDao();
    }

    @Provides
    @Singleton
    AssetPairDao provideAssetPairDao(AppDatabase appDatabase) {
        return appDatabase.assetPairDao();
    }

    @Provides
    @Singleton
    AssetPairWidgetDao provideAssetPairWidgetDao(AppDatabase appDatabase) {
        return appDatabase.assetPairWidgetDao();
    }
}