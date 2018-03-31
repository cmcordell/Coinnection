package personal.calebcordell.coinnection.presentation.views.donations;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import personal.calebcordell.coinnection.dagger.PerDialog;


@PerDialog
@Subcomponent(modules = DonationWalletDialogModule.class)
public interface DonationWalletDialogSubcomponent extends AndroidInjector<DonationWalletDialogFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<DonationWalletDialogFragment> {
    }
}