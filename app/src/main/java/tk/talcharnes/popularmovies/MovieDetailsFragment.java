package tk.talcharnes.popularmovies;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ListView;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment{
    private String id;
    private String trailerList = "";
    private String movie_number;
    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        //get movie object in order to extract details
        Intent intent = getActivity().getIntent();
        int movie_number = intent.getIntExtra("Movie_number", 0);
        MovieModel movie = PostersFragment.getMovieModelList().get(movie_number);

        //set title in details view
        TextView titleView = (TextView) rootView.findViewById(R.id.movie_details_text);
        titleView.setText(movie.getTitle());

        //set poster into details view
        ImageView poster = (ImageView)rootView.findViewById(R.id.poster);
        Picasso.with(getContext()).load(movie.getPoster_path()).placeholder(R.drawable.temp_poster).into(poster);

        // set movie year in details view
        TextView release_date = (TextView)rootView.findViewById(R.id.release_date);
        if(movie.getRelease_date().length() > 3){
        release_date.setText(movie.getRelease_date().substring(0,4));}
        else if (movie.getRelease_date() == null){
            release_date.setText("Release date not available");
        }
        else{
            release_date.setText((movie.getRelease_date()));
        };

        //set vote average in details view
        TextView vote_average = (TextView) rootView.findViewById(R.id.vote_average);
        vote_average.setText(movie.getVote_average() + " /10");

        //set overview in details view
        TextView overview = (TextView) rootView.findViewById(R.id.overview);
        overview.setText(movie.getOverview());

        id = movie.getMovieID();
        MovieJSON fetchMovieJSON = new MovieJSON();
        fetchMovieJSON.execute(id);





        return rootView;
    }

    /**
     * Created by Tal on 6/16/2016.
     */

    public class MovieJSON extends AsyncTask<String, Void, Void> implements AdapterView.OnItemClickListener{
        private ArrayList<MovieTrailer> movieTrailerList = new ArrayList<>();
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
        protected Void doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            try {

                //open connection to api
                final String BASE_URL = "https://api.themoviedb.org/3/movie/";
                MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
                //String movie_id = movieDetailsFragment.getMovieID() + "?";
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
            String[] trailerNames = new String[movieTrailerList.size()];
            for(int i = 0; i < movieTrailerList.size(); i++){
                trailerNames[i]= movieTrailerList.get(i).getMovieName();
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1 , trailerNames);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(this);


        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String url = movieTrailerList.get(position).getTrailerUrl().toString();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }

}


