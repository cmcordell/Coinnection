package personal.calebcordell.coinnection.data.globalmarketdata;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import io.reactivex.Flowable;
import personal.calebcordell.coinnection.domain.model.GlobalMarketData;


@Dao
public interface GlobalMarketDataDao {
    @Query("SELECT * FROM global_market_data")
    Flowable<GlobalMarketData> get();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GlobalMarketData globalMarketData);
}