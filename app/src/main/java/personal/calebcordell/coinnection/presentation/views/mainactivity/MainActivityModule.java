package personal.calebcordell.coinnection.presentation.views.mainactivity;

import android.app.Activity;
import android.support.v4.app.Fragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;
import personal.calebcordell.coinnection.dagger.PerActivity;
import personal.calebcordell.coinnection.dagger.module.BaseActivityModule;
import personal.calebcordell.coinnection.presentation.views.about.AboutFragment;
import personal.calebcordell.coinnection.presentation.views.about.AboutSubcomponent;
import personal.calebcordell.coinnection.presentation.views.allassets.AllAssetsFragment;
import personal.calebcordell.coinnection.presentation.views.allassets.AllAssetsSubcomponent;
import personal.calebcordell.coinnection.presentation.views.assetdetail.AssetDetailFragment;
import personal.calebcordell.coinnection.presentation.views.assetdetail.AssetDetailSubcomponent;
import personal.calebcordell.coinnection.presentation.views.assetpairdetail.AssetPairDetailFragment;
import personal.calebcordell.coinnection.presentation.views.assetpairdetail.AssetPairDetailSubcomponent;
import personal.calebcordell.coinnection.presentation.views.assetpairlist.AssetPairListFragment;
import personal.calebcordell.coinnection.presentation.views.assetpairlist.AssetPairListSubcomponent;
import personal.calebcordell.coinnection.presentation.views.assetpairsetup.AssetPairSetupFragment;
import personal.calebcordell.coinnection.presentation.views.assetpairsetup.AssetPairSetupSubcomponent;
import personal.calebcordell.coinnection.presentation.views.assetsearch.AssetSearchFragment;
import personal.calebcordell.coinnection.presentation.views.assetsearch.AssetSearchSubcomponent;
import personal.calebcordell.coinnection.presentation.views.base.BaseFragment;
import personal.calebcordell.coinnection.presentation.views.donations.DonationWalletDialogFragment;
import personal.calebcordell.coinnection.presentation.views.donations.DonationWalletDialogSubcomponent;
import personal.calebcordell.coinnection.presentation.views.donations.DonationsFragment;
import personal.calebcordell.coinnection.presentation.views.donations.DonationsSubcomponent;
import personal.calebcordell.coinnection.presentation.views.editbalancedialog.EditBalanceDialogFragment;
import personal.calebcordell.coinnection.presentation.views.editbalancedialog.EditBalanceDialogSubcomponent;
import personal.calebcordell.coinnection.presentation.views.licenses.LicenseDialogFragment;
import personal.calebcordell.coinnection.presentation.views.licenses.LicenseDialogSubcomponent;
import personal.calebcordell.coinnection.presentation.views.licenses.LicensesFragment;
import personal.calebcordell.coinnection.presentation.views.licenses.LicensesSubcomponent;
import personal.calebcordell.coinnection.presentation.views.portfolio.PortfolioFragment;
import personal.calebcordell.coinnection.presentation.views.portfolio.PortfolioSubcomponent;
import personal.calebcordell.coinnection.presentation.views.portfoliodetail.PortfolioDetailFragment;
import personal.calebcordell.coinnection.presentation.views.portfoliodetail.PortfolioDetailSubcomponent;
import personal.calebcordell.coinnection.presentation.views.settings.SettingsFragment;
import personal.calebcordell.coinnection.presentation.views.settings.SettingsSubcomponent;


@Module(includes = BaseActivityModule.class,
        subcomponents = {
                AboutSubcomponent.class,
                AllAssetsSubcomponent.class,
                AssetDetailSubcomponent.class,
                AssetPairDetailSubcomponent.class,
                AssetPairListSubcomponent.class,
                AssetPairSetupSubcomponent.class,
                AssetSearchSubcomponent.class,
                DonationsSubcomponent.class,
                DonationWalletDialogSubcomponent.class,
                EditBalanceDialogSubcomponent.class,
                LicensesSubcomponent.class,
                LicenseDialogSubcomponent.class,
                PortfolioSubcomponent.class,
                PortfolioDetailSubcomponent.class,
                SettingsSubcomponent.class})
public abstract class MainActivityModule {

    @Binds
    @IntoMap
    @FragmentKey(AboutFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    bindAboutFragment(AboutSubcomponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(AllAssetsFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    bindAllAssetsFragment(AllAssetsSubcomponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(AssetDetailFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    bindAssetDetailFragment(AssetDetailSubcomponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(AssetPairDetailFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    bindAssetPairDetailFragment(AssetPairDetailSubcomponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(AssetPairListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    bindAssetPairListFragment(AssetPairListSubcomponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(AssetPairSetupFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    bindAssetPairSetupFragment(AssetPairSetupSubcomponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(AssetSearchFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    bindAssetSearchFragment(AssetSearchSubcomponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(DonationsFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    bindDonationsFragment(DonationsSubcomponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(DonationWalletDialogFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    bindDonationWalletDialogFragment(DonationWalletDialogSubcomponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(EditBalanceDialogFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    bindEditBalanceDialogFragment(EditBalanceDialogSubcomponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(LicensesFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    bindLicensesFragment(LicensesSubcomponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(LicenseDialogFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    bindLicenseDialogFragment(LicenseDialogSubcomponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(PortfolioFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    bindPortfolioFragment(PortfolioSubcomponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(PortfolioDetailFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    bindPortfolioDetailFragment(PortfolioDetailSubcomponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(SettingsFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    bindSettingsFragment(SettingsSubcomponent.Builder builder);

    @Binds
    @PerActivity
    abstract Activity bindActivity(MainActivity mainActivity);

    @Binds
    @PerActivity
    abstract BaseFragment.BackHandlerInterface
    bindBackHandlerInterface(MainActivity mainActivity);

    @Binds
    @PerActivity
    abstract BaseFragment.ParentActivityInterface
    bindParentActivityInterface(MainActivity mainActivity);
}