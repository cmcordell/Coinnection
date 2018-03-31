package personal.calebcordell.coinnection.dagger.module;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import personal.calebcordell.coinnection.data.PreferencesRepositoryImpl;
import personal.calebcordell.coinnection.data.assetdata.AssetRepositoryImpl;
import personal.calebcordell.coinnection.data.assetpairdata.AssetPairRepositoryImpl;
import personal.calebcordell.coinnection.data.widgetdata.AssetPairWidgetRepositoryImpl;
import personal.calebcordell.coinnection.data.globalmarketdata.GlobalMarketDataRepositoryImpl;
import personal.calebcordell.coinnection.data.portfolioassetdata.PortfolioAssetRepositoryImpl;
import personal.calebcordell.coinnection.data.portfoliodata.PortfolioRepositoryImpl;
import personal.calebcordell.coinnection.data.watchlistassetdata.WatchlistAssetRepositoryImpl;
import personal.calebcordell.coinnection.domain.repository.AssetPairRepository;
import personal.calebcordell.coinnection.domain.repository.AssetPairWidgetRepository;
import personal.calebcordell.coinnection.domain.repository.AssetRepository;
import personal.calebcordell.coinnection.domain.repository.GlobalMarketDataRepository;
import personal.calebcordell.coinnection.domain.repository.PortfolioAssetRepository;
import personal.calebcordell.coinnection.domain.repository.PortfolioRepository;
import personal.calebcordell.coinnection.domain.repository.PreferencesRepository;
import personal.calebcordell.coinnection.domain.repository.WatchlistAssetRepository;


@Module
public abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract AssetRepository
    provideAssetRepository(AssetRepositoryImpl assetRepository);

    @Binds
    @Singleton
    abstract PortfolioRepository
    providePortfolioRepository(PortfolioRepositoryImpl portfolioRepository);

    @Binds
    @Singleton
    abstract PortfolioAssetRepository
    providePortfolioAssetRepository(PortfolioAssetRepositoryImpl portfolioAssetRepository);

    @Binds
    @Singleton
    abstract WatchlistAssetRepository
    provideWatchlistAssetRepository(WatchlistAssetRepositoryImpl watchlistAssetRepository);

    @Binds
    @Singleton
    abstract AssetPairRepository
    provideAssetPairRepository(AssetPairRepositoryImpl assetPairRepository);

    @Binds
    @Singleton
    abstract GlobalMarketDataRepository
    provideGlobalMarketDataRepository(GlobalMarketDataRepositoryImpl globalMarketDataRepository);

    @Binds
    @Singleton
    abstract AssetPairWidgetRepository
    provideAssetPairWidgetRepository(AssetPairWidgetRepositoryImpl assetPairWidgetRepository);

    @Binds
    @Singleton
    abstract PreferencesRepository
    providePreferencesRepository(PreferencesRepositoryImpl preferencesRepository);
}