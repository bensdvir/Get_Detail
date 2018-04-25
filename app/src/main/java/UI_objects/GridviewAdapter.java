package UI_objects;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ws.wolfsoft.get_detail.R;


public class GridviewAdapter extends BaseAdapter {

    Context context;

    ArrayList<Beanclass> bean;
    Typeface fonts1, fonts2;


    public GridviewAdapter(Context context,ArrayList<Beanclass> bean) {
        this.bean = bean;
        this.context = context;
    }

    @Override
    public int getCount() {
        return bean.size();
    }

    @Override
    public Object getItem(int position) {
        return bean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        fonts1 = Typeface.createFromAsset(context.getAssets(),
                "fonts/Lato-Light.ttf");

        fonts2 = Typeface.createFromAsset(context.getAssets(),
                "fonts/Lato-Regular.ttf");

        ViewHolder viewHolder = null;

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.gridview, null);

            viewHolder = new ViewHolder();

            viewHolder.image1 = (ImageView) convertView.findViewById(R.id.image1);
            viewHolder.title1 = (TextView) convertView.findViewById(R.id.title1);
            viewHolder.discription1 = (TextView) convertView.findViewById(R.id.description1);
            viewHolder.date1 = (TextView) convertView.findViewById(R.id.date1);


            viewHolder.title1.setTypeface(fonts2);
            viewHolder.discription1.setTypeface(fonts1);
            viewHolder.date1.setTypeface(fonts2);

            convertView.setTag(viewHolder);


        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }


        final Beanclass bean = (Beanclass) getItem(position);

        /*final ViewHolder finalViewHolder = viewHolder;
        new AsyncTask<Void, Void, Void>() {
            Bitmap bmp =  null;
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InputStream in = new URL(bean.getImage1().toString()).openStream();
                    bmp = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    // log error
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (bmp != null) {
                    finalViewHolder.image1.setImageBitmap(bmp);
                    finalViewHolder.title1.setText(bean.getTitle1());
                    finalViewHolder.discription1.setText(bean.getDiscription1());
                    finalViewHolder.date1.setText(bean.getDate1());
                }
            }

        }.execute();


        viewHolder = finalViewHolder;
        */
        viewHolder.image1.setImageResource(bean.getImage1());
        viewHolder.title1.setText(bean.getTitle1());
        viewHolder.discription1.setText(bean.getDiscription1());
        viewHolder.date1.setText(bean.getDate1());





        return convertView;
    }

    private class ViewHolder {
        ImageView image1;
        TextView title1;
        TextView discription1;
        TextView date1;

    }
}









//package ws.wolfsoft.get_detail;
//
//import android.app.Activity;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//
//import java.util.ArrayList;
//
///**
// * Created by Rp on 3/30/2016.
// */
//public class GridviewAdapter extends BaseAdapter {
//
//    Context context;
//    ArrayList<Beanclass> beans;
//
//    public GridviewAdapter(Context context,ArrayList<Beanclass> beans) {
//        this.beans = beans;
//        this.context = context;
//    }
//
//
//
//
//    @Override
//    public int getCount() {
//        return beans.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return beans.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        ViewHolder viewHolder = null;
//        if (convertView == null){
//
//            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//
//            viewHolder = new ViewHolder();
//
//            convertView = layoutInflater.inflate(R.layout.gridview,null);
//
//
//            viewHolder.image= (ImageView)convertView.findViewById(R.id.image);
//
//
//
//
//            convertView.setTag(viewHolder);
//
//
//        }else {
//
//            viewHolder = (ViewHolder)convertView.getTag();
//
//        }
//
//
//        Beanclass beans = (Beanclass)getItem(position);
//
//        viewHolder.image.setImageResource(beans.getImage());
//        return convertView;
//    }
//
//    private class ViewHolder{
//        ImageView image;
//
//
//    }
//
//}
