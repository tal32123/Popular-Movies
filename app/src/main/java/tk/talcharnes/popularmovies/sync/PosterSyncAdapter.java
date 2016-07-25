package tk.talcharnes.popularmovies.sync;

/**
 * Created by Tal on 7/24/2016.
 */

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import tk.talcharnes.popularmovies.BuildConfig;
import tk.talcharnes.popularmovies.R;
import tk.talcharnes.popularmovies.db.MovieContract;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class PosterSyncAdapter extends AbstractThreadedSyncAdapter {
    static final String LOG_TAG = PosterSyncAdapter.class.getSimpleName();
    int position;
    //Sync interval and flex time are set to once a day because the database is only updated once a day
    public static final int SYNC_INTERVAL = 60 * 60 * 24;

    public static final int SYNC_FLEXTIME = SYNC_INTERVAL;
    //will contain raw Json data
    String posterJsonString = null;


    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;

    /**
     * Set up the sync adapter
     */
    public PosterSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public PosterSyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();

    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "onPerformSync Called.");



        int rowsDeleted = getContext().getContentResolver().delete(
                MovieContract.PopularEntry.CONTENT_URI,
                null,
                null
        );
        Log.i("Rows Deleted", " " + rowsDeleted);
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        //will contain raw Json data
        try {

            //open connection to api

            final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
            final String SORT_PARAM = "sort_by";
            final String MINIMUM_VOTES_PARAM = "vote_count.gte";
            final String MINIMUM_VOTES = 500 + "";


            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, "popularity.desc")
                    .appendQueryParameter(MINIMUM_VOTES_PARAM, MINIMUM_VOTES)
                    .appendQueryParameter("api_key", BuildConfig.MOVIE_DB_API_KEY).build();

            URL url = new URL(builtUri.toString());
            Log.i(LOG_TAG, "url " + url);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //read input into string
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                //nothing else to do in this case
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                //nothing here, don't parse
                return;
            }

            posterJsonString = buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error", e);
            return;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();

                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
            addMoviesToDb(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int rowsDeletedRatingEntry = getContext().getContentResolver().delete(
                MovieContract.RatingEntry.CONTENT_URI,
                null,
                null
        );
        Log.i("Rows Deleted", " " + rowsDeletedRatingEntry);
        urlConnection = null;
        reader = null;

        //will contain raw Json data
        try {

            //open connection to api

            final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
            final String SORT_PARAM = "sort_by";
            final String MINIMUM_VOTES_PARAM = "vote_count.gte";
            final String MINIMUM_VOTES = 500 + "";


            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, "vote_average.desc")
                    .appendQueryParameter(MINIMUM_VOTES_PARAM, MINIMUM_VOTES)
                    .appendQueryParameter("api_key", BuildConfig.MOVIE_DB_API_KEY).build();

            URL url = new URL(builtUri.toString());
            Log.i(LOG_TAG, "url " + url);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //read input into string
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                //nothing else to do in this case
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                //nothing here, don't parse
                return;
            }

            posterJsonString = buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error", e);
            return;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();

                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
            addMoviesToDb(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return;
    }

    public void addMoviesToDb(int position) throws JSONException {


        JSONObject jsonParentObject = new JSONObject(posterJsonString);
        JSONArray movieJSonArray = jsonParentObject.getJSONArray("results");

        Vector<ContentValues> cVVector = new Vector<ContentValues>(movieJSonArray.length());
        for (int i = 0; i < movieJSonArray.length(); i++) {
            JSONObject movieJsonObject = movieJSonArray.getJSONObject(i);
            String id = movieJsonObject.getString("id");
            String title = movieJsonObject.getString("title");
            String overview = movieJsonObject.getString("overview");
            String poster_path = "http://image.tmdb.org/t/p/w185/" + movieJsonObject.getString("poster_path");
            String release_date = movieJsonObject.getString("release_date");
            String vote_average = movieJsonObject.getString("vote_average");
            ContentValues movieValues = new ContentValues();
            switch (position) {
                case 0: {
                    movieValues.put(MovieContract.PopularEntry.COLUMN_ID, id);
                    movieValues.put(MovieContract.PopularEntry.COLUMN_TITLE, title);
                    movieValues.put(MovieContract.PopularEntry.COLUMN_OVERVIEW, overview);
                    movieValues.put(MovieContract.PopularEntry.COLUMN_POSTER_PATH, poster_path);
                    movieValues.put(MovieContract.PopularEntry.COLUMN_RELEASE_DATE, release_date);
                    movieValues.put(MovieContract.PopularEntry.COLUMN_VOTE_AVERAGE, vote_average);
                    movieValues.put(MovieContract.PopularEntry.COLUMN_POSITION, (""+i));
                    cVVector.add(movieValues);
                    break;
                }
                case 1: {
                    movieValues.put(MovieContract.RatingEntry.COLUMN_ID, id);
                    movieValues.put(MovieContract.RatingEntry.COLUMN_TITLE, title);
                    movieValues.put(MovieContract.RatingEntry.COLUMN_OVERVIEW, overview);
                    movieValues.put(MovieContract.RatingEntry.COLUMN_POSTER_PATH, poster_path);
                    movieValues.put(MovieContract.RatingEntry.COLUMN_RELEASE_DATE, release_date);
                    movieValues.put(MovieContract.RatingEntry.COLUMN_VOTE_AVERAGE, vote_average);
                    movieValues.put(MovieContract.RatingEntry.COLUMN_POSITION, (""+i));
                    cVVector.add(movieValues);
                    break;
                }
                case 2: {
                    break;
                }
            }
        }
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            switch (position) {
                case 0:
                    getContext().getContentResolver().bulkInsert(MovieContract.PopularEntry.CONTENT_URI, cvArray);
                    break;
                case 1:
                    getContext().getContentResolver().bulkInsert(MovieContract.RatingEntry.CONTENT_URI, cvArray);
                case 2:
                    break;
            }
        }


    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }
    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            onAccountCreated(newAccount, context);

        }
        return newAccount;
    }


        private static void onAccountCreated(Account newAccount, Context context) {
                /*
         * Since we've created an account
         */
                        PosterSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

                        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
                                ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

                        /*
        * Finally, let's do a sync to get things started
        */
                                syncImmediately(context);
            }

                public static void initializeSyncAdapter(Context context) {
                getSyncAccount(context);
            }
    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }


}

