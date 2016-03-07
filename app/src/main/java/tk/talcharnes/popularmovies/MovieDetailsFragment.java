package tk.talcharnes.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment {

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


        return rootView;
    }
}
