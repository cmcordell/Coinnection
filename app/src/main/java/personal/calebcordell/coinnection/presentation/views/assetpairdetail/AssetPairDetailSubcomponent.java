package personal.calebcordell.coinnection.presentation.views.assetpairdetail;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import personal.calebcordell.coinnection.dagger.PerFragment;


@PerFragment
@Subcomponent(modules = AssetPairDetailModule.class)
public interface AssetPairDetailSubcomponent extends AndroidInjector<AssetPairDetailFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<AssetPairDetailFragment> {
    }
}