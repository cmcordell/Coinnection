package personal.calebcordell.coinnection.presentation.views.settings;


interface SettingsContract {

    interface View {
        void showForceUpdateDialog();
    }

    interface Presenter {
        void onCurrencyPreferenceChanged();

        void onForceUpdatePreferenceClicked();

        void forceUpdate();
    }
}
