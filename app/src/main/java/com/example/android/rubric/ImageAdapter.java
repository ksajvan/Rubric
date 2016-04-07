package com.example.android.rubric;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Created by ajvan on 30/03/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
//    private String[] moviePosterImages;

    public ImageAdapter(Context c){
        mContext = c;

        inflater = LayoutInflater.from(c);
    }

    public ImageAdapter(Context c, String[] postersArray){
        mContext = c;
        moviePosterImages = postersArray;

        inflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return moviePosterImages.length;
    }

    @Override
    public Object getItem(int position) {
//        return null;
        return moviePosterImages[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (null == view) {
            view = inflater.inflate(R.layout.poster_image_item, viewGroup, false);
        }

        Picasso picasso = Picasso.with(mContext);
        // Image Source:
        // green (memory, best performance)
        // blue (disk, good performance)
        // red (network, worst performance).
        picasso.setIndicatorsEnabled(true);
        picasso.load(moviePosterImages[i]).placeholder(R.drawable.ic_download).fit().into((ImageView) view);

//        Picasso
//                .with(mContext)
//                .load(moviePosterImages[i])
//                .placeholder(R.drawable.ic_download)
//                .fit()
//                .into((ImageView) view);

        return view;
    }

    public static String[] moviePosterImages = {
            "http://image.tmdb.org/t/p/w185//lIv1QinFqz4dlp5U4lQ6HaiskOZ.jpg",
            "http://image.tmdb.org/t/p/w185//9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg",
            "http://image.tmdb.org/t/p/w185//eqFckcHuFCT1FrzLOAvXBb4jHwq.jpg",
            "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185//d4KNaTrltq6bpkFS01pYtyXa09m.jpg",
            "http://image.tmdb.org/t/p/w185//3TpMBcAYH4cxCw5WoRacWodMTCG.jpg",
            "http://image.tmdb.org/t/p/w185//A2rxR8g3y6kcjIoR2fcwtq9eppc.jpg"
    };
}
