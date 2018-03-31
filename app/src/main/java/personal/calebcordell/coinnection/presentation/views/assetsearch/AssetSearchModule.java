package personal.calebcordell.coinnection.presentation.views.assetsearch;

import dagger.Binds;
import dagger.Module;
import personal.calebcordell.coinnection.dagger.PerFragment;
import personal.calebcordell.coinnection.dagger.module.BaseFragmentModule;


@Module(includes = BaseFragmentModule.class)
public abstract class AssetSearchModule {
    @Binds
    @PerFragment
    abstract AssetSearchContract.Presenter
    provideAssetSearchPresenter(AssetSearchPresenter assetSearchPresenter);
}