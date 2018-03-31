package personal.calebcordell.coinnection.presentation.views.assetpairsetup;

import dagger.Binds;
import dagger.Module;
import personal.calebcordell.coinnection.dagger.PerFragment;
import personal.calebcordell.coinnection.dagger.module.BaseFragmentModule;


@Module(includes = BaseFragmentModule.class)
public abstract class AssetPairSetupModule {
    @Binds
    @PerFragment
    abstract AssetPairSetupContract.Presenter
    provideAssetPairSetupPresenter(AssetPairSetupPresenter assetPairSetupPresenter);
}