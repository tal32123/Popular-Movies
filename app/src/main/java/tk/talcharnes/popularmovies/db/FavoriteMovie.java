package tk.talcharnes.popularmovies.db;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import tk.talcharnes.popularmovies.R;

/**
 * Created by Tal on 7/22/2016.
 */
public class FavoriteMovie {

    public void favorited(View v, Bundle bundle, Context context){
        CheckBox favorited = (CheckBox) v.findViewById(R.id.favorite);

        if(bundle != null) {

            String title;
            String id;
            String release_date_string;
            String poster_path;
            String overview_string;
            String vote_average;
            Cursor movieIDCursor;

            String position = bundle.getString("position");
            String uriString = bundle.getString("uri");
            Uri uri = Uri.parse(uriString);

            //switch because favorite position is checked differently
            switch (uriString) {
                case "content://tk.talcharnes.popularmovies.db/favorites":
                    movieIDCursor = context.getContentResolver().query(
                            uri,
                            null,
                            null,
                            null,
                            null);
                    break;


                default:
                    movieIDCursor = context.getContentResolver().query(
                            uri,
                            null,
                            "position = ? ",
                            new String[]{position},
                            null
                    );
                    break;
            }

            movieIDCursor.moveToFirst();
            if (uriString.equals("content://tk.talcharnes.popularmovies.db/favorites")) {
                movieIDCursor.moveToPosition(Integer.parseInt(position));
            }
            title = movieIDCursor.getString(movieIDCursor.getColumnIndex("title"));
            release_date_string = movieIDCursor.getString(movieIDCursor.getColumnIndex("release_date"));
            poster_path = movieIDCursor.getString(movieIDCursor.getColumnIndex("poster_path"));
            overview_string = movieIDCursor.getString(movieIDCursor.getColumnIndex("overview"));
            vote_average = movieIDCursor.getString(movieIDCursor.getColumnIndex("vote_average"));
            id = movieIDCursor.getString(movieIDCursor.getColumnIndex("id"));
            movieIDCursor.close();


            if (favorited.isChecked()) {
                favorited.setText("Remove from favorites");
                Toast.makeText(context, "Movie added to favorites", Toast.LENGTH_SHORT).show();


                Cursor favoriteCursor = context.getContentResolver().query(
                        MovieContract.FavoritesEntry.CONTENT_URI,
                        new String[]{MovieContract.FavoritesEntry._ID},
                        MovieContract.FavoritesEntry.COLUMN_ID + " = ? ",
                        new String[]{id},
                        null
                );


                if (favoriteCursor.moveToFirst()) {
                    int movieIdIndex = favoriteCursor.getColumnIndex(MovieContract.FavoritesEntry._ID);
                    //not same as movieID. This is the _ID column where movieID is the ID for themoviedb.org
                    long movie_id = favoriteCursor.getLong(movieIdIndex);
                    Log.i("fav availabe. _id = ", "" + movie_id);
                } else {
                    ContentValues movieValues = new ContentValues();
                    movieValues.put(MovieContract.FavoritesEntry.COLUMN_ID, id);
                    movieValues.put(MovieContract.FavoritesEntry.COLUMN_OVERVIEW, overview_string);
                    movieValues.put(MovieContract.FavoritesEntry.COLUMN_POSTER_PATH, poster_path);
                    movieValues.put(MovieContract.FavoritesEntry.COLUMN_RELEASE_DATE, release_date_string);
                    movieValues.put(MovieContract.FavoritesEntry.COLUMN_TITLE, title);
                    movieValues.put(MovieContract.FavoritesEntry.COLUMN_VOTE_AVERAGE, vote_average);

                    Uri insertedUri =
                            context.getContentResolver().insert(MovieContract.FavoritesEntry.CONTENT_URI, movieValues);

                    long movie_id = ContentUris.parseId(insertedUri);
                    Log.i("fav created. _id = ", "" + movie_id);

                }

                favoriteCursor.close();


            } else {
                favorited.setText("Add to favorites");
                Toast.makeText(context, "Movie removed from favorites", Toast.LENGTH_SHORT).show();

                int rowsDeleted = context.getContentResolver().delete(
                        MovieContract.FavoritesEntry.CONTENT_URI,
                        MovieContract.FavoritesEntry.COLUMN_ID + " = ?",
                        new String[]{id}
                );
                Log.i("Rows deleted: ", "" + rowsDeleted);

            }
        }
    }
}
