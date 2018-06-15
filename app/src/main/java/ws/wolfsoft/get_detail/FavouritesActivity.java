package ws.wolfsoft.get_detail;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.facebook.login.LoginManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import Communication.Communication;
import DataObjects.Apartment;
import DataObjects.User;
import UI_objects.ChildAnimationExample;
import UI_objects.SliderLayout;

public class FavouritesActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener{
    SliderLayout mDemoSlider;
    public HashMap<String,Bundle> apartments = new HashMap<>();
    Boolean isReady;
    Boolean emptyFavs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        TextView address = (TextView) findViewById(R.id.address);
        isReady = false;
        emptyFavs = false;
        Button deleteButton = (Button) findViewById(R.id.delete_button);
        deleteButton.setVisibility (View.INVISIBLE);
        deleteButton.setClickable(false);


        FavouritesActivity.this.runOnUiThread(new Runnable() {
            public void run() {

                android.support.v7.widget.Toolbar tb = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
                LinearLayout profileLayout = (LinearLayout) tb.findViewById(R.id.profileLayout);
                LinearLayout logoutLayout = (LinearLayout) tb.findViewById(R.id.logoutLayout);
                LinearLayout searchLayout = (LinearLayout) tb.findViewById(R.id.searchLayout);
                searchLayout.setVisibility(View.INVISIBLE);
                searchLayout.setVisibility(View.GONE);

                profileLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                Intent intent = new Intent(FavouritesActivity.this, ProfileActivity.class);
                                HashMap<String, String> header = new HashMap<String, String>();
                                header.put("token", LoginActivity.sessionId);
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
                                b.putString("token", user.getToken());
                                b.putString("isProfile", "yes");
                                b.putString("sessionId", LoginActivity.sessionId);
                                intent.putExtras(b);
                                startActivity(intent);

                                try {
                                    startActivity(intent);
                                } catch (Exception e) {
                                }
                                return null;
                            }
                        }.execute();
                    }
                });

                logoutLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                        if (LoginActivity.sessionId != null) {
                            LoginManager.getInstance().logOut();
                        }
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                        }
                    }
                });
            }
        });



       new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                HashMap<String,String> header = new HashMap<String,String>();
                HashMap<String, File> file_maps = new HashMap<String, File>();

                header.put("token",LoginActivity.sessionId);
                List<Apartment> response = Communication.makeGetRequestGetList(Communication.ip+"/user/wishList", header, Apartment.class);
                int i =1;
                for (Apartment ap :response){
                    apartments.put(ap.getAddress(),apartmentToBundle(ap));
                    HomeActivity.apartmentsImages.put(ap.getAddress(), ap.getImage());

                    File f = new File((getApplicationContext().getFileStreamPath(ap.getAddress()+".jpg")
                            .getPath()) );;
                    byte[] bitmapdata = ap.getImage();
                    //Convert bitmap to byte array
                    //byte[] bitmapdata= HomeActivity.tmpImage;
                    //write the bytes in file
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(f);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fos.write(bitmapdata);
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    file_maps.put(""+i, f);
                    i+=1;
                }



                mDemoSlider = (SliderLayout) findViewById(R.id.slider);


                /////////////////////////////
                FavouritesActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        for (String name : file_maps.keySet()) {
                            TextSliderView textSliderView = new TextSliderView(FavouritesActivity.this);
                            // initialize a SliderLayout
                            textSliderView
                                    //  .description(name)
                                    .image(file_maps.get(name))
                                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                                    .setOnSliderClickListener(FavouritesActivity.this);

                            textSliderView.description( file_maps.get(name).getName());


                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle().putString("extra", name);

                            mDemoSlider.addSlider(textSliderView);
                        }
                        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
                        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        mDemoSlider.setCustomAnimation(new ChildAnimationExample());
                        mDemoSlider.setDuration(4000);
                    }
                });
                if (response.isEmpty()){
                    emptyFavs = true;
                    FavouritesActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            address.setText("No apartments to show :(");
                        }
                    });

                }
                else{

                    FavouritesActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            deleteButton.setVisibility (View.VISIBLE);
                            deleteButton.setClickable(true);
                        }
                    });
                }

                isReady =true;
                return null;
            }
        }.execute();


        Thread thread = new Thread() {


            @Override
            public void run() {
                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                try {

                    while (!isReady){
                        Thread.sleep(100);
                    }
                    if(emptyFavs){
                        address.setText("No apartments to show :(");
                        return;
                    }
                    while (true) {
                        Thread.sleep(100);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                BaseSliderView bs = mDemoSlider.getCurrentSlider();
                                String ad = bs.getDescription().replace(".jpg","");
                                Bundle b = apartments.get(ad);
                                address.setText(b.get("address").toString());

                                // update TextView here!
                            }
                        });
                    }
                } catch (Exception e) {
                }
            }
        };

        thread.start();



        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDemoSlider = (SliderLayout) findViewById(R.id.slider);
                BaseSliderView slider = mDemoSlider.getCurrentSlider();
                String ad = slider.getDescription().replace(".jpg","");
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        HashMap<String, String> header = new HashMap<String, String>();
                        header.put("token", LoginActivity.sessionId);
                        header.put("address", ad);
                        Boolean response = Communication.makePostRequest(Communication.ip + "/user/deleteFromWishList", header, null ,Boolean.class);
                        if (response){
                            FavouritesActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(FavouritesActivity.this, "apartment deleted successfully", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });
                            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                            finish();
                            startActivity(getIntent());
                        }
                        else {
                            FavouritesActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(FavouritesActivity.this, "some bug", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });

                        }
                        return null;
                    }
                }.execute();


            }
        });
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        Intent intent = new Intent(getBaseContext(), ApartmentActivity.class);
        String ad = slider.getDescription().replace(".jpg","");
        Bundle b = apartments.get(ad);
        if(getIntent().getExtras().containsKey("sessionId"))
            b.putString("sessionId",getIntent().getExtras().get("sessionId").toString());
        intent.putExtras(b);
        try {
            startActivity(intent);}
        catch (Exception e){}

    }

    private Bundle apartmentToBundle(Apartment ap){
        Bundle b = new Bundle();
        b.putString("address", ap.getAddress());
        b.putInt("price", ap.getPrice().intValue());
        b.putInt("floor", ap.getFloor().intValue());
        b.putBoolean("elevator", ap.getElevator());
        b.putBoolean("wareHouse", ap.getWareHouse());
        b.putBoolean("parking", ap.getParking());
        b.putInt("constructionYear", ap.getConstructionYear().intValue());
        b.putString("description", ap.getDescription());
        b.putInt("numToilets", ap.getNumToilet().intValue());
        b.putInt("numRooms",ap.getNumRooms().intValue());
        b.putDouble("size",ap.getSize());
        b.putDouble("averageRank", ap.getAverageRank());
        //b.putString("image",ap.getImage());
        b.putString("landLordID",ap.getLandLordID());
        b.putString("isRent",ap.getIsRent().toString());

        return b;
    }


}
