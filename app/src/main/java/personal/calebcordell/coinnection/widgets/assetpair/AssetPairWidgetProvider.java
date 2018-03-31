package personal.calebcordell.coinnection.widgets.assetpair;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.util.SparseIntArray;
import android.widget.RemoteViews;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.interactor.impl.widgetinteractors.ClearAssetPairWidgetsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.widgetinteractors.FetchAndGetAllAssetPairWidgetsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.widgetinteractors.RemoveAssetPairWidgetsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.widgetinteractors.RestoreAssetPairWidgetsInteractor;
import personal.calebcordell.coinnection.domain.model.AssetPairWidget;
import personal.calebcordell.coinnection.presentation.StringUtils;


public class AssetPairWidgetProvider extends AppWidgetProvider {
    private static final String TAG = AssetPairWidgetProvider.class.getSimpleName();
    private static final int JOB_ID = 789654444;

    private static final long UPDATE_DELAY = 10 * 60 * 1000; //10 minutes

    @Inject
    protected FetchAndGetAllAssetPairWidgetsInteractor mFetchAndGetAllAssetPairWidgetsInteractor;
    @Inject
    protected RemoveAssetPairWidgetsInteractor mRemoveAssetPairWidgetsInteractor;
    @Inject
    protected RestoreAssetPairWidgetsInteractor mRestoreAssetPairWidgetsInteractor;
    @Inject
    protected ClearAssetPairWidgetsInteractor mClearAssetPairWidgetsInteractor;


    @Override
    public void onUpdate(Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        inject(context);

        mFetchAndGetAllAssetPairWidgetsInteractor.execute(new DisposableSingleObserver<List<AssetPairWidget>>() {
            @Override
            public void onSuccess(List<AssetPairWidget> assetPairWidgets) {
                for(AssetPairWidget widget : assetPairWidgets) {
                    updateWidget(context, appWidgetManager, widget);
                }
            }

            @Override
            public void onError(Throwable e) {}
        });
    }

    private void updateWidget(Context context, AppWidgetManager appWidgetManager, AssetPairWidget widget) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_asset_pair);

        views.setImageViewResource(R.id.icon, widget.getAssetPair().getLogo());
        views.setTextViewText(R.id.header, String.format("%s/%s", widget.getAssetPair().getSymbol(), widget.getAssetPair().getQuoteCurrencySymbol()));
        views.setTextViewText(R.id.price, StringUtils.getFormattedDecimalString(widget.getAssetPair().getPrice()));

        appWidgetManager.updateAppWidget(widget.getId(), views);
    }


    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

        inject(context);

        mRemoveAssetPairWidgetsInteractor.execute(appWidgetIds, new DisposableCompletableObserver() {
            @Override public void onComplete() {}
            @Override public void onError(Throwable e) {}
        });
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);

        inject(context);

        SparseIntArray idsArray = new SparseIntArray(oldWidgetIds.length);
        for(int i=0; i<oldWidgetIds.length; i++) {
            idsArray.put(oldWidgetIds[i], newWidgetIds[i]);
        }

        mRestoreAssetPairWidgetsInteractor.execute(idsArray, new DisposableCompletableObserver() {
            @Override public void onComplete() {}
            @Override public void onError(Throwable e) {}
        });
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        inject(context);

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(
                context.getPackageName(), AssetPairWidgetUpdateService.class.getName()));
        builder.setPeriodic(UPDATE_DELAY)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

        if (jobScheduler == null || jobScheduler.schedule(builder.build()) == JobScheduler.RESULT_FAILURE) {
            Log.e(TAG, "onEnabled(): Some error while scheduling the job");
        }
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);

        inject(context);

        mClearAssetPairWidgetsInteractor.execute(null);

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            jobScheduler.cancel(JOB_ID);
        }
    }

    private void inject(Context context) {
        if (mFetchAndGetAllAssetPairWidgetsInteractor == null
                || mRemoveAssetPairWidgetsInteractor == null
                || mRestoreAssetPairWidgetsInteractor == null
                || mClearAssetPairWidgetsInteractor == null) {
            AndroidInjection.inject(this, context);
        }
    }
}