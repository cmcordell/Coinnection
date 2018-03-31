package personal.calebcordell.coinnection.presentation;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;


public class Constants {
    public static final String EXTRA_ASSET = "asset";
    public static final String EXTRA_ASSET_PAIR = "asset_pair";
    public static final String EXTRA_ASSET_BALANCE = "asset_balance";
    public static final String EXTRA_ALL_ASSET_IDS = "all_asset_ids";
    public static final String EXTRA_ALL_ASSETS = "all_assets";
    public static final String EXTRA_PORTFOLIO_ASSETS = "portfolio_assets";
    public static final String EXTRA_WATCHLIST_ASSETS = "watchlist_assets";
    public static final String EXTRA_PORTFOLIO_ASSET_IDS = "portfolio_asset_ids";
    public static final String EXTRA_WATCHLIST_ASSET_IDS = "watchlist_asset_ids";
    public static final String EXTRA_START_POSITION = "start_position";
    public static final String EXTRA_ON_CLOSE_LISTENER = "on_close_listener";
    public static final String EXTRA_ANIMATION_SETTINGS = "animation_settings";
    public static final String EXTRA_DONATION_ITEM = "donation_item";
    public static final String EXTRA_LICENSE_ITEM = "license_item";


    public static final String MAIN_FRAGMENT = "backstack.main_fragment";
    public static final String NAV_START_FRAGMENT = "backstack.nav_start_fragment";

    public static final long SELECTABLE_VIEW_ANIMATION_DELAY = 100;

    public static final int ASSET_TYPE_ASSET = 0;
    public static final int ASSET_TYPE_PORTFOLIO = 1;
    public static final int ASSET_TYPE_WATCHLIST = 2;
    public static final int ASSET_TYPE_PAIR = 3;

    public static final long RELOAD_TIME_FULL_UPDATE = 24 * 60 * 60 * 1000; //24 hours
    public static final long RELOAD_TIME_SINGLE_ASSET = 5 * 60 * 1000; //5 minutes
    public static final long RELOAD_TIME_ALL_ASSETS = 15 * 60 * 1000; //15 minutes
    public static final long RELOAD_TIME_NETWORK_CHECK = 3000; //3 seconds


    @Retention(SOURCE)
    @IntDef({TIMEFRAME_HOUR, TIMEFRAME_DAY, TIMEFRAME_WEEK,
            TIMEFRAME_MONTH, TIMEFRAME_YEAR, TIMEFRAME_ALL})
    public @interface Timeframe {}
    public static final int TIMEFRAME_HOUR = 0;
    public static final int TIMEFRAME_DAY = 1;
    public static final int TIMEFRAME_WEEK = 2;
    public static final int TIMEFRAME_MONTH = 3;
    public static final int TIMEFRAME_YEAR = 4;
    public static final int TIMEFRAME_ALL = 5;


    public static final int PORTFOLIO_LIST_ITEM_SHOW_PRICE = 0;
    public static final int PORTFOLIO_LIST_ITEM_SHOW_PERCENT_CHANGE = 1;
    public static final int PORTFOLIO_LIST_ITEM_SHOW_BALANCE_VALUE = 2;
    public static final int[] PORTFOLIO_LIST_ITEM_INFO = {
            PORTFOLIO_LIST_ITEM_SHOW_PRICE,
            PORTFOLIO_LIST_ITEM_SHOW_PERCENT_CHANGE,
            PORTFOLIO_LIST_ITEM_SHOW_BALANCE_VALUE};


    public static final int ALL_ASSETS_LIST_ITEM_SHOW_PRICE = 0;
    public static final int ALL_ASSETS_LIST_ITEM_SHOW_PERCENT_CHANGE_1H = 1;
    public static final int ALL_ASSETS_LIST_ITEM_SHOW_PERCENT_CHANGE_24H = 2;
    public static final int ALL_ASSETS_LIST_ITEM_SHOW_PERCENT_CHANGE_7D = 3;
    public static final int ALL_ASSETS_LIST_ITEM_SHOW_MARKET_CAP = 4;
    public static final int ALL_ASSETS_LIST_ITEM_SHOW_24_HOUR_VOLUME = 5;
    public static final int ALL_ASSETS_LIST_ITEM_SHOW_AVAILABLE_SUPPLY = 6;
    public static final int ALL_ASSETS_LIST_ITEM_SHOW_NAME = 7;
    public static final int[] ALL_ASSETS_LIST_ITEM_INFO = {
            ALL_ASSETS_LIST_ITEM_SHOW_PRICE,
            ALL_ASSETS_LIST_ITEM_SHOW_PERCENT_CHANGE_1H,
            ALL_ASSETS_LIST_ITEM_SHOW_PERCENT_CHANGE_24H,
            ALL_ASSETS_LIST_ITEM_SHOW_PERCENT_CHANGE_7D,
            ALL_ASSETS_LIST_ITEM_SHOW_MARKET_CAP,
            ALL_ASSETS_LIST_ITEM_SHOW_24_HOUR_VOLUME,
            ALL_ASSETS_LIST_ITEM_SHOW_AVAILABLE_SUPPLY,
            ALL_ASSETS_LIST_ITEM_SHOW_NAME};


    public static final int LIST_FIRST_ITEM = 0;
    public static final int LIST_CENTER_ITEM = 1;
    public static final int LIST_LAST_ITEM = 2;


    public static final int ASSET_PAIR_FROM_APP = -2;
    public static final int ASSET_PAIR_FROM_WIDGET = -1;


    public static final int SORT_DIRECTION_DESCENDING = 0;
    public static final int SORT_DIRECTION_ASCENDING = 1;


    /**
     * Preferences
     */
    public static final String KEY_APP_THEME = "pref_app_theme";
    @Retention(SOURCE)
    @StringDef({APP_THEME_LIGHT, APP_THEME_DARK})
    public @interface AppTheme {}
    public static final String APP_THEME_LIGHT = "light";
    public static final String APP_THEME_DARK = "dark";


    public static final String KEY_CURRENCY = "pref_currency";
    @Retention(SOURCE)
    @StringDef({CURRENCY_AUD, CURRENCY_BRL, CURRENCY_CAD, CURRENCY_CHF, CURRENCY_CNY,
            CURRENCY_EUR, CURRENCY_GBP, CURRENCY_HKD, CURRENCY_IDR, CURRENCY_INR,
            CURRENCY_JPY, CURRENCY_KRW, CURRENCY_MXN, CURRENCY_RUB, CURRENCY_USD})
    public @interface CurrencyCode {}
    public static final String CURRENCY_AUD = "aud";
    public static final String CURRENCY_BRL = "brl";
    public static final String CURRENCY_CAD = "cad";
    public static final String CURRENCY_CHF = "chf";
    public static final String CURRENCY_CNY = "cny";
    public static final String CURRENCY_EUR = "eur";
    public static final String CURRENCY_GBP = "gbp";
    public static final String CURRENCY_HKD = "hkd";
    public static final String CURRENCY_IDR = "idr";
    public static final String CURRENCY_INR = "inr";
    public static final String CURRENCY_JPY = "jpy";
    public static final String CURRENCY_KRW = "krw";
    public static final String CURRENCY_MXN = "mxn";
    public static final String CURRENCY_RUB = "rub";
    public static final String CURRENCY_USD = "usd";


    public static final String KEY_FIRST_RUN = "pref_first_run";
    public static final String KEY_LAST_FULL_UPDATE = "pref_last_full_update";
    public static final String KEY_FORCE_UPDATE = "pref_force_update";


    public static final String COIN_DOES_NOT_EXIST_MESSAGE = "HTTP 404 ";
}