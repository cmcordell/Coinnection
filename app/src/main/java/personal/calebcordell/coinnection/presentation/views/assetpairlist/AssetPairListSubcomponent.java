package personal.calebcordell.coinnection.presentation.views.assetpairlist;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import personal.calebcordell.coinnection.dagger.PerFragment;


@PerFragment
@Subcomponent(modules = AssetPairListModule.class)
public interface AssetPairListSubcomponent extends AndroidInjector<AssetPairListFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<AssetPairListFragment> {
    }
}