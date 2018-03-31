package personal.calebcordell.coinnection.presentation.views.assetsearch;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import personal.calebcordell.coinnection.dagger.PerFragment;


@PerFragment
@Subcomponent(modules = AssetSearchModule.class)
public interface AssetSearchSubcomponent extends AndroidInjector<AssetSearchFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<AssetSearchFragment> {
    }
}