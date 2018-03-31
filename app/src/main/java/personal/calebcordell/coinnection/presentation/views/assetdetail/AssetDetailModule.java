package personal.calebcordell.coinnection.presentation.views.assetdetail;

import dagger.Binds;
import dagger.Module;
import personal.calebcordell.coinnection.dagger.PerFragment;
import personal.calebcordell.coinnection.dagger.module.BaseFragmentModule;


@Module(includes = BaseFragmentModule.class)
public abstract class AssetDetailModule {

    @Binds
    @PerFragment
    abstract AssetDetailContract.Presenter
    provideAssetDetailPresenter(AssetDetailPresenter assetDetailPresenter);
}