package tk.talcharnes.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

import tk.talcharnes.popularmovies.db.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment{

    private String title;
    private String id;
    private String release_date_string;
    private String poster_path;
    private String overview_string;
    private String vote_average;



    private Cursor cursor;
    public MovieDetailsFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String uri;
        Uri movie_uri;
        String position;
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        //get movie object in order to extract details
        Bundle arguments = getArguments();
        if (arguments != null){
            uri = arguments.getString("uri");
            position = arguments.getString("position");
            if(uri != null) {
                movie_uri = Uri.parse(uri);
                Log.i("Position ", position);
                Log.i("URI ", movie_uri.toString());
            }
            else movie_uri = null;
        }
        else {
            uri = null;
            movie_uri = null;
            position = "0";
        }




        //switch because favorite position is checked differently
        if (uri != null){
        switch (uri) {
            case "content://tk.talcharnes.popularmovies.db/favorites":
                cursor = getActivity().getContentResolver().query(
                        movie_uri,
                        null,
                        null,
                        null,
                        null);
                Log.i(uri.toString(), " uri");
                DatabaseUtils.dumpCursor(cursor);
                if (cursor.moveToFirst()) {
                    cursor.moveToPosition(Integer.parseInt(position));

                    title = cursor.getString(cursor.getColumnIndex("title"));
                    release_date_string = cursor.getString(cursor.getColumnIndex("release_date"));
                    poster_path = cursor.getString(cursor.getColumnIndex("poster_path"));
                    overview_string = cursor.getString(cursor.getColumnIndex("overview"));
                    vote_average = cursor.getString(cursor.getColumnIndex("vote_average"));
                    id = cursor.getString(cursor.getColumnIndex("id"));
                    cursor.close();
                } else {
                    title = "Please choose a movie";
                    release_date_string = "N/A";
                    overview_string = "Not Available";
                    vote_average = "0";
                    poster_path = getPoster_path();

                }
                break;

            default:
                cursor = getActivity().getContentResolver().query(
                        movie_uri,
                        null,
                        "position = ? ",
                        new String[]{position},
                        null);
                DatabaseUtils.dumpCursor(cursor);

                if (cursor.moveToFirst()) {

                    title = cursor.getString(cursor.getColumnIndex("title"));
                    release_date_string = cursor.getString(cursor.getColumnIndex("release_date"));
                    poster_path = cursor.getString(cursor.getColumnIndex("poster_path"));
                    overview_string = cursor.getString(cursor.getColumnIndex("overview"));
                    vote_average = cursor.getString(cursor.getColumnIndex("vote_average"));
                    id = cursor.getString(cursor.getColumnIndex("id"));
                    cursor.close();
                }
                else {
                    title = "Please choose a movie";
                    release_date_string = "N/A";
                    overview_string = "Not Available";
                    vote_average = "0";
                    poster_path = getPoster_path();

                }
                break;

        }}

        else{
            title = "Please choose a movie";
            release_date_string = "N/A";
            overview_string = "Not Available";
            vote_average = "0";
            poster_path = getPoster_path();


        }


        //set title in details view
        TextView titleView = (TextView) rootView.findViewById(R.id.movie_details_text);
        titleView.setText(title);
        //// TODO: 7/19/2016 change actionbar so that its height changes dynamically to fit the movie title 
        //sets action bar/toolbar title
        // getActivity().setTitle(title);

        //set poster into details view
        ImageView poster = (ImageView) rootView.findViewById(R.id.poster);
        Picasso.with(getContext()).load(poster_path).placeholder(R.drawable.temp_poster).into(poster);

        // set movie year in details view
        TextView release_date = (TextView) rootView.findViewById(R.id.release_date);

        if (release_date_string == null) {
            release_date.setText("Release date not available");
        } else if (release_date_string.length() > 3) {
            release_date.setText(release_date_string.substring(0, 4));
        } else {
            release_date.setText(release_date_string);
        }
        ;


        //Set vote average rating bar
        RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.rating_bar);
        ratingBar.setIsIndicator(true);
        float rating = (float) (Float.parseFloat(vote_average));
        ratingBar.setRating(rating);


        //set overview in details view
        TextView overview = (TextView) rootView.findViewById(R.id.overview);
        overview.setText(overview_string);


        if (id != null){
        MovieJSON fetchMovieJSON = new MovieJSON();
        fetchMovieJSON.execute(id);

        //checks to see if a movie exists
        CheckBox favorited = (CheckBox) rootView.findViewById(R.id.favorite);
        Cursor favoriteCursor = getActivity().getContentResolver().query(
                MovieContract.FavoritesEntry.CONTENT_URI,
                new String[]{MovieContract.FavoritesEntry._ID, MovieContract.FavoritesEntry.COLUMN_ID},
                MovieContract.FavoritesEntry.COLUMN_ID + " = ? ",
                new String[]{id},
                null
        );
        DatabaseUtils.dumpCursor(favoriteCursor);
        if (favoriteCursor.moveToFirst()) {
            favorited.setChecked(true);
        } else {
            favorited.setChecked(false);
        }
        favoriteCursor.close();
    }

        return rootView;
    }
    /**
     * Created by Tal on 6/16/2016.
     */

    public class MovieJSON extends AsyncTask<String, Void, Void> implements AdapterView.OnItemClickListener{
        private ArrayList<MovieTrailer> movieTrailerList = new ArrayList<>();
        private ArrayList<MovieReview> movieReviewArrayList = new ArrayList<>();
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
            JSONObject reviewsJsonObject = jsonParentObject.getJSONObject("reviews");
            JSONArray resultsJsonArray = reviewsJsonObject.getJSONArray("results");
            for(int i = 0; i < resultsJsonArray.length(); i++){
                JSONObject reviewObject = resultsJsonArray.getJSONObject(i);
                MovieReview review = new MovieReview();
                review.setAuthor(reviewObject.getString("author"));
                review.setReview(reviewObject.getString("content"));
                movieReviewArrayList.add(review);
            }

            return null;
        }


        @Override
        protected Void doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            try {

                //open connection to api
                final String BASE_URL = "https://api.themoviedb.org/3/movie/";
                String movie_id = params[0] + "?";
                final String APPEND_EXTRAS = "append_to_response";
                final String EXTRAS = "releases,trailers,reviews";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(movie_id)
                        .appendQueryParameter(APPEND_EXTRAS, EXTRAS)
                        .appendQueryParameter("api_key", BuildConfig.MOVIE_DB_API_KEY).build();

                URL url = new URL(builtUri.toString());
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



        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ListView listView = (ListView) getActivity().findViewById(R.id.trailer_list);

            //the following code gets all the videos for each movie
            //this was replaced in order to only have the four latest videos so that the layout looks nicer
//            String[] trailerNames = new String[movieTrailerList.size()];
//            for(int i = 0; i < movieTrailerList.size(); i++){
//                trailerNames[i]= movieTrailerList.get(i).getMovieName();
//            }

            //replace this with previous/commented out code to get ALL videos for each movie
            //note this can mess up the layout
            int listSize;
            if (movieTrailerList.size() > 4){
                listSize = 4;
            }
            else {
                listSize = movieTrailerList.size();
            }
            String[] trailerNames = new String[listSize];
            for(int i = 0; i < listSize; i++){
                trailerNames[i]= movieTrailerList.get(i).getMovieName();
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.list_item, trailerNames);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(this);


            ListView reviewListView = (ListView) getActivity().findViewById(R.id.review_listview);
            ReviewAdapter movieReviewReviewAdapter = new ReviewAdapter(getActivity(), movieReviewArrayList);
            reviewListView.setAdapter(movieReviewReviewAdapter);


        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String url = movieTrailerList.get(position).getTrailerUrl().toString();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }


    public String getMovieID(){return id;}

    public String getOverview() {
        return overview_string;
    }



    public String getTitle() {
        return title;
    }


    public String getVote_average() {
        return vote_average;
    }



    public String getRelease_date() {
        return release_date_string;
    }



    public String getPoster_path() {
        if(poster_path != null){
            return ( poster_path);
        }
        else{
            return "http://1vyf1h2a37bmf88hy3i8ce9e.wpengine.netdna-cdn.com/wp-content/themes/public/img/noimgavailable.jpg";}
    }

}


