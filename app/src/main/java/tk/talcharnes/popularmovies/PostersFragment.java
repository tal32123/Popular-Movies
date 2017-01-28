package tk.talcharnes.popularmovies;

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
import android.widget.TextView;

import tk.talcharnes.popularmovies.db.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class PostersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    GridView gridView;
    Bundle myBundle;
    Uri sortUri;
    int posterPosition = gridView.INVALID_POSITION;
    Cursor posterCursor;
    PosterAdapter adapter;
    Spinner spinner;
    int spinnerPosition;
    final String SELECTED_KEY = "poster_Position";
    TextView emptyView;

    public PostersFragment() {
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String sortUri, String position);
    }

    @Override
    public LayoutInflater getLayoutInflater(Bundle savedInstanceState) {
        return super.getLayoutInflater(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //  getLoaderManager().initLoader(spinnerPosition, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            posterPosition = savedInstanceState.getInt(SELECTED_KEY);

        }

        adapter = new PosterAdapter(getContext(), null, 0);
        gridView = (GridView) view.findViewById(R.id.gridview);
        emptyView = (TextView) view.findViewById(R.id.gridview_empty);
        gridView.setEmptyView(emptyView);
        gridView.setAdapter(adapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                posterPosition = position;
                ((Callback) getActivity()).onItemSelected(sortUri.toString(), "" + position);


            }
        });


        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("spinner", spinner.getSelectedItemPosition());
        outState.putInt(SELECTED_KEY, posterPosition);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
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
        if (this.myBundle != null) {
            spinner.setSelection(myBundle.getInt("spinner", 0));
        } else {
            spinner.setSelection(0);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    spinnerPosition = 0;
                    sortUri = MovieContract.PopularEntry.CONTENT_URI;
                    restartPosterLoader();


                } else if (position == 1) {
                    spinnerPosition = 1;
                    sortUri = MovieContract.RatingEntry.CONTENT_URI;
                    restartPosterLoader();
                } else if (position == 2) {
                    spinnerPosition = 2;
                    sortUri = MovieContract.FavoritesEntry.CONTENT_URI;
                    emptyView.setText(getString(R.string.no_favorites_in_list));
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


        return super.onOptionsItemSelected(item);
    }

    private void restartPosterLoader() {
        Log.i("RESTART LOADER", "");
        getLoaderManager().initLoader(spinnerPosition, null, this);
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
            default:
                return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(PostersFragment.class.getSimpleName(), sortUri + " " + spinnerPosition);

        //Destroy old lodaers so they won't be called again when a favorite is added
        //this created a bug previously where a favorite was added/removed it
        //would call an old loader and just be funky in general so ChangeCursor
        //was necessary which doesn't work without the following lines

        if (loader.getId() == 0) {
            getLoaderManager().destroyLoader(1);
            getLoaderManager().destroyLoader(2);
        } else if (loader.getId() == 1) {
            getLoaderManager().destroyLoader(0);
            getLoaderManager().destroyLoader(2);
        } else {
            getLoaderManager().destroyLoader(0);
            getLoaderManager().destroyLoader(1);
        }

        //Changes cursor so that old cursor is not being monitored anymore
        adapter.changeCursor(data);


        if (posterPosition != gridView.INVALID_POSITION) {
            gridView.smoothScrollToPosition(posterPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}