package personal.calebcordell.coinnection.presentation.views.licenses;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import personal.calebcordell.coinnection.dagger.PerFragment;


@PerFragment
@Subcomponent(modules = LicensesModule.class)
public interface LicensesSubcomponent extends AndroidInjector<LicensesFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<LicensesFragment> {
    }
}