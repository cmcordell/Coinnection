package personal.calebcordell.coinnection.data.assetdata;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

@Singleton
public interface AssetService {
    @GET("ticker/")
    Single<List<AssetEntity>> assets(@Query("convert") String currencyCode, @Query("limit") int limit);

    @GET("ticker/{id}/")
    Single<List<AssetEntity>> asset(@Path("id") String id, @Query("convert") String currencyCode);
}