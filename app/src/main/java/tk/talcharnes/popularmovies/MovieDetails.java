package tk.talcharnes.popularmovies;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

public class MovieDetails extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.container, new MovieDetailsFragment()).commit();

        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Movie Details");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void favorited(View v){
        CheckBox favorited = (CheckBox) findViewById(R.id.favorite);
//        String movieID = MovieDetailsFragment.getMovieID();
//
//        if (favorited.isChecked()){
//            favorited.setText("Remove from favorites");
//            Toast.makeText(getApplicationContext(), "Movie added to favorites", Toast.LENGTH_SHORT).show();
//
//
//            //// TODO: 7/7/2016 make favorites button stay highlighted if favorite is clicked
//            Cursor favoriteCursor = getContentResolver().query(
//                    MovieContract.FavoritesEntry.CONTENT_URI,
//                    new String[]{MovieContract.FavoritesEntry._ID},
//                    MovieContract.FavoritesEntry.COLUMN_ID + " = ? ",
//                    new String[]{movieID},
//                    null
//            );
//
//
//
//            if (favoriteCursor.moveToFirst()) {
//                int movieIdIndex = favoriteCursor.getColumnIndex(MovieContract.FavoritesEntry._ID);
//                //not same as movieID. This is the _ID column where movieID is the ID for themoviedb.org
//                long movie_id = favoriteCursor.getLong(movieIdIndex);
//                Log.i("fav availabe. _id = ", "" + movie_id);
//            }
//
//            else {
//                ContentValues movieValues = new ContentValues();
//                movieValues.put(MovieContract.FavoritesEntry.COLUMN_ID, movieID);
//                movieValues.put(MovieContract.FavoritesEntry.COLUMN_OVERVIEW, MovieDetailsFragment.getOverview());
//                movieValues.put(MovieContract.FavoritesEntry.COLUMN_POSTER_PATH, MovieDetailsFragment.getPoster_path());
//                movieValues.put(MovieContract.FavoritesEntry.COLUMN_RELEASE_DATE, MovieDetailsFragment.getRelease_date());
//                movieValues.put(MovieContract.FavoritesEntry.COLUMN_TITLE, MovieDetailsFragment.getTitle());
//                movieValues.put(MovieContract.FavoritesEntry.COLUMN_VOTE_AVERAGE, MovieDetailsFragment.getVote_average());
//
//                Uri insertedUri =
//                        getContentResolver().insert(MovieContract.FavoritesEntry.CONTENT_URI, movieValues);
//
//                long movie_id = ContentUris.parseId(insertedUri);
//                Log.i("fav created. _id = ", ""+ movie_id);
//                Log.i("Path = ", getApplicationContext().getDatabasePath(MovieContract.FavoritesEntry.TABLE_NAME).toString());
//
//            }
//
//                favoriteCursor.close();
//
//
//        }
//        else{
//            favorited.setText("Add to favorites");
//            Toast.makeText(getApplicationContext(), "Movie removed from favorites", Toast.LENGTH_SHORT).show();
//
//            //// TODO: 7/7/2016 delete movie from favorites db
//                int rowsDeleted = getContentResolver().delete(
//                        MovieContract.FavoritesEntry.CONTENT_URI,
//                        MovieContract.FavoritesEntry.COLUMN_ID + " = ?",
//                        new String[]{movieID}
//                );
//                Log.i("Rows deleted: ", ""+ rowsDeleted);
//
//        }
    }


}
