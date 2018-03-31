package personal.calebcordell.coinnection.presentation.views.portfoliodetail;

import android.support.v4.app.Fragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;
import personal.calebcordell.coinnection.dagger.PerFragment;
import personal.calebcordell.coinnection.dagger.module.BaseFragmentModule;
import personal.calebcordell.coinnection.presentation.views.assetdetailtab.AssetDetailTabFragment;
import personal.calebcordell.coinnection.presentation.views.assetdetailtab.AssetDetailTabSubcomponent;


@Module(includes = BaseFragmentModule.class,
        subcomponents = {AssetDetailTabSubcomponent.class})
public abstract class PortfolioDetailModule {

    @Binds
    @IntoMap
    @FragmentKey(AssetDetailTabFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    bindAssetDetailTabFragment(AssetDetailTabSubcomponent.Builder builder);

    @Binds
    @PerFragment
    abstract PortfolioDetailContract.Presenter
    providePortfolioDetailPresenter(PortfolioDetailPresenter portfolioDetailPresenter);
}