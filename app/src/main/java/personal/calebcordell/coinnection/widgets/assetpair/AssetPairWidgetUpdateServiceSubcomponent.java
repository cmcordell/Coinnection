package personal.calebcordell.coinnection.widgets.assetpair;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import personal.calebcordell.coinnection.dagger.PerWidget;


@PerWidget
@Subcomponent(modules = AssetPairWidgetUpdateServiceModule.class)
public interface AssetPairWidgetUpdateServiceSubcomponent extends AndroidInjector<AssetPairWidgetUpdateService> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<AssetPairWidgetUpdateService> {
    }
}