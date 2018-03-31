package personal.calebcordell.coinnection.data.widgetdata;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;


@Dao
public interface AssetPairWidgetDao {
    @Query("SELECT * FROM asset_pair_widgets WHERE id = :id LIMIT 1")
    Single<AssetPairWidgetEntity> get(int id);
    @Query("SELECT * FROM asset_pair_widgets WHERE asset_id = :assetId AND quote_currency_symbol = :quoteCurrencySymbol")
    List<AssetPairWidgetEntity> get(String assetId, String quoteCurrencySymbol);
    @Query("SELECT * FROM asset_pair_widgets")
    Single<List<AssetPairWidgetEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AssetPairWidgetEntity assetPairWidgetEntity);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<AssetPairWidgetEntity> assetInfoWidgetEntities);

    @Query("DELETE FROM asset_pair_widgets WHERE id = :id")
    void remove(int id);
    @Query("DELETE FROM asset_pair_widgets WHERE id IN (:ids)")
    void remove(int[] ids);

    @Query("DELETE FROM asset_pair_widgets")
    void clear();
}