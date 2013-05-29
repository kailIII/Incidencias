package com.dexafree.incidencias;

/**
 * Created by Carlos on 18/05/13.
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class FavoritosAdapter extends BaseAdapter {



    private Activity activity;
    //public ArrayList<Favoritos> data = Favoritos.FavoritosList;
    private static LayoutInflater inflater = null;
    //public ImageLoader imageLoader;
    ViewHolder holder;

    FavoritosAdapter(Activity a) {

        activity = a;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //	imageLoader = new ImageLoader(activity.getApplicationContext());

    }

    @Override
    public int getCount() {
        return Favoritos.FavoritosList.size();

    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    public static class ViewHolder {

        public TextView carretera;
        public TextView provincia;




    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        if (convertView == null) {
            vi = inflater.inflate(R.layout.row_favs, null);
            holder = new ViewHolder();

            holder.carretera = (TextView) vi.findViewById(R.id.carreteraFav);
            holder.provincia = (TextView) vi.findViewById(R.id.provFav);

            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();


        holder.carretera.setText("CARRETERA: " + Favoritos.FavoritosList.get(position).getCarretera());
        holder.provincia.setText("PROVINCIA: " + Favoritos.FavoritosList.get(position).getProvincia());



//

        return vi;
    }


    public void setNotifyOnChange(Boolean notifyOnChange){
        notifyOnChange = true;

    }





}