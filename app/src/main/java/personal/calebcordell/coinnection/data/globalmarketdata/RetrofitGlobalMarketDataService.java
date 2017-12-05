package personal.calebcordell.coinnection.data.globalmarketdata;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface RetrofitGlobalMarketDataService {

    String BASE_URL = "https://api.coinmarketcap.com/v1/";

    @GET("global/")
    Single<GlobalMarketDataEntity> globalMarketData(@Query("convert") String currencyCode);
}
