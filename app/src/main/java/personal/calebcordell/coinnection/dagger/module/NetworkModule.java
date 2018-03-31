package personal.calebcordell.coinnection.dagger.module;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import personal.calebcordell.coinnection.data.assetdata.AssetEntity;
import personal.calebcordell.coinnection.data.assetdata.AssetJsonDeserializer;
import personal.calebcordell.coinnection.data.assetdata.AssetService;
import personal.calebcordell.coinnection.data.globalmarketdata.GlobalMarketDataService;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public class NetworkModule {
    private static final String BASE_URL = "https://api.coinmarketcap.com/v1/";
    private static final int OKHTTP_CACHE_SIZE = 2 * 1024 * 1024; //2 MiB

    @Provides
    @Singleton
    Cache provideOkHttpCache(@Named(AppModule.APP_CONTEXT) Context context) {
        return new Cache(context.getCacheDir(), OKHTTP_CACHE_SIZE);
    }

    @Provides
    @Singleton
    Gson providesGson(AssetJsonDeserializer deserializer) {
        return new GsonBuilder()
                .registerTypeAdapter(AssetEntity.class, deserializer)
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache) {
        return new OkHttpClient.Builder()
                .cache(cache)
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    AssetService provideAssetService(Retrofit retrofit) {
        return retrofit.create(AssetService.class);
    }

    @Provides
    @Singleton
    GlobalMarketDataService provideGlobalMarketDataService(Retrofit retrofit) {
        return retrofit.create(GlobalMarketDataService.class);
    }
}
