package tk.talcharnes.popularmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Tal on 6/22/2016.
 */
public class MovieDBHelper extends SQLiteOpenHelper {
    private static String LOG_TAG = MovieDBHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 3;
    static final String DATABASE_NAME = "movie.db";

    public MovieDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE IF NOT EXISTS " + MovieContract.FavoritesEntry.TABLE_NAME + " ( "
                + MovieContract.FavoritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.FavoritesEntry.COLUMN_ID + " TEXT NOT NULL, "
                + MovieContract.FavoritesEntry.COLUMN_POSTER_PATH + " TEXT, "
                + MovieContract.FavoritesEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + MovieContract.FavoritesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, "
                + MovieContract.FavoritesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "
                + MovieContract.FavoritesEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL" + " );";

        final String SQL_CREATE_POPULAR_TABLE = "CREATE TABLE IF NOT EXISTS " + MovieContract.FavoritesEntry.TABLE_NAME + " ( "
                + MovieContract.FavoritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.FavoritesEntry.COLUMN_ID + " TEXT NOT NULL, "
                + MovieContract.FavoritesEntry.COLUMN_POSTER_PATH + " TEXT, "
                + MovieContract.FavoritesEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + MovieContract.FavoritesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, "
                + MovieContract.FavoritesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "
                + MovieContract.FavoritesEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL" + " );";

        final String SQL_CREATE_RATING_TABLE = "CREATE TABLE IF NOT EXISTS " + MovieContract.FavoritesEntry.TABLE_NAME + " ( "
                + MovieContract.FavoritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.FavoritesEntry.COLUMN_ID + " TEXT NOT NULL, "
                + MovieContract.FavoritesEntry.COLUMN_POSTER_PATH + " TEXT, "
                + MovieContract.FavoritesEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + MovieContract.FavoritesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, "
                + MovieContract.FavoritesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "
                + MovieContract.FavoritesEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL" + " );";

        db.execSQL(SQL_CREATE_FAVORITES_TABLE);
        Log.i(LOG_TAG, "Creating table with query: " + SQL_CREATE_FAVORITES_TABLE);
        db.execSQL(SQL_CREATE_POPULAR_TABLE);
        Log.i(LOG_TAG, "Creating table with query: " + SQL_CREATE_POPULAR_TABLE);
        db.execSQL(SQL_CREATE_RATING_TABLE);
        Log.i(LOG_TAG, "Creating table with query: " + SQL_CREATE_RATING_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.FavoritesEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.PopularEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.RatingEntry.TABLE_NAME);

        onCreate(db);
    }
}
