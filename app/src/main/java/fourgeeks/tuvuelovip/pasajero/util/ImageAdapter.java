package fourgeeks.tuvuelovip.pasajero.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fourgeeks.tuvuelovip.pasajero.R;

/**
 * Created by alacret on 3/16/17.
 */

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private String[] urls;
    private String[] descriptions;

    public ImageAdapter(Context c, String[] urls, String[] descriptions) {
        this.context = c;
        this.urls = urls;
        this.descriptions = descriptions;
    }

    public int getCount() {
        return urls.length;
    }

    public Object getItem(int position) {
        return urls[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.confort, parent, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
//        if (convertView == null) {
//            // if it's not recycled, initialize some attributes
//            imageView = new ImageView(context);
//            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(8, 8, 8, 8);
//            imageView.set
//        } else {
//            imageView = (ImageView) convertView;
//        }
        Picasso.with(context).load(urls[position]).into(imageView);

        TextView textView = (TextView)  view.findViewById(R.id.text);
        textView.setText(descriptions[position]);
        return view;
    }

}
