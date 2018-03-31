package personal.calebcordell.coinnection.presentation.views.assetpairsetup;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import personal.calebcordell.coinnection.dagger.PerFragment;


@PerFragment
@Subcomponent(modules = AssetPairSetupModule.class)
public interface AssetPairSetupSubcomponent extends AndroidInjector<AssetPairSetupFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<AssetPairSetupFragment> {
    }
}