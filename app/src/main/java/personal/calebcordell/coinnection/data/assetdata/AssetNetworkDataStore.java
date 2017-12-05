package personal.calebcordell.coinnection.data.assetdata;

import android.support.annotation.NonNull;
import android.util.Log;

import io.reactivex.Flowable;
import personal.calebcordell.coinnection.presentation.App;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Uses Retrofit to get data from CoinMarketCap.com
 */
public class AssetNetworkDataStore {
    private static final String TAG = AssetNetworkDataStore.class.getSimpleName();
    private static final String ASSET_BASE_URL = "https://api.coinmarketcap.com/v1/";

    private static final int CACHE_SIZE = 1 * 1024 * 1024; //1MB

    private static AssetNetworkDataStore INSTANCE;

    //TODO Remove
    private int networkCalls = 0;
    private long startTime = System.currentTimeMillis();
    //--------------------

    private AssetNetworkDataStore() {}

    public static AssetNetworkDataStore getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new AssetNetworkDataStore();
        }
        return INSTANCE;
    }

    private AssetService getAssetService(final int timeout, @NonNull final TimeUnit timeUnit) {
        final Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(timeout, timeUnit)
                .readTimeout(timeout, timeUnit)
                .cache(new Cache(App.getAppContext().getCacheDir(), CACHE_SIZE))
                .build();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ASSET_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        return retrofit.create(AssetService.class);
    }

    public Single<List<AssetEntity>> assets() {
        networkCalls++;
        double upTime = (System.currentTimeMillis() - startTime) / 60000.0;
        Log.d(TAG, "Network Calls: " + networkCalls + ";  Up Time: " + String.format("%.2f", upTime) + "min");
        return getAssetService(120, TimeUnit.SECONDS)
                .assets(App.getApp().getCurrencyCode(), 10000)
                .subscribeOn(Schedulers.io());
    }

    public Single<List<AssetEntity>> assets(@NonNull final List<String> ids) {
        return Flowable.fromIterable(ids)
                .subscribeOn(Schedulers.io())
                .flatMapSingle(this::asset)
                .toList();
    }

    public Single<AssetEntity> asset(@NonNull final String id) {
        networkCalls++;
        double upTime = (System.currentTimeMillis() - startTime) / 60000.0;
        Log.d(TAG, "Asset: " + id + ";  Network Calls: " + networkCalls + ";  Up Time: " + String.format("%.2f", upTime) + "min");
        return getAssetService(20, TimeUnit.SECONDS).asset(id, App.getApp().getCurrencyCode())
                .subscribeOn(Schedulers.io())
                .flatMap(assetEntities -> Single.just(assetEntities.get(0)));
    }
}