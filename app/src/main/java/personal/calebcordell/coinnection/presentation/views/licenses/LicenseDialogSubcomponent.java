package personal.calebcordell.coinnection.presentation.views.licenses;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import personal.calebcordell.coinnection.dagger.PerDialog;


@PerDialog
@Subcomponent(modules = LicenseDialogModule.class)
public interface LicenseDialogSubcomponent extends AndroidInjector<LicenseDialogFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<LicenseDialogFragment> {
    }
}