package tk.talcharnes.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import tk.talcharnes.popularmovies.db.FavoriteMovie;

public class MovieDetails extends ActionBarActivity {
    Bundle arguments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        if(savedInstanceState == null){


            Intent intent = getIntent();
            String  position = intent.getStringExtra("position");
            String uri = intent.getStringExtra("uri");


            arguments = new Bundle();
            arguments.putString("position", position);
            arguments.putString("uri", uri);

            MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
            movieDetailsFragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction().
                    add(R.id.movie_detail_container, movieDetailsFragment).commit();


        }

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

        return super.onOptionsItemSelected(item);
    }
    public void favorited(View view){
        FavoriteMovie favoriteMovie = new FavoriteMovie();
        favoriteMovie.favorited(view, arguments, getApplicationContext());
    }

}
