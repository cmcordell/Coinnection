package personal.calebcordell.coinnection.presentation.views.assetpairlist;

import dagger.Binds;
import dagger.Module;
import personal.calebcordell.coinnection.dagger.PerFragment;
import personal.calebcordell.coinnection.dagger.module.BaseFragmentModule;


@Module(includes = BaseFragmentModule.class)
public abstract class AssetPairListModule {
    @Binds
    @PerFragment
    abstract AssetPairListContract.Presenter
    provideAssetPairListPresenter(AssetPairListPresenter assetPairListPresenter);
}