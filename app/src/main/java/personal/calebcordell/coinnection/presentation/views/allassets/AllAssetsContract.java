package personal.calebcordell.coinnection.presentation.views.allassets;

import java.util.List;

import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.GlobalMarketData;
import personal.calebcordell.coinnection.presentation.views.base.BasePresenter;


/**
 * Defines the relationship between AllAssets Presenter and AllAssets View
 */
public interface AllAssetsContract {
    interface View {
        void showAssets(List<Asset> assets);

        void showGlobalMarketData(GlobalMarketData globalMarketData);

        void openAssetDetailViewUI(Asset asset);
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void pause();

        abstract void resume();
    }
}