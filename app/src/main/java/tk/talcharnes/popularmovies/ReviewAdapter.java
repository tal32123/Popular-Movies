package tk.talcharnes.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tal on 6/20/2016.
 */
public class ReviewAdapter extends ArrayAdapter<MovieReview> {

    private final String LOG_TAG = ReviewAdapter.class.getSimpleName();

    /**
     * @param context              The current context. Used to inflate the layout file.
     * @param movieReviewArrayList A List of MovieReview objects to display in a list
     */
    public ReviewAdapter(Activity context, ArrayList<MovieReview> movieReviewArrayList) {
        super(context, 0, movieReviewArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieReview movieReview = getItem(position);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.review_cardview_layout, parent, false);
        TextView author = (TextView) rootView.findViewById(R.id.author);
        author.setText(movieReview.getAuthor().toString());
        TextView review = (TextView) rootView.findViewById(R.id.review);
        review.setText(movieReview.getReview().toString());
        return rootView;
    }
}
