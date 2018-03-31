package personal.calebcordell.coinnection.presentation.views.editbalancedialog;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import personal.calebcordell.coinnection.dagger.PerDialog;


@PerDialog
@Subcomponent(modules = EditBalanceDialogModule.class)
public interface EditBalanceDialogSubcomponent extends AndroidInjector<EditBalanceDialogFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<EditBalanceDialogFragment> {
    }
}