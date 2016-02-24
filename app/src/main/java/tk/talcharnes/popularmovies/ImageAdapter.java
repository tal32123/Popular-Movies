package tk.talcharnes.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Integer[] asc = {
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
    };
    private Integer[] desc = {
            R.drawable.sample_7, R.drawable.sample_6,
            R.drawable.sample_5, R.drawable.sample_4,
            R.drawable.sample_3, R.drawable.sample_2,
            R.drawable.sample_1, R.drawable.sample_0,

    };
    public int getCount() {
        return mThumbIds.length;
    }

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public long getItemId(int position) {
        return 0;
    }

    public Object getItem(int position) {
        return null;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
          //  imageView.setLayoutParams(new GridView.LayoutParams(185, 277));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
          //  imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    private Integer[] mThumbIds = getDesc();




    public Integer[] getAsc() {
        return asc;
    }

    public Integer[] getDesc() {
        return desc;
    }
}