package personal.calebcordell.coinnection.presentation.views.settings;

import personal.calebcordell.coinnection.presentation.views.base.BasePresenter;


interface SettingsContract {
    interface View {
        void showForceUpdateDialog();

        void showMessage(String message);
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void onCurrencyPreferenceChanged();

        abstract void onForceUpdatePreferenceClicked();

        abstract void forceUpdate();
    }
}
