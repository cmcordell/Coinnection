package personal.calebcordell.coinnection.data.assetdata;

import personal.calebcordell.coinnection.domain.model.Asset;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AssetService {

    String BASE_URL = "https://api.coinmarketcap.com/v1/";
    int CACHE_SIZE = 1 * 1024 * 1024; //1MB

    @GET("ticker/")
    Single<List<AssetEntity>> assets(@Query("convert") String currencyCode, @Query("limit") int limit);

    @GET("ticker/{id}/")
    Single<List<AssetEntity>> asset(@Path("id") String id, @Query("convert") String currencyCode);

    @GET("ticker/{id}/")
    Call<List<AssetEntity>> getAsset(@Path("id") String id, @Query("convert") String currencyCode);
}
