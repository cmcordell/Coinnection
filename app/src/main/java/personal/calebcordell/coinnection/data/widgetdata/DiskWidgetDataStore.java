package personal.calebcordell.coinnection.data.widgetdata;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import personal.calebcordell.coinnection.data.SQLiteHelper;
import personal.calebcordell.coinnection.domain.model.AssetWidget;
import personal.calebcordell.coinnection.domain.model.Widget;


class DiskWidgetDataStore {
    private static DiskWidgetDataStore mInstance;
    private SQLiteDatabase mDatabase;

    private static final String WIDGET_ID = "widget_id";

    private static final String WIDGET_CRYPTOCURRENCY_TABLE = "widgets_cryptocurrency";
    private static final String WIDGET_CRYPTOCURRENCY_ASSET_ID = "asset_id";
    private static final String WIDGET_CRYPTOCURRENCY_TIMEFRAME = "timeframe";
    private static final String[] WIDGET_CRYPTOCURRENCY_COLS = new String[] {WIDGET_ID, WIDGET_CRYPTOCURRENCY_ASSET_ID, WIDGET_CRYPTOCURRENCY_TIMEFRAME};

    private DiskWidgetDataStore() {
        final SQLiteHelper helper = SQLiteHelper.getInstance();
        mDatabase = helper.getWritableDatabase();
    }
    public static DiskWidgetDataStore getInstance() {
        if(mInstance == null) {
            mInstance = new DiskWidgetDataStore();
        }
        return mInstance;
    }

    List<Integer> getAllWidgetIds() {
        ArrayList<Integer> ids = new ArrayList<>();

        final Cursor widgetCryptocurrencyCursor = mDatabase.query(true, WIDGET_CRYPTOCURRENCY_TABLE, WIDGET_CRYPTOCURRENCY_COLS, null, null, null, null, null, null, null);
        if(widgetCryptocurrencyCursor != null) {
            widgetCryptocurrencyCursor.moveToFirst();

            while(!widgetCryptocurrencyCursor.isAfterLast()) {
                ids.add(widgetCryptocurrencyCursor.getInt(0));
                widgetCryptocurrencyCursor.moveToNext();
            }
            widgetCryptocurrencyCursor.close();
        }

        //Add other widgets here

        if(ids.size() == 0) {
            return null;
        }
        return ids;
    }

    List<Integer> getCryptocurrencyInfoWidgetIds() {
        ArrayList<Integer> ids = new ArrayList<>();

        final Cursor widgetCryptocurrencyCursor = mDatabase.query(true, WIDGET_CRYPTOCURRENCY_TABLE, WIDGET_CRYPTOCURRENCY_COLS, null, null, null, null, null, null, null);
        if(widgetCryptocurrencyCursor != null) {
            widgetCryptocurrencyCursor.moveToFirst();

            while(!widgetCryptocurrencyCursor.isAfterLast()) {
                ids.add(widgetCryptocurrencyCursor.getInt(0));
                widgetCryptocurrencyCursor.moveToNext();
            }
            widgetCryptocurrencyCursor.close();
        }

        if(ids.size() == 0) {
            return null;
        }
        return ids;
    }

    Widget getWidget(final int id) {
        Widget widget = null;
        final String[] whereArgs = {String.valueOf(id)};

        final Cursor widgetCryptocurrencyCursor = mDatabase.query(true, WIDGET_CRYPTOCURRENCY_TABLE, WIDGET_CRYPTOCURRENCY_COLS, WIDGET_ID+"=?", whereArgs, null, null, null, null, null);

        if(widgetCryptocurrencyCursor != null) {
            widgetCryptocurrencyCursor.moveToFirst();

            widget = new AssetWidget(widgetCryptocurrencyCursor.getInt(0),
                    widgetCryptocurrencyCursor.getString(2),
                    widgetCryptocurrencyCursor.getInt(3));

            widgetCryptocurrencyCursor.close();
        }

        //Add other widgets here

        return widget;
    }

    List<Widget> getWidgets(final int[] ids) {
        List<Widget> widgets = new ArrayList<>(ids.length);

        for(final int id : ids) {
            final String[] whereArgs = {String.valueOf(id)};

            final Cursor widgetCryptocurrencyCursor = mDatabase.query(true, WIDGET_CRYPTOCURRENCY_TABLE, WIDGET_CRYPTOCURRENCY_COLS, WIDGET_ID + "=?", whereArgs, null, null, null, null, null);
            //Add other widget currsors here

            if (widgetCryptocurrencyCursor != null) {
                widgetCryptocurrencyCursor.moveToFirst();

                widgets.add(new AssetWidget(widgetCryptocurrencyCursor.getInt(0),
                        widgetCryptocurrencyCursor.getString(2),
                        widgetCryptocurrencyCursor.getInt(3)));

                widgetCryptocurrencyCursor.close();
            }
            //Add elseifs for other widgets here

        }

        if(widgets.size() == 0) {
            return null;
        }
        return widgets;
    }

    void updateWidget(Widget widget) {
        final String[] whereArgs = {String.valueOf(widget.getAppWidgetId())};
        ContentValues values = new ContentValues();
        values.put(WIDGET_ID, widget.getAppWidgetId());

        if(widget instanceof AssetWidget) {
            values.put(WIDGET_CRYPTOCURRENCY_ASSET_ID, ((AssetWidget) widget).getAssetId());
            values.put(WIDGET_CRYPTOCURRENCY_TIMEFRAME, ((AssetWidget) widget).getTimeframe());
            mDatabase.update(WIDGET_CRYPTOCURRENCY_TABLE, values, WIDGET_ID+"=?", whereArgs);
        }
        //Add elseifs for other widgets here
    }

    void addWidget(Widget widget) {
        ContentValues values = new ContentValues();
        values.put(WIDGET_ID, widget.getAppWidgetId());

        if(widget instanceof AssetWidget) {
            values.put(WIDGET_CRYPTOCURRENCY_ASSET_ID, ((AssetWidget) widget).getAssetId());
            values.put(WIDGET_CRYPTOCURRENCY_TIMEFRAME, ((AssetWidget) widget).getTimeframe());
            mDatabase.insertWithOnConflict(WIDGET_CRYPTOCURRENCY_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
        //Add elseifs for other widgets here
    }

    void deleteWidget(int id) {
        final String[] whereArgs = {String.valueOf(id)};
        mDatabase.delete(WIDGET_CRYPTOCURRENCY_TABLE, WIDGET_ID+"=?", whereArgs);
    }

    void clearWidgets() {
        mDatabase.delete(WIDGET_CRYPTOCURRENCY_TABLE, null, null);
    }
}
