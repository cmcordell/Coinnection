package personal.calebcordell.coinnection.presentation.views.allassets;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import personal.calebcordell.coinnection.dagger.PerFragment;


@PerFragment
@Subcomponent(modules = AllAssetsModule.class)
public interface AllAssetsSubcomponent extends AndroidInjector<AllAssetsFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<AllAssetsFragment> {
    }
}