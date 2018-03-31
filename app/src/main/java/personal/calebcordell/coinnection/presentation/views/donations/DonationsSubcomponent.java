package personal.calebcordell.coinnection.presentation.views.donations;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import personal.calebcordell.coinnection.dagger.PerFragment;


@PerFragment
@Subcomponent(modules = DonationsModule.class)
public interface DonationsSubcomponent extends AndroidInjector<DonationsFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<DonationsFragment> {
    }
}