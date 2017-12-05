package personal.calebcordell.coinnection.data.globalmarketdata;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.presentation.App;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class GlobalMarketNetworkDataStore {
    private static final String GLOBAL_MARKET_DATA_BASE_URL = "https://api.coinmarketcap.com/v1/";

    private static GlobalMarketNetworkDataStore INSTANCE;
    private RetrofitGlobalMarketDataService mGlobalMarketDataService;

    private GlobalMarketNetworkDataStore() {
        final Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        final Retrofit retrofit = new Retrofit.Builder().baseUrl(GLOBAL_MARKET_DATA_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mGlobalMarketDataService = retrofit.create(RetrofitGlobalMarketDataService.class);
    }

    public static GlobalMarketNetworkDataStore getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new GlobalMarketNetworkDataStore();
        }
        return INSTANCE;
    }

    public Single<GlobalMarketDataEntity> getGlobalMarketData() {
        String currencyCode = App.getApp().getCurrencyCode();

        return mGlobalMarketDataService.globalMarketData(currencyCode);
    }
}
