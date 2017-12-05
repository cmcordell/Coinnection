package personal.calebcordell.coinnection.data.portfoliodata;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import io.reactivex.Flowable;
import personal.calebcordell.coinnection.domain.model.Portfolio;


@Dao
public interface PortfolioDao {
    @Query("SELECT * FROM portfolios")
    Flowable<Portfolio> get();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Portfolio portfolio);
}