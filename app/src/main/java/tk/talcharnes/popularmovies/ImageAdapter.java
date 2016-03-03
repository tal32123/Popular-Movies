package tk.talcharnes.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Tal on 2/24/2016.
 */
public class ImageAdapter extends BaseAdapter {
    int pixels;
  //  private static String[] desc = new String[9];
    private static String[] desc = {
            "http://www.jqueryscript.net/images/jQuery-Ajax-Loading-Overlay-with-Loading-Text-Spinner-Plugin.jpg",

    };
    private static String[] imageArray = getDesc();
    private Context mContext;
    //private String[] asc = new String[PostersFragment.getMovieModelListLength()];
    private static String[] asc = {};

    public ImageAdapter(){

    }


    public int getCount() {
        try{
            return imageArray.length;}
        catch(NullPointerException e){
            e.printStackTrace();
            return 0;
        }
    }

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        return imageArray[position];
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            pixels = (int) (mContext.getResources().getDisplayMetrics().density + 0.5f);


           imageView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));

            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(mContext).load(imageArray[position])
                .placeholder(R.drawable.temp_poster)
                .resize(185, 277)
                .into(imageView);

        return imageView;
    }



    public static String[] getAsc() {
        return asc;
    }

    public static void setAsc(String[] asc) {
        ImageAdapter.asc = asc;
    }

    public static String[] getDesc() {
        return desc;
    }
    public static void setImageArray(String[] arrayName){
        imageArray = arrayName;
    }
}