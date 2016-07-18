package tk.talcharnes.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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

import tk.talcharnes.popularmovies.db.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class PostersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    GridView gridView;
    Bundle myBundle;
    Uri sortUri;
    Cursor posterCursor;
    PosterAdapter adapter;
    Spinner spinner;
    int spinnerPosition;
    private String sort_method;
    public PostersFragment() {
    }

    @Override
    public LayoutInflater getLayoutInflater(Bundle savedInstanceState) {
        return super.getLayoutInflater(savedInstanceState);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(spinnerPosition, null, this);
        super.onActivityCreated(savedInstanceState);}

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);


        gridView = (GridView) view.findViewById(R.id.gridview);
        adapter = new PosterAdapter(getContext(), null, 0);


       gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(getActivity(), MovieDetails.class);
                intent.putExtra("position", (""+position));
                intent.putExtra("uri", sortUri.toString());
                startActivity(intent);
            }
        });
        gridView.setAdapter(adapter);

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
                    spinnerPosition = 0;
                    sort_method = "popularity.desc";
                    sortUri = MovieContract.PopularEntry.CONTENT_URI;
                    restartPosterLoader();

                }
                else if (position == 1){
                    spinnerPosition = 1;
                    sort_method = "vote_average.desc";
                    sortUri = MovieContract.RatingEntry.CONTENT_URI;
                    restartPosterLoader();
                }
                else if (position == 2){
                    spinnerPosition = 2;
                    sortUri = MovieContract.FavoritesEntry.CONTENT_URI;
                   restartPosterLoader();
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
            restartPosterLoader();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void updatePosters(){
            FetchPostersTask updatePosters = new FetchPostersTask(getContext());
            updatePosters.execute();
        }

public String getSort_method(){
    return sort_method;
}
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {
        switch (loaderID) {
            case 0:
                return new CursorLoader(getActivity(),
                        MovieContract.PopularEntry.CONTENT_URI,
                        new String[]{"_id", "poster_path", "position"},
                        null,
                        null,
                        null
                );
            case 1:
                return new CursorLoader(getActivity(),
                        MovieContract.RatingEntry.CONTENT_URI,
                        new String[]{"_id", "poster_path", "position"},
                        null,
                        null,
                        null
                );
            case 2:
                return new CursorLoader(getActivity(),
                        MovieContract.FavoritesEntry.CONTENT_URI,
                        new String[]{"_id", "poster_path", "position"},
                        null,
                        null,
                        null
                );
            default:return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
     void restartPosterLoader(){
         Log.i("RESTART LOADER", "");
         getLoaderManager().restartLoader(spinnerPosition, null, this);
    }

}