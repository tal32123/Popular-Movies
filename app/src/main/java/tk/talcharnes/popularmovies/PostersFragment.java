package tk.talcharnes.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class PostersFragment extends Fragment {
    private static List<MovieModel> movieModelList;
    private static int movieModelListLength;
    GridView gridView;
    private String done = null;
    ImageAdapter adapter;
    public PostersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        FetchPostersTask fetchPostersTask = (FetchPostersTask) new FetchPostersTask().execute();
        // should find gridview on the view which you are creating
        gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(getContext()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getContext(), "You clicked image " + position + movieModelList.get(0).getTitle() ,
                        Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private final String BASE_URL = "https://api.themoviedb.org/3/";
    private String middle_section = "discover/movie?sort_by=popularity.desc&api_key=";
    private String jSonUrl = BASE_URL + middle_section + BuildConfig.MOVIE_DB_API_KEY;
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
        @Override
        protected Void doInBackground(Void ...params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
//
            //will contain raw Json data
           // String posterJsonString = null;
              try{

                  //open connection to api

                    URL url = new URL(jSonUrl);
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
                            parseMovieJson();;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //ImageAdapter.setAsc(movieModelList.);
            String[] asc = new String[movieModelList.size()];
            for(int i = 0; i < asc.length; i++){
                asc[i]=(getMovieModelList().get(i).getPoster_path());
                ImageAdapter.setAsc(asc);
            }

        }
    }
    public static List<MovieModel> getMovieModelList(){
        return movieModelList;
    }
    public static int getMovieModelListLength(){
        return movieModelListLength;
    }

}
