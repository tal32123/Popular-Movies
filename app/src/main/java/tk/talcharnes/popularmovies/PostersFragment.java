package tk.talcharnes.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.List;
import java.util.Vector;

import tk.talcharnes.popularmovies.db.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class PostersFragment extends Fragment {
    private static List<MovieModel> movieModelList;
    private int movieModelListLength;
    GridView gridView;
    Bundle myBundle;
    ImageAdapter adapter;
    Spinner spinner;
    int spinnerPosition;
    private String sort_method;
    public PostersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);


        gridView = (GridView) view.findViewById(R.id.gridview);

        if (getResources().getConfiguration().orientation
                == 1) {
            gridView.setNumColumns(3);


        } else if ( getResources().getConfiguration().orientation
                == 2) {
            gridView.setNumColumns(5);

        }

        adapter = new ImageAdapter(getContext());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getContext(), "You clicked " + movieModelList.get(position).getTitle(),
                        Toast.LENGTH_SHORT).show();
                MovieModel movieModel = movieModelList.get(position);
                Intent intent = new Intent(getActivity(), MovieDetails.class);
                intent.putExtra("Movie_number", position);
                startActivity(intent);

            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("spinner", spinner.getSelectedItemPosition());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(savedInstanceState != null) {
         //  spinner.setSelection(savedInstanceState.getInt("spinner", 0));
            this.myBundle = savedInstanceState;

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //commented out until a settings menu is implemented
      //  super.onCreateOptionsMenu(menu, inflater);
      //  getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        inflater.inflate(R.menu.menu_refresh, menu);


        MenuItem item = menu.findItem(R.id.spinnerr);
         spinner = (Spinner) MenuItemCompat.getActionView(item);


        String[] sortingCriteria = {"Popular", "Highest Rated", "Favorites"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner, sortingCriteria);
        spinner.setAdapter(spinnerAdapter);
        if(this.myBundle != null){
            spinner.setSelection(myBundle.getInt("spinner", 0));
        }
        else {
            spinner.setSelection(0);
         }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position == 0){
                    sort_method = "popularity.desc";
                    spinnerPosition = 0;
                    updatePosters();
                }
                else if (position == 1){
                    sort_method = "vote_average.desc";
                    spinnerPosition = 1;
                    updatePosters();
                }
                else if (position == 2){
                    spinnerPosition = 2;
                    Toast.makeText(getContext(), "Favorites is not yet available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // does nothing
            }

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_refresh) {
            Toast.makeText(getActivity(), "Refreshing",
                    Toast.LENGTH_SHORT).show();

           updatePosters();
            gridView.setAdapter(adapter);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void updatePosters(){
        FetchPostersTask updatePosters = new FetchPostersTask();
        updatePosters.execute();
    }









    //Get movie posters and data
    public class FetchPostersTask extends AsyncTask<Void,Void,Void> {
        private final String LOG_TAG = FetchPostersTask.class.getSimpleName();
        //will contain raw Json data
        String posterJsonString = null;


        public Void parseMovieJson()
                throws JSONException{
            JSONObject jsonParentObject = new JSONObject(posterJsonString);
            JSONArray movieJSonArray = jsonParentObject.getJSONArray("results");

            movieModelList = new ArrayList<>();
            for(int i = 0; i < movieJSonArray.length(); i++){
                JSONObject movieJsonObject = movieJSonArray.getJSONObject(i);
                MovieModel movieModel = new MovieModel();
               movieModel.setMovieID(movieJsonObject.getString("id"));
                movieModel.setTitle(movieJsonObject.getString("title"));
                movieModel.setOverview(movieJsonObject.getString("overview"));
                movieModel.setPoster_path(movieJsonObject.getString("poster_path"));
                movieModel.setRelease_date(movieJsonObject.getString("release_date"));
                movieModel.setVote_average(movieJsonObject.getString("vote_average"));
                movieModelListLength++;

                movieModelList.add(movieModel);
            }
            return null;
        }
        public void addMoviesToDb() throws JSONException {


            JSONObject jsonParentObject = new JSONObject(posterJsonString);
            JSONArray movieJSonArray = jsonParentObject.getJSONArray("results");


            Vector<ContentValues> cVVector = new Vector<ContentValues>(movieJSonArray.length());
            for (int i = 0; i < movieJSonArray.length(); i++) {
                JSONObject movieJsonObject = movieJSonArray.getJSONObject(i);
                String id = movieJsonObject.getString("id");
                String title = movieJsonObject.getString("title");
                String overview = movieJsonObject.getString("overview");
                String poster_path = movieJsonObject.getString("poster_path");
                String release_date = movieJsonObject.getString("release_date");
                String vote_average = movieJsonObject.getString("vote_average");
                ContentValues movieValues = new ContentValues();
                switch (spinnerPosition) {
                    case 0: {
                        movieValues.put(MovieContract.PopularEntry.COLUMN_ID, id);
                        movieValues.put(MovieContract.PopularEntry.COLUMN_TITLE, title);
                        movieValues.put(MovieContract.PopularEntry.COLUMN_OVERVIEW, overview);
                        movieValues.put(MovieContract.PopularEntry.COLUMN_POSTER_PATH, poster_path);
                        movieValues.put(MovieContract.PopularEntry.COLUMN_RELEASE_DATE, release_date);
                        movieValues.put(MovieContract.PopularEntry.COLUMN_VOTE_AVERAGE, vote_average);
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
                        cVVector.add(movieValues);
                        break;
                    }
                    case 2: {
                        break;
                    }
                }
            }
                if (cVVector.size() > 0){
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    switch (spinnerPosition){
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

        @Override
        protected Void doInBackground(Void ...params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            //will contain raw Json data
            try{

                //open connection to api

                final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
                final String SORT_PARAM ="sort_by";
                final String MINIMUM_VOTES_PARAM="vote_count.gte";
                final String MINIMUM_VOTES= 500+"";


                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, sort_method)
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
                if(inputStream == null){
                    //nothing else to do in this case
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = reader.readLine())!= null){
                    buffer.append(line + "\n");
                }

                if(buffer.length()==0){
                    //nothing here, don't parse
                    return null;
                }

                posterJsonString = buffer.toString();
            }
            catch(MalformedURLException e){
                e.printStackTrace();
            }
            catch(IOException e){
                Log.e(LOG_TAG, "Error", e);
                return null;
            }
            finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(reader != null){
                    try{
                        reader.close();

                    }
                    catch (final IOException e){
                        Log.e(LOG_TAG,"Error closing stream", e);
                    }
                }
            }
            try{
                parseMovieJson();
                addMoviesToDb();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            String[] asc = new String[movieModelList.size()];
            for(int i = 0; i < asc.length; i++){
                asc[i]=(getMovieModelList().get(i).getPoster_path());

            }

            adapter.setImageArray(asc);
            adapter.notifyDataSetChanged();
        }
    }
    public static List<MovieModel> getMovieModelList(){
        return movieModelList;
    }

}