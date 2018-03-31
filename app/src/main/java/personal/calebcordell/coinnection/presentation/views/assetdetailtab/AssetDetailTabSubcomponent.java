package personal.calebcordell.coinnection.presentation.views.assetdetailtab;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import personal.calebcordell.coinnection.dagger.PerChildFragment;


@PerChildFragment
@Subcomponent(modules = AssetDetailTabModule.class)
public interface AssetDetailTabSubcomponent extends AndroidInjector<AssetDetailTabFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<AssetDetailTabFragment> {
    }
}