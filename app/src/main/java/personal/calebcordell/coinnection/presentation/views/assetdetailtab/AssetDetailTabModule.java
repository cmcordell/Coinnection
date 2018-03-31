package personal.calebcordell.coinnection.presentation.views.assetdetailtab;

import dagger.Binds;
import dagger.Module;
import personal.calebcordell.coinnection.dagger.PerChildFragment;
import personal.calebcordell.coinnection.dagger.module.BaseChildFragmentModule;


@Module(includes = BaseChildFragmentModule.class)
public abstract class AssetDetailTabModule {

    @Binds
    @PerChildFragment
    abstract AssetDetailTabContract.Presenter
    bindAssetDetailTabPresenter(AssetDetailTabPresenter assetDetailTabPresenter);
}