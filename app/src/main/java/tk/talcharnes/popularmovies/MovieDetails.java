package tk.talcharnes.popularmovies;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

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
        if (favorited.isChecked()){
            favorited.setText("Remove from favorites");
            Toast.makeText(getApplicationContext(), "Movie added to favorites", Toast.LENGTH_SHORT).show();
        }
        else{
            favorited.setText("Add to favorites");
            Toast.makeText(getApplicationContext(), "Movie removed from favorites", Toast.LENGTH_SHORT).show();
        }
    }

}
