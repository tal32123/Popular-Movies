package tk.talcharnes.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Tal on 7/11/2016.
 */
public class CursorAdapter extends android.widget.CursorAdapter {
    public CursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.poster_view, parent, false);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String poster_path = cursor.getString(cursor.getColumnIndex("poster_path"));
        Log.i("poster path ", poster_path);
        Picasso.with(context).load(poster_path)
                .placeholder(R.drawable.temp_poster)
                .resize(185, 277)
                .into((ImageView)view);
        cursor.close();
    }
}
