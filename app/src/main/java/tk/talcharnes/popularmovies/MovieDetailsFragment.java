package tk.talcharnes.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        Intent intent = getActivity().getIntent();
        int movie_number = intent.getIntExtra("Movie_number", 0);
        TextView titleView = (TextView) rootView.findViewById(R.id.movie_details_text);
        String title = PostersFragment.getMovieModelList().get(movie_number).getTitle();
        titleView.setText(title);


        return rootView;
    }
}
