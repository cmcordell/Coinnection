package personal.calebcordell.coinnection.widgets.assetpair;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.widget.RemoteViews;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.observers.DisposableSingleObserver;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.interactor.impl.widgetinteractors.FetchAndGetAllAssetPairWidgetsInteractor;
import personal.calebcordell.coinnection.domain.model.AssetPairWidget;
import personal.calebcordell.coinnection.presentation.StringUtils;


public class AssetPairWidgetUpdateService extends JobService {
    private static final String TAG = AssetPairWidgetUpdateService.class.getSimpleName();

    @Inject
    protected FetchAndGetAllAssetPairWidgetsInteractor mFetchAndGetAllAssetPairWidgetsInteractor;

    @Override
    public boolean onStartJob(final JobParameters params) {
        inject(this);

        final Context context = this;
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        mFetchAndGetAllAssetPairWidgetsInteractor.execute(new DisposableSingleObserver<List<AssetPairWidget>>() {
            @Override
            public void onSuccess(List<AssetPairWidget> assetPairWidgets) {
                for(AssetPairWidget widget : assetPairWidgets) {
                    updateWidget(context, appWidgetManager, widget);
                }
                jobFinished(params, true);
            }

            @Override
            public void onError(Throwable e) {}
        });

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }


    private void updateWidget(Context context, AppWidgetManager appWidgetManager, AssetPairWidget widget) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_asset_pair);

        views.setImageViewResource(R.id.icon, widget.getAssetPair().getLogo());
        views.setTextViewText(R.id.header, String.format("%s/%s", widget.getAssetPair().getSymbol(), widget.getAssetPair().getQuoteCurrencySymbol()));
        views.setTextViewText(R.id.price, StringUtils.getFormattedDecimalString(widget.getAssetPair().getPrice()));

        appWidgetManager.updateAppWidget(widget.getId(), views);
    }

    private void inject(Context context) {
        if (mFetchAndGetAllAssetPairWidgetsInteractor == null) {
            AndroidInjection.inject(this);
        }
    }
}
