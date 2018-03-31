package personal.calebcordell.coinnection.widgets.assetpair;

import android.app.Activity;
import android.support.v4.app.Fragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;
import personal.calebcordell.coinnection.dagger.PerActivity;
import personal.calebcordell.coinnection.dagger.module.BaseActivityModule;
import personal.calebcordell.coinnection.presentation.views.assetpairsetup.AssetPairSetupFragment;
import personal.calebcordell.coinnection.presentation.views.assetpairsetup.AssetPairSetupSubcomponent;
import personal.calebcordell.coinnection.presentation.views.base.BaseFragment;


@Module(includes = BaseActivityModule.class,
        subcomponents = AssetPairSetupSubcomponent.class)
public abstract class AssetPairWidgetSetupActivityModule {

    @Binds
    @IntoMap
    @FragmentKey(AssetPairSetupFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    bindAssetPairSetupFragment(AssetPairSetupSubcomponent.Builder builder);

    @Binds
    @PerActivity
    abstract Activity bindActivity(AssetPairWidgetSetupActivity assetPairWidgetSetupActivity);

    @Binds
    @PerActivity
    abstract BaseFragment.BackHandlerInterface
    bindBackHandlerInterface(AssetPairWidgetSetupActivity assetPairWidgetSetupActivity);

    @Binds
    @PerActivity
    abstract BaseFragment.ParentActivityInterface
    bindParentActivityInterface(AssetPairWidgetSetupActivity assetPairWidgetSetupActivity);
}