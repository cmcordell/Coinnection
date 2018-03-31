package personal.calebcordell.coinnection.presentation.views.assetpairsetup;

import android.support.annotation.NonNull;

import java.util.List;

import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.AssetPair;
import personal.calebcordell.coinnection.domain.model.Currency;
import personal.calebcordell.coinnection.presentation.views.base.BasePresenter;


public interface AssetPairSetupContract {
    interface View {
        void setAssets(@NonNull List<Asset> assets);

        void setCurrencies(@NonNull List<Currency> currencies);

        void setCurrenciesVisible(boolean visible);

        void assetPairLoaded(@NonNull AssetPair assetPair);

        void showMessage(@NonNull String message);
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void setIsWidgetSetup();

        abstract void onAssetPairSelected(@NonNull AssetPair assetPair);
    }
}
