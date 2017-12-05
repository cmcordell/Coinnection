package personal.calebcordell.coinnection.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import personal.calebcordell.coinnection.presentation.App;


public class SQLiteHelper extends SQLiteOpenHelper {

    private static SQLiteHelper instance;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database.db";

    private static final String CREATE_PORTFOLIO_TABLE = "CREATE TABLE portfolios(portfolio_id TEXT PRIMARY KEY, name TEXT NOT NULL);";
    private static final String CREATE_PORTFOLIO_ASSETS_TABLE = "CREATE TABLE portfolio_balances(position INTEGER NOT NULL, portfolio_id TEXT, cryptocurrency_id TEXT, balance REAL NOT NULL, PRIMARY KEY(portfolio_id, cryptocurrency_id));";
    private static final String CREATE_ASSETS_LAST_UPDATE_TABLE = "CREATE TABLE assets_last_update(last_update INTEGER PRIMARY KEY)";
    private static final String CREATE_ALL_ASSETS_TABLE = "CREATE TABLE assets(cryptocurrency_id TEXT PRIMARY KEY, name TEXT NOT NULL, symbol TEXT NOT NULL, rank INTEGER NOT NULL, price REAL, volume_24h REAL, market_cap REAL, available_supply REAL, total_supply REAL, percent_change_1h REAL,  percent_change_24h REAL,  percent_change_7d REAL, last_update INTEGER NOT NULL);";
    private static final String CREATE_WATCHLIST_ASSETS_TABLE = "CREATE TABLE favorite_cryptocurrencies(position INTEGER NOT NULL, cryptocurrency_id TEXT PRIMARY KEY);";
    private static final String CREATE_GLOBAL_MARKET_DATA_TABLE = "CREATE TABLE global_market_data(position INTEGER PRIMARY KEY, total_market_cap REAL NOT NULL, total_volume_24h REAL NOT NULL, bitcoin_percentage REAL NOT NULL, altcoin_percentage REAL NOT NULL, active_currencies INTEGER NOT NULL, active_assets INTEGER NOT NULL, active_markets INTEGER NOT NULL, last_update INTEGER NOT NULL);";
    private static final String CREATE_WIDGETS_ASSET_TABLE = "CREATE TABLE widgets_cryptocurrency(widget_id INTEGER PRIMARY KEY, asset_id TEXT NOT NULL, timeframe INTEGER NOT NULL);";
    private static final String[] CREATE_STATEMENTS = new String[] {CREATE_PORTFOLIO_TABLE, CREATE_PORTFOLIO_ASSETS_TABLE, CREATE_ASSETS_LAST_UPDATE_TABLE, CREATE_ALL_ASSETS_TABLE, CREATE_WATCHLIST_ASSETS_TABLE, CREATE_GLOBAL_MARKET_DATA_TABLE, CREATE_WIDGETS_ASSET_TABLE};

    private static final String DELETE_PORTFOLIO_TABLE = "DROP TABLE IF EXISTS portfolios;";
    private static final String DELETE_PORTFOLIO_ASSETS_TABLE = "DROP TABLE IF EXISTS portfolio_balances;";
    private static final String DELETE_ASSETS_LAST_UPDATE_TABLE = "DROP TABLE IF EXISTS cryptocurrencies_last_update";
    private static final String DELETE_ALL_ASSETS_TABLE = "DROP TABLE IF EXISTS assets;";
    private static final String DELETE_WATCHLIST_ASSETS_TABLE = "DROP TABLE IF EXISTS favorite_cryptocurrencies;";
    private static final String DELETE_GLOBAL_MARKET_DATA_TABLE = "DROP TABLE IF EXISTS global_market_data;";
    private static final String DELETE_WIDGETS_ASSET_TABLE = "DROP TABLE IF EXISTS widgets_cryptocurrency;";
    private static final String[] DELETE_STATEMENTS = new String[] {DELETE_PORTFOLIO_TABLE, DELETE_PORTFOLIO_ASSETS_TABLE, DELETE_ASSETS_LAST_UPDATE_TABLE, DELETE_ALL_ASSETS_TABLE, DELETE_WATCHLIST_ASSETS_TABLE, DELETE_GLOBAL_MARKET_DATA_TABLE, DELETE_WIDGETS_ASSET_TABLE};

    public static synchronized SQLiteHelper getInstance() {
        if(instance == null) {
            instance = new SQLiteHelper(App.getAppContext());
        }
        return instance;
    }

    private SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase mDatabase) {
        for(String statement : CREATE_STATEMENTS) {
            mDatabase.execSQL(statement);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase mDatabase, int oldVersion, int newVersion) {
        for(String statement : DELETE_STATEMENTS) {
            mDatabase.execSQL(statement);
        }
        onCreate(mDatabase);
    }
}
