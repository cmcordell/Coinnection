package personal.calebcordell.coinnection.presentation.views.assetpairdetail;

import android.support.annotation.NonNull;

import personal.calebcordell.coinnection.domain.model.AssetPair;
import personal.calebcordell.coinnection.presentation.views.base.BasePresenter;


public interface AssetPairDetailContract {
    interface View {
        void showAssetPair(@NonNull AssetPair assetPair);

        void openRemoveAssetPairUI();
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void setAssetPair(@NonNull AssetPair assetPair);

        abstract void onRemoveAssetPairClicked();

        abstract void removeAssetPair();
    }
}