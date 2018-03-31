package personal.calebcordell.coinnection.widgets.assetpair;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import personal.calebcordell.coinnection.dagger.PerActivity;


@PerActivity
@Subcomponent(modules = AssetPairWidgetSetupActivityModule.class)
public interface AssetPairWidgetSetupActivitySubcomponent extends AndroidInjector<AssetPairWidgetSetupActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<AssetPairWidgetSetupActivity> {
    }
}