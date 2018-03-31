package personal.calebcordell.coinnection.widgets.assetpair;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import personal.calebcordell.coinnection.dagger.PerWidget;


@PerWidget
@Subcomponent(modules = AssetPairWidgetProviderModule.class)
public interface AssetPairWidgetProviderSubcomponent extends AndroidInjector<AssetPairWidgetProvider> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<AssetPairWidgetProvider> {
    }
}