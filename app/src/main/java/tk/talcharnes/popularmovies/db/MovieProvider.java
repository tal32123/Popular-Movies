package tk.talcharnes.popularmovies.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Tal on 6/26/2016.
 */
public class MovieProvider extends ContentProvider {
    final static int POPULAR_MOVIES = 100;
    final static int POPULAR_MOVIES_WITH_ID = 101;
    final static int RATING_MOVIES = 200;
    final static int RATING_MOVIES_WITH_ID = 201;
    final static int FAVORITE_MOVIES = 300;
    final static int FAVORITE_MOVIES_WITH_ID = 301;
    private static final String LOG_TAG = MovieProvider.class.getSimpleName();
    private MovieDBHelper mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_POPULAR, POPULAR_MOVIES);
        matcher.addURI(authority, MovieContract.PATH_POPULAR + "/*", POPULAR_MOVIES_WITH_ID);

        matcher.addURI(authority, MovieContract.PATH_RATING, RATING_MOVIES);
        matcher.addURI(authority, MovieContract.PATH_RATING + "/*", RATING_MOVIES_WITH_ID);

        matcher.addURI(authority, MovieContract.PATH_FAVORITES, FAVORITE_MOVIES);
        matcher.addURI(authority, MovieContract.PATH_FAVORITES + "/*", FAVORITE_MOVIES_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        switch (sUriMatcher.match(uri)){
            case POPULAR_MOVIES: {
                retCursor = db.query(MovieContract.PopularEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case POPULAR_MOVIES_WITH_ID: {
                retCursor = db.query(MovieContract.PopularEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case FAVORITE_MOVIES: {
                retCursor =  db.query(MovieContract.FavoritesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case FAVORITE_MOVIES_WITH_ID: {
                retCursor = null;
                break;
            }
            case RATING_MOVIES: {
                retCursor = db.query(MovieContract.RatingEntry.TABLE_NAME, null, null, null, null, null, null);
                break;
            }
            case RATING_MOVIES_WITH_ID: {
                retCursor = null;
                break;
            }
            default: throw new UnsupportedOperationException("Unknown Uri " + uri);
        }

            retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case POPULAR_MOVIES: return MovieContract.PopularEntry.CONTENT_TYPE;
            case POPULAR_MOVIES_WITH_ID: return MovieContract.PopularEntry.CONTENT_ITEM_TYPE;
            case FAVORITE_MOVIES: return MovieContract.FavoritesEntry.CONTENT_TYPE;
            case FAVORITE_MOVIES_WITH_ID: return MovieContract.FavoritesEntry.CONTENT_ITEM_TYPE;
            case RATING_MOVIES: return MovieContract.RatingEntry.CONTENT_TYPE;
            case RATING_MOVIES_WITH_ID: return MovieContract.RatingEntry.CONTENT_ITEM_TYPE;
            default: throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case POPULAR_MOVIES: {
                long _id = db.insert(MovieContract.PopularEntry.TABLE_NAME, null, values);
                if (_id > 0) returnUri = MovieContract.PopularEntry.buildPopularUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FAVORITE_MOVIES: {
                long _id = db.insert(MovieContract.FavoritesEntry.TABLE_NAME, null, values);
                if (_id > 0) returnUri = MovieContract.FavoritesEntry.buildFavoritesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case RATING_MOVIES: {
                long _id = db.insert(MovieContract.RatingEntry.TABLE_NAME, null, values);
                if(_id > 0) returnUri = MovieContract.RatingEntry.buildRatingUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        //This makes delete all rows return number of rows deleted
        if (null == selection)  selection = "1";
        switch (match){
            case POPULAR_MOVIES:
                rowsDeleted = db.delete(MovieContract.PopularEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVORITE_MOVIES:
                rowsDeleted = db.delete(MovieContract.FavoritesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case RATING_MOVIES:
                rowsDeleted = db.delete(MovieContract.RatingEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }
        //because a null deletes all rows
        if (rowsDeleted !=0 ){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match){
            case POPULAR_MOVIES:
                rowsUpdated = db.update(MovieContract.PopularEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case FAVORITE_MOVIES:
                rowsUpdated = db.update(MovieContract.FavoritesEntry.TABLE_NAME, values , selection, selectionArgs);
                break;
            case RATING_MOVIES:
                rowsUpdated = db.update(MovieContract.RatingEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
                default:
                    throw new UnsupportedOperationException("Unknown Uri " + uri);
        }
        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case POPULAR_MOVIES:
            {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.PopularEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
        }
            case RATING_MOVIES:{
                db.beginTransaction();
                int returnCount = 0;
                try{
                    for(ContentValues value : values){
                        long _id = db.insert(MovieContract.RatingEntry.TABLE_NAME, null, value);
                        if(_id != -1){
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case FAVORITE_MOVIES:{
                db.beginTransaction();
                int returnCount = 0;
                try{
                    for(ContentValues value : values){
                        long _id = db.insert(MovieContract.FavoritesEntry.TABLE_NAME, null, value);
                        if(_id != -1){
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }

            default:
                return super.bulkInsert(uri, values);
        }
    }
}
