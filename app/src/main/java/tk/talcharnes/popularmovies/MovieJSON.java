package tk.talcharnes.popularmovies;
import android.net.Uri;
import android.os.AsyncTask;
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
import java.util.ArrayList;

/**
 * Created by Tal on 6/16/2016.
 */

    public class MovieJSON extends AsyncTask<Void, Void, Void> {
        private static ArrayList<MovieTrailer> movieTrailerList = new ArrayList<>();
        //will contain raw Json data
        String movieExtrasJSONString = null;
        public final String LOG_TAG = MovieJSON.class.getSimpleName();

        public Void parseMovieExtraJson()
                throws JSONException {
            JSONObject jsonParentObject = new JSONObject(movieExtrasJSONString);
            JSONObject trailerJSonArray = jsonParentObject.getJSONObject("trailers");
            JSONArray youtubeTrailers = trailerJSonArray.getJSONArray("youtube");
            for(int i = 0; i < youtubeTrailers.length(); i++){
                JSONObject youtubeTrailerArray = youtubeTrailers.getJSONObject(i);
                MovieTrailer movieTrailer = new MovieTrailer();
                movieTrailer.setMovieName(youtubeTrailerArray.getString("name"));
                movieTrailer.setTrailerUrl("https://www.youtube.com/watch?v=" + youtubeTrailerArray.getString("source"));
                movieTrailerList.add(movieTrailer);
            }

            return null;
        }


        @Override
        protected Void doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            try {

                //open connection to api
                final String BASE_URL = "https://api.themoviedb.org/3/movie/";
                String movie_id = MovieDetailsFragment.getMovieID() + "?";
                final String APPEND_EXTRAS = "append_to_response";
                final String EXTRAS = "releases,trailers,reviews";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(movie_id)
                        .appendQueryParameter(APPEND_EXTRAS, EXTRAS)
                        .appendQueryParameter("api_key", BuildConfig.MOVIE_DB_API_KEY).build();

                URL url = new URL(builtUri.toString());
                Log.i(LOG_TAG, "url" + url);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //read input into string
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    //nothing else to do in this case
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    //nothing here, don't parse
                    return null;
                }

                movieExtrasJSONString = buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
                return null;
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
             try{


             parseMovieExtraJson();
                } catch (JSONException e) {
            e.printStackTrace();
              }
            return null;
            }
    public static ArrayList<MovieTrailer> getMovieTrailerList(){
        return movieTrailerList;
    }
        }



