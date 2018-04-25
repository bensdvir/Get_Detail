package ws.wolfsoft.get_detail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.facebook.login.LoginManager;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import Communication.Communication;
import DataObjects.Comment;
import DataObjects.User;
import UI_objects.Beanclass;
import UI_objects.ChildAnimationExample;
import UI_objects.ExpandableHeightGridView;
import UI_objects.ExpandableHeightListView;
import UI_objects.GridviewAdapter;
import UI_objects.JayBaseAdapter;
import UI_objects.SliderLayout;


public class ApartmentActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {
    SliderLayout mDemoSlider;



    LinearLayout linear1,showless,review;
    LinearLayout linear2;

    Typeface fonts1,fonts2;
    private ExpandableHeightGridView gridview;
    private ExpandableHeightListView listview;
    private ArrayList<UI_objects.Bean>Bean;
    private JayBaseAdapter baseAdapter;
    private ArrayList<Beanclass> beanclassArrayList;
    private GridviewAdapter gridviewAdapter;
    private String m_Text = "";






   // private int[] IMAGE = {R.drawable.p1, R.drawable.p2, R.drawable.p3};
   private String[] IMAGE = {"https://vignette.wikia.nocookie.net/joke-battles/images/a/ac/Tree.png",
           "https://vignette.wikia.nocookie.net/joke-battles/images/a/ac/Tree.png",
           "https://vignette.wikia.nocookie.net/joke-battles/images/a/ac/Tree.png"};
    private String[] TITLE = {"Best seller", "Dunt think twise for it", "Good product"};
    private String[] RATING = {"4.5 rating", "5 rating", "4 rating"};
    private String[] BY = {"by Kelly","by Emma","by Erik"};


    private int[] IMAGEgrid = {R.drawable.pik1, R.drawable.pik2, R.drawable.pik3, R.drawable.pik4};
    private String[] TITLeGgrid = {"Min 70% off", "Min 50% off", "Min 45% off",  "Min 60% off"};
    private String[] DIscriptiongrid = {"Wrist Watches", "Belts", "Sunglasses","Perfumes"};
    private String[] Dategrid = {"Explore Now!","Grab Now!","Discover now!", "Great Savings!"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        /*SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        */

        final ImageView comment = (ImageView) findViewById(R.id.commentImage);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ApartmentActivity.this);
                builder.setTitle("Enter your comment");

                final EditText input = new EditText(ApartmentActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        Thread t3 = new Thread(new Runnable() {

                            public void run() {
                                HashMap<String, String> header = new HashMap<String, String>();
                                header.put("text", m_Text);
                                header.put("address", getIntent().getExtras().getString("address"));
                                //header.put("userID",getIntent().getExtras().getString("landLordID"));
                                //header.put("userID",LoginActivity.sessionId);
                                header.put("userID", getIntent().getExtras().getString("sessionId"));
                                Communication.makePostRequestGetCode(Communication.ip + "/comment/toAddress", header,null);
                                ApartmentActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(ApartmentActivity.this, "comment published", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //if (temp!= null){

                                //}
                                //ans = response;//
                            }
                        });
                        try {t3.start();t3.join();}catch (Exception e){}
                        //Toast.makeText(ApartmentActivity.this, "This is my Toast message!",
                         //       Toast.LENGTH_LONG).show();
                        //addApartment(m_Text);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }});


        final HashMap<String, String> header = new HashMap<String, String>();
        header.put("commented",  getIntent().getExtras().get("address").toString());
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                List<Comment> response = Communication.makeGetRequestGetList(Communication.ip + "/comment/getAllComments", header, Comment.class);
                listview = (ExpandableHeightListView)findViewById(R.id.listview);
                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                Bean = new ArrayList<UI_objects.Bean>();
                for (int i= 0; i< response.size(); i++){
                    Comment com = response.get(i);
                    //Log.d("hidvir",com.getText());
                    cal.setTimeInMillis(Long.parseLong(com.getTimeStamp().toString()));
                    String date = DateFormat.format("dd-MM-yyyy", cal).toString();

                    UI_objects.Bean bean = new UI_objects.Bean(com.getUserPictureUrl().toString(), com.getText(), date, com.getUserName(),com.getUserToken());
                    //                     //Bean bean = new Bean(0, com.getText(), com.getTimeStamp().toString(), com.getUserName());
                    Bean.add(bean);
                }
                baseAdapter = new JayBaseAdapter(ApartmentActivity.this, Bean) {};
                listview = (ExpandableHeightListView)findViewById(R.id.listview);
                listview.setAdapter(baseAdapter);

                return null;
            }}.execute();


//        ********LISTVIEW***********


        /*listview = (ExpandableHeightListView)findViewById(R.id.listview);


        Bean = new ArrayList<Bean>();

        for (int i= 0; i< TITLE.length; i++){

            Bean bean = new Bean(IMAGE[i], TITLE[i], RATING[i], BY[i]);
            Bean.add(bean);

        }

        baseAdapter = new JayBaseAdapter(ApartmentActivity.this, Bean) {
        };

        listview.setAdapter(baseAdapter);
        */




//        ********GRIDVIEW***********


       /* gridview = (ExpandableHeightGridView)findViewById(R.id.gridview);
        beanclassArrayList= new ArrayList<Beanclass>();

        for (int i= 0; i< IMAGEgrid.length; i++) {

            Beanclass beanclass = new Beanclass(IMAGEgrid[i], TITLeGgrid[i], DIscriptiongrid[i], Dategrid[i]);
            beanclassArrayList.add(beanclass);

        }
        gridviewAdapter = new GridviewAdapter(ApartmentActivity.this, beanclassArrayList);
        gridview.setExpanded(true);

        gridview.setAdapter(gridviewAdapter);
        */


//        ********lInearlayout()***********

//        review = (LinearLayout)findViewById(R.id.review);
//
//
//        for(int i=0;i<IMAGE.length;i++) {
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(2,150);
//            // product.setOrientation(LinearLayout.HORIZONTAL);
//            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(150, 150);
//            int colorInt0 = getResources().getColor(R.color.colorblue);
//
//
//
//
//
//            LinearLayout ll1 = new LinearLayout(this);
//            ll1.setOrientation(LinearLayout.HORIZONTAL);
//
//            LinearLayout ll2 = new LinearLayout(this);
//            ll2.setOrientation(LinearLayout.VERTICAL);
//
//
////            View div1 = new View(this);
////            div1.setLayoutParams(params);
////            div1.setBackgroundColor(colorInt0);
////            div1.setPadding(10, 0, 0, 0);
//
//
//
//            ImageView imageView1 = new ImageView(this);
//            // imageView.setImageResource(R.drawable.ac);
//            imageView1.setImageResource(IMAGE[i]);
//            imageView1.setLayoutParams(params1);
//            imageView1.setPadding(0, 0, 0, 0);
//
//
//            TextView valuename1 = new TextView(this);
//            valuename1.setText(TITLE[i]);
//            valuename1.setId(R.id.title);
//            valuename1.setTextSize(16);
//            valuename1.setPadding(16, 10, 0, 10);
//            valuename1.setTextColor(colorInt0);
//
//            // valuename.setLayoutParams(params);
//
//            TextView valuecoment1 = new TextView(this);
//            valuecoment1.setText(RATING[i]);
//            valuename1.setId(R.id.title);
//            valuecoment1.setTextSize(11);
//            valuecoment1.setPadding(16, 0, 0, 0);
//            valuecoment1.setLineSpacing(5, 1);
//
//
//            TextView valuecoment2 = new TextView(this);
//            valuecoment2.setText(BY[i]);
//            valuename1.setId(R.id.title);
//            valuecoment2.setTextSize(11);
//            valuecoment2.setPadding(16, 10, 0, 10);
//            valuecoment2.setLineSpacing(5, 1);
//
//
//            ll1.addView(imageView1);
//
//            ll2.addView(valuename1);
//            ll2.addView(valuecoment1);
//            ll2.addView(valuecoment2);
////            ll1.addView(div1);
//
//
//            ll1.addView(ll2);
//
//            review.addView(ll1);
//
//        }




//
//        fonts1 =  Typeface.createFromAsset(MainActivity.this.getAssets(),
//                "fonts/Lato-Light.ttf");
//
//
//
//        fonts2 =  Typeface.createFromAsset(MainActivity.this.getAssets(),
//                "fonts/Lato-Regular.ttf");
//
//
//        TextView t5 =(TextView)findViewById(R.id.title);
//        t5.setTypeface(fonts1);












//                ***********viewmore**********

        linear1 = (LinearLayout)findViewById(R.id.linear1);
        showless = (LinearLayout)findViewById(R.id.showless);

        linear2 = (LinearLayout)findViewById(R.id.linear2);

        linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                linear2.setVisibility(View.VISIBLE);
                linear1.setVisibility(View.GONE);

            }
        });

        showless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                linear2.setVisibility(View.GONE);
                linear1.setVisibility(View.VISIBLE);


            }
        });

//         ********Slider*********

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String,String> file_maps = new HashMap<String, String>();
        file_maps.put("1", getIntent().getExtras().get("image").toString());
        //file_maps.put("2",R.drawable.rabiakiva2);


        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    //  .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .setOnSliderClickListener(this);


            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new ChildAnimationExample());
        mDemoSlider.setDuration(4000);
        //mDemoSlider.addOnPageChangeListener();

        final ImageView imageView2 = (ImageView) findViewById(R.id.toLandlordImage);
        final User[] user = new User[1];
        Thread t = new Thread(new Runnable() {

            public void run() {
                final String finalId = getIntent().getExtras().get("landLordID").toString();
                HashMap<String, String> header = new HashMap<String, String>();
                header.put("token", finalId);
                User response = Communication.makeGetRequest(Communication.ip + "/user/getByToken", header, User.class);
                user[0] = response;
                //ans = response;//
            }
        });
        try {t.start();t.join();}catch (Exception e){}

        new AsyncTask<Void, Void, Void>() {
            Bitmap bmp =  null;
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InputStream in = new URL(user[0].getImage().toString()).openStream();
                    bmp = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    // log error
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (bmp != null)
                    imageView2.setImageBitmap(bmp);
            }

        }.execute();
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
                //Bundle b = new Bundle();
                //b.putString("idFacebook", LoginActivity.sessionId);
                Bundle b = getIntent().getExtras();

                b.putString("idFacebook",getIntent().getExtras().getString("landLordID"));
                intent.putExtras(b);
                startActivity(intent);
            }});
        final String url = (getIntent().getExtras().containsKey("profile_pic")) ?  getIntent().getExtras().get("profile_pic").toString() : "http://profile.actionsprout.com/default.jpeg";


        //Change to real landlord

        new AsyncTask<Void, Void, Void>() {
            Bitmap bmp =  null;
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InputStream in = new URL(url).openStream();
                    bmp = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    // log error
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (bmp != null)
                    imageView2.setImageBitmap(bmp);
            }

        }.execute();
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
                //Bundle facebookData = getIntent().getExtras();
                Bundle b = new Bundle();
                b.putString("idFacebook",getIntent().getExtras().get("landLordID").toString());
                intent.putExtras(b);
                //Should be of the specific user
                startActivity(intent);
            }
        });


        android.support.v7.widget.Toolbar tb = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        LinearLayout profileLayout  = (LinearLayout) tb.findViewById(R.id.profileLayout);
        LinearLayout logoutLayout  = (LinearLayout) tb.findViewById(R.id.logoutLayout);
        LinearLayout searchLayout  = (LinearLayout) tb.findViewById(R.id.searchLayout);

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
                //Bundle b = new Bundle();
                //b.putString("idFacebook", LoginActivity.sessionId);
                intent.putExtras(getIntent().getExtras());
                startActivity(intent);
            }
        });

        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                if(getIntent().getExtras().containsKey("profile_pic"))  {
                    LoginManager.getInstance().logOut();
                }
                startActivity(intent);
            }
        });


        customfonts.MyTextView address = (customfonts.MyTextView) findViewById(R.id.address);
        address.setText(getIntent().getExtras().get("address").toString());
        customfonts.MyTextView price = (customfonts.MyTextView) findViewById(R.id.price);
        price.setText(getIntent().getExtras().get("price").toString());

        customfonts.MyTextView elevator = (customfonts.MyTextView) findViewById(R.id.elevatorValue);
        elevator.setText(getIntent().getExtras().get("elevator").toString());

        customfonts.MyTextView description = (customfonts.MyTextView) findViewById(R.id.descriptionValue);
        description.setText(getIntent().getExtras().get("description").toString());

        customfonts.MyTextView constructionYear = (customfonts.MyTextView) findViewById(R.id.constructionValue);
        constructionYear.setText(getIntent().getExtras().get("constructionYear").toString());

        customfonts.MyTextView wareHouse = (customfonts.MyTextView) findViewById(R.id.warehouseValue);
        wareHouse.setText(getIntent().getExtras().get("wareHouse").toString());

        customfonts.MyTextView parking = (customfonts.MyTextView) findViewById(R.id.parkingValue);
        parking.setText(getIntent().getExtras().get("parking").toString());

        customfonts.MyTextView size = (customfonts.MyTextView) findViewById(R.id.size2);
        size.setText(getIntent().getExtras().get("size").toString());

        customfonts.MyTextView numRooms = (customfonts.MyTextView) findViewById(R.id.roomsNumValue);
        numRooms.setText(getIntent().getExtras().get("numRooms").toString());

        customfonts.MyTextView des = (customfonts.MyTextView) findViewById(R.id.descriptionValue);
        des.setText(getIntent().getExtras().get("description").toString());

        customfonts.MyTextView rank = (customfonts.MyTextView) findViewById(R.id.est);
        rank.setText("Ranking: " +getIntent().getExtras().get("averageRank").toString());

        if (HomeActivity.aparttmentsRatings.get( getIntent().getExtras().getString("address"))!=null) {
            ratingBar.setRating(HomeActivity.aparttmentsRatings.get( getIntent().getExtras().getString("address")));
            ratingBar.setClickable(false);
            ratingBar.setActivated(false);
            ratingBar.setEnabled(false);
        }

        //ratingBar.setNumStars(Integer.parseInt(getIntent().getExtras().get("averageRank").toString()));

        /*ratingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingBar bar = (RatingBar) v;
                bar.setClickable(false);
            }
        });
        */

        /*ratingBar.setOnTouchListener(new View.OnTouchListener() {
                                         @Override
                                         public boolean onTouch(View v, MotionEvent event) {
                                             //if (event.getAction() == MotionEvent.ACTION_UP) {
                                              //   // TODO perform your action here
                                             //}
                                             RatingBar bar = (RatingBar) v;
                                             bar.setClickable(false);
                                             bar.setRating(bar.getRating());
                                             return true;
                                         }
        });
        */
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                HomeActivity.aparttmentsRatings.put( getIntent().getExtras().getString("address"),new Float (ratingBar.getRating()));
                ratingBar.setClickable(false);
                ratingBar.setActivated(false);
                ratingBar.setEnabled(false);

            }
        });

        //Thread t2 = new Thread(new Runnable() {

          //public void run() {




       //             baseAdapter = new JayBaseAdapter(ApartmentActivity.this, Bean) {};
         //           listview.setAdapter(baseAdapter);

//                    };

                    //ans = response;//
            }
        //});
        //try {t2.start();t2.join();}catch (Exception e){}


    //}

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }
}
