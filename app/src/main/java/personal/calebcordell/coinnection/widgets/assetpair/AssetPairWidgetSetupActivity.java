package personal.calebcordell.coinnection.widgets.assetpair;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toolbar;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.observers.DisposableSingleObserver;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.dagger.module.BaseActivityModule;
import personal.calebcordell.coinnection.domain.interactor.impl.widgetinteractors.AddFetchReturnAssetPairWidgetInteractor;
import personal.calebcordell.coinnection.domain.model.AssetPair;
import personal.calebcordell.coinnection.domain.model.AssetPairWidget;
import personal.calebcordell.coinnection.presentation.Preferences;
import personal.calebcordell.coinnection.presentation.StringUtils;
import personal.calebcordell.coinnection.presentation.views.assetpairsetup.AssetPairSetupFragment;
import personal.calebcordell.coinnection.presentation.views.base.BaseFragment;


public class AssetPairWidgetSetupActivity extends DaggerAppCompatActivity implements BaseFragment.BackHandlerInterface, BaseFragment.ParentActivityInterface {
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private AppWidgetManager mAppWidgetManager;

    private BaseFragment mSelectedFragment;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @Inject
    protected AddFetchReturnAssetPairWidgetInteractor mAddFetchReturnAssetPairWidgetInteractor;
    @Inject
    protected Preferences mPreferences;
    @Inject
    @Named(BaseActivityModule.ACTIVITY_FRAGMENT_MANAGER)
    protected FragmentManager mFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(mPreferences.getAppThemeStyleAttr());
        setContentView(R.layout.activity_widget_asset_pair_setup);

        ButterKnife.bind(this);

        setActionBar(mToolbar);

        // Find the widget id from the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }


        //initializing RemoteViews and AppWidgetManager
        mAppWidgetManager = AppWidgetManager.getInstance(this);

        if (savedInstanceState == null) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, AssetPairSetupFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (mSelectedFragment != null && !mSelectedFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {
        this.mSelectedFragment = selectedFragment;
    }

    @Override
    public void setHomeAsUp(final boolean isHomeAsUp) {}

    @Override
    public View getRootLayout() {
        return getCurrentFocus();
    }

    public void setActionBarElevation(int elevationDP) {
        mToolbar.setElevation(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, elevationDP, getResources().getDisplayMetrics()));
    }

    public void assetPairSelected(final AssetPair assetPair) {
        AssetPairWidget widget = new AssetPairWidget(mAppWidgetId, assetPair);

        mAddFetchReturnAssetPairWidgetInteractor.execute(widget, new DisposableSingleObserver<AssetPairWidget>() {
            @Override
            public void onSuccess(AssetPairWidget assetPairWidget) {
                finishSetup(assetPairWidget.getAssetPair());
            }

            @Override
            public void onError(Throwable e) {}
        });
    }
    private void finishSetup(final AssetPair assetPair) {
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_asset_pair);

        views.setImageViewResource(R.id.icon, assetPair.getLogo());
        views.setTextViewText(R.id.header, String.format("%s/%s", assetPair.getSymbol(), assetPair.getQuoteCurrencySymbol()));
        views.setTextViewText(R.id.price, StringUtils.getFormattedDecimalString(assetPair.getPrice()));

        mAppWidgetManager.updateAppWidget(mAppWidgetId, views);
        Intent resultValue = new Intent();

        // Set the results as expected from a 'configure activity'.
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    /**
     * Returns number of cells needed for given size of the widget.
     *
     * @param size Widget size in dp.
     * @return Size in number of cells.
     */
    private static int getCellsForSize(int size) {
        return (int) (Math.ceil(size + 30d) / 70d);
    }
}
