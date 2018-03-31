package personal.calebcordell.coinnection.data.globalmarketdata;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface GlobalMarketDataService {
    @GET("global/")
    Single<GlobalMarketDataEntity> globalMarketData(@Query("convert") String currencyCode);
}
