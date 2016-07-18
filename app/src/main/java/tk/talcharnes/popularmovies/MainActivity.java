package tk.talcharnes.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {
        private Bundle state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        state = savedInstanceState;


        //Checks to see if there is internet connection or not. If so, it brings you into the proper layout.
        //Otherwise it brings you to a layout stating that a connection is necessary to continue (and it has a refresh button)
        if(savedInstanceState==null){ //&& isNetworkAvailable()){
        setContentView(R.layout.activity_main);


            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment, new PostersFragment())
                    .commit();
        }
//        else {
//            setContentView(R.layout.no_network);
//
//        }
        else{


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, new PostersFragment())
                    .commit();

        }
    }
    //refresh button for once a connection is established
    public void refresh(View view){
        if(state==null && isNetworkAvailable()){
            setContentView(R.layout.activity_main);


            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment, new PostersFragment())
                    .commit();
        }    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
