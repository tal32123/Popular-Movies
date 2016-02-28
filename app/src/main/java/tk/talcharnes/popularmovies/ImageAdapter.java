package tk.talcharnes.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Tal on 2/24/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private static String[] desc = {
            "http://www.jqueryscript.net/images/jQuery-Ajax-Loading-Overlay-with-Loading-Text-Spinner-Plugin.jpg",
            "http://www.jqueryscript.net/images/jQuery-Ajax-Loading-Overlay-with-Loading-Text-Spinner-Plugin.jpg",
            "http://www.jqueryscript.net/images/jQuery-Ajax-Loading-Overlay-with-Loading-Text-Spinner-Plugin.jpg",
            "http://www.jqueryscript.net/images/jQuery-Ajax-Loading-Overlay-with-Loading-Text-Spinner-Plugin.jpg",
            "http://www.jqueryscript.net/images/jQuery-Ajax-Loading-Overlay-with-Loading-Text-Spinner-Plugin.jpg",
            "http://www.jqueryscript.net/images/jQuery-Ajax-Loading-Overlay-with-Loading-Text-Spinner-Plugin.jpg",
            "http://www.jqueryscript.net/images/jQuery-Ajax-Loading-Overlay-with-Loading-Text-Spinner-Plugin.jpg",
            "http://www.jqueryscript.net/images/jQuery-Ajax-Loading-Overlay-with-Loading-Text-Spinner-Plugin.jpg",
            "http://www.jqueryscript.net/images/jQuery-Ajax-Loading-Overlay-with-Loading-Text-Spinner-Plugin.jpg",
            "http://www.jqueryscript.net/images/jQuery-Ajax-Loading-Overlay-with-Loading-Text-Spinner-Plugin.jpg"
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
            //imageView.setLayoutParams(new GridView.LayoutParams(385, 385));
            convertView = imageView;
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //  imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(mContext).load(imageArray[position])
                .placeholder(R.drawable.sample_0)
                .into(imageView);
        //imageView.setImageResource(Integer.parseInt(imageArray[position]));
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