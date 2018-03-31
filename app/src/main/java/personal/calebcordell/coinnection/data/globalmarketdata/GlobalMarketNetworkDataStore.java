package personal.calebcordell.coinnection.data.globalmarketdata;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import personal.calebcordell.coinnection.presentation.Preferences;


@Singleton
public class GlobalMarketNetworkDataStore {
    private static final String TAG = GlobalMarketNetworkDataStore.class.getSimpleName();

    private final GlobalMarketDataService mGlobalMarketDataService;
    private final Preferences mPreferences;

    @Inject
    public GlobalMarketNetworkDataStore(GlobalMarketDataService globalMarketDataService,
                                        Preferences preferences) {
        mGlobalMarketDataService = globalMarketDataService;
        mPreferences = preferences;
    }

    public Single<GlobalMarketDataEntity> getGlobalMarketData() {
        return mGlobalMarketDataService.globalMarketData(mPreferences.getCurrencyCode());
    }
}
