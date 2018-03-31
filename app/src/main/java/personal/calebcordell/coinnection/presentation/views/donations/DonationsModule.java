package personal.calebcordell.coinnection.presentation.views.donations;

import dagger.Module;
import personal.calebcordell.coinnection.dagger.module.BaseFragmentModule;


@Module(includes = BaseFragmentModule.class,
        subcomponents = DonationWalletDialogSubcomponent.class)
public abstract class DonationsModule {
}