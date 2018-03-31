package personal.calebcordell.coinnection.presentation.views.assetdetail;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import personal.calebcordell.coinnection.dagger.PerFragment;


@PerFragment
@Subcomponent(modules = AssetDetailModule.class)
public interface AssetDetailSubcomponent extends AndroidInjector<AssetDetailFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<AssetDetailFragment> {
    }
}