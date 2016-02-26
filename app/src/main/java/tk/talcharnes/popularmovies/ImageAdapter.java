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
    private String[] imageArray = getAsc();

    private Context mContext;
    private String[] asc = new String[(PostersFragment.getMovieModelList().size())];

    public ImageAdapter(){
        //loads the asc array with movie url's


        for(int i = 0; i < asc.length; i++){
            asc[i]=(PostersFragment.getMovieModelList().get(i).getPoster_path());
        }
    }
    private Integer[] desc = {
            R.drawable.sample_7, R.drawable.sample_6,
            R.drawable.sample_5, R.drawable.sample_4,
            R.drawable.sample_3, R.drawable.sample_2,
            R.drawable.sample_1, R.drawable.sample_0,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7

    };


    public int getCount() {
        return imageArray.length;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(385, 385));
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



    public String[] getAsc() {
        return asc;
    }

    public Integer[] getDesc() {
        return desc;
    }
}