package UI_objects;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import Communication.Communication;
import DataObjects.User;
import ws.wolfsoft.get_detail.ProfileActivity;
import ws.wolfsoft.get_detail.R;


public class JayBaseAdapter extends BaseAdapter {

    Context context;

    ArrayList<Bean>bean;
    Typeface fonts1,fonts2;





    public JayBaseAdapter(Context context, ArrayList<Bean> bean) {


        this.context = context;
        this.bean = bean;
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

        fonts1 =  Typeface.createFromAsset(context.getAssets(),
                "fonts/Lato-Light.ttf");

        fonts2 = Typeface.createFromAsset(context.getAssets(),
                "fonts/Lato-Regular.ttf");

        ViewHolder viewHolder = null;

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list,null);

            viewHolder = new ViewHolder();

            viewHolder.image = (ImageView)convertView.findViewById(R.id.image);
            viewHolder.title = (TextView)convertView.findViewById(R.id.title);
            viewHolder.discription = (TextView)convertView.findViewById(R.id.description);
            viewHolder.date = (TextView)convertView.findViewById(R.id.date);




            viewHolder.title.setTypeface(fonts2);
            viewHolder.discription.setTypeface(fonts1);

            viewHolder.date.setTypeface(fonts2);

            convertView.setTag(viewHolder);


        }else {

            viewHolder = (ViewHolder)convertView.getTag();
        }







         final Bean bean = (Bean)getItem(position);

        //viewHolder.image.setImageResource(bean.getImage());


        final ViewHolder finalViewHolder = viewHolder;
        new AsyncTask<Void, Void, Void>() {
            Bitmap bmp =  null;
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InputStream in = new URL(bean.getImage().toString()).openStream();
                    bmp = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    // log error
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (bmp != null) {

                    finalViewHolder.image.setImageBitmap(bmp);
                }
            }

        }.execute();


        viewHolder = finalViewHolder;
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        Intent intent = new Intent (context, ProfileActivity.class);
                        HashMap<String, String> header = new HashMap<String, String>();
                        header.put("token", bean.userID);
                        User response = Communication.makeGetRequest(Communication.ip + "/user/getByToken", header, User.class);
                        //Bundle b = userToBundle(response);
                        User user = response;
                        Bundle b = new Bundle();
                        b.putString("email", user.getEmail());
                        b.putString("firstName", user.getFirstName());
                        b.putString("gender", user.getGender());
                        b.putString("image", user.getImage());
                        b.putString("rank", user.getAvgRankRanker().toString());
                        b.putString("lastName", user.getLastName());
                        b.putString("LandLordID", user.getToken());
                        b.putString("notProfile", "hi");
                        intent.putExtras(b);
                        context.startActivity(intent);
                        return null;
                    }
                }.execute();
            }});


        viewHolder.title.setText(bean.getTitle());
        viewHolder.discription.setText(bean.getDiscription());
        viewHolder.date.setText(bean.getDate());





        /*Bean bean = (Bean)getItem(position);
        viewHolder.image.setImageResource(bean.getImage());
        viewHolder.title.setText(bean.getTitle());
        viewHolder.discription.setText(bean.getDiscription());
        viewHolder.date.setText(bean.getDate());
        */








        return convertView;
    }

    public class ViewHolder{
        ImageView image;
        TextView title;
        TextView discription;
        TextView date;





    }
}




