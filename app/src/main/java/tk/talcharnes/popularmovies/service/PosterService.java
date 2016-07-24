package tk.talcharnes.popularmovies.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
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
import tk.talcharnes.popularmovies.db.MovieContract;

/**
 * Created by Tal on 7/24/2016.
 */
public class PosterService extends IntentService {

    int position;
    final String LOG_TAG = PosterService.class.getSimpleName();
    //will contain raw Json data
    String posterJsonString = null;

    public PosterService() {
        super("PosterService");
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
                    this.getContentResolver().bulkInsert(MovieContract.PopularEntry.CONTENT_URI, cvArray);
                    break;
                case 1:
                    this.getContentResolver().bulkInsert(MovieContract.RatingEntry.CONTENT_URI, cvArray);
                case 2:
                    break;
            }
        }


    }

    @Override
    protected void onHandleIntent(Intent intent) {


        int rowsDeleted = this.getContentResolver().delete(
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

        int rowsDeletedRatingEntry = this.getContentResolver().delete(
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


}

