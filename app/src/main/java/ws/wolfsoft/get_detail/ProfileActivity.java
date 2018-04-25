package ws.wolfsoft.get_detail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class ProfileActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {
    SliderLayout mDemoSlider;


    LinearLayout linear1, showless, review;
    LinearLayout linear2;

    Typeface fonts1, fonts2;
    private ExpandableHeightGridView gridview;
    private ExpandableHeightListView listview;
    private ArrayList<UI_objects.Bean> Bean;
    private JayBaseAdapter baseAdapter;
    private ArrayList<Beanclass> beanclassArrayList;
    private GridviewAdapter gridviewAdapter;
    private String m_Text = "";
    private User ans = null;


    //private int[] IMAGE = {R.drawable.p1, R.drawable.p2, R.drawable.p3};
    private String[] IMAGE = {"https://vignette.wikia.nocookie.net/joke-battles/images/a/ac/Tree.png",
           "https://vignette.wikia.nocookie.net/joke-battles/images/a/ac/Tree.png",
          "https://vignette.wikia.nocookie.net/joke-battles/images/a/ac/Tree.png"};

    private String[] TITLE = {"Best seller", "Dunt think twise for it", "Good product"};
    private String[] RATING = {"4.5 rating", "5 rating", "4 rating"};
    private String[] BY = {"by Kelly", "by Emma", "by Erik"};


    private int[] IMAGEgrid = {R.drawable.pik1, R.drawable.pik2, R.drawable.pik3, R.drawable.pik4};
    private String[] TITLeGgrid = {"Min 70% off", "Min 50% off", "Min 45% off", "Min 60% off"};
    private String[] DIscriptiongrid = {"Wrist Watches", "Belts", "Sunglasses", "Perfumes"};
    private String[] Dategrid = {"Explore Now!", "Grab Now!", "Discover now!", "Great Savings!"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        String userID = "";
        if (getIntent().getExtras().containsKey("landLordID")) {
            userID = getIntent().getExtras().getString("LandLordID");
        } else {
            userID = getIntent().getExtras().getString("sessionId");
        }
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
     //   SwipeRefreshLayout mySwipeRefreshLayout = (SwipeRefreshLayout)this.findViewById(R.id.swipeContainer);

       // mySwipeRefreshLayout.setOnRefreshListener(
         //       new SwipeRefreshLayout.OnRefreshListener() {
           //         @Override
             //       public void onRefresh() {
               //         finish();
                 //       startActivity(getIntent());
                   // }
                //}
        //);
        final String finalUserID = userID;
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(final RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                if (finalUserID.equals(LoginActivity.sessionId)) {

                    Thread t3 = new Thread(new Runnable() {

                        public void run() {
                            ProfileActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(ProfileActivity.this, "You can't rate yourself", Toast.LENGTH_SHORT).show();
                                    ratingBar.setRating(0);
                                    return;
                                }
                            });

                        }
                    });
                    try {
                        t3.start();
                        t3.join();
                    } catch (Exception e) {
                    }
                }
                HomeActivity.usersRatings.put(finalUserID,ratingBar.getRating());
                ratingBar.setClickable(false);
                ratingBar.setActivated(false);
                ratingBar.setEnabled(false);

            }
        });
        ImageView comment = (ImageView) findViewById(R.id.commentImage);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Enter your comment");

                final EditText input = new EditText(ProfileActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle b = getIntent().getExtras();
                        String user = "";
                        if (b.containsKey("landLordID")) {
                            user = getIntent().getExtras().getString("LandLordID");
                        } else {
                            user = getIntent().getExtras().getString("sessionId");

                        }
                        if (user.equals(LoginActivity.sessionId)) {

                            Thread t3 = new Thread(new Runnable() {

                                public void run() {
                                    ProfileActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(ProfileActivity.this, "You can't comment yourself", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            });
                            try {
                                t3.start();
                                t3.join();
                            } catch (Exception e) {
                            }
                        } else {
                            m_Text = input.getText().toString();
                            Thread t3 = new Thread(new Runnable() {

                                public void run() {
                                    HashMap<String, String> header = new HashMap<String, String>();
                                    header.put("text", m_Text);
                                    header.put("userTo", getIntent().getExtras().getString("LandlordID"));
                                    //header.put("userID",getIntent().getExtras().getString("landLordID"));
                                    //header.put("userFrom", LoginActivity.sessionId);
                                    header.put("userID",getIntent().getExtras().getString("sessionId"));
                                    Communication.makePostRequestGetCode(Communication.ip + "/comment/toUser", header, null);
                                    ProfileActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(ProfileActivity.this, "comment published", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    //if (temp!= null){

                                    //}
                                    //ans = response;//
                                }
                            });
                            try {
                                t3.start();
                                t3.join();
                            } catch (Exception e) {
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });


//        ********LISTVIEW***********


        final HashMap<String, String> header = new HashMap<String, String>();
        header.put("commented",  userID);
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
                baseAdapter = new JayBaseAdapter(ProfileActivity.this, Bean) {};
                listview = (ExpandableHeightListView)findViewById(R.id.listview);
                listview.setAdapter(baseAdapter);

                return null;
            }}.execute();


        mDemoSlider = (SliderLayout) findViewById(R.id.slider);



        android.support.v7.widget.Toolbar tb = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        LinearLayout profileLayout  = (LinearLayout) tb.findViewById(R.id.profileLayout);
        LinearLayout logoutLayout  = (LinearLayout) tb.findViewById(R.id.logoutLayout);
        LinearLayout searchLayout  = (LinearLayout) tb.findViewById(R.id.searchLayout);

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
                //Bundle facebookData = getIntent().getExtras();
                Bundle b = new Bundle();
                b.putString("idFacebook", LoginActivity.sessionId);
                b.putString("sessionId", getIntent().getExtras().getString("sessionId"));
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                if (getIntent().getExtras().containsKey("sessionId")) {
                    LoginManager.getInstance().logOut();
                }
                startActivity(intent);
            }
        });


        if (HomeActivity.usersRatings.containsKey(userID)){
            ratingBar.setRating(HomeActivity.usersRatings.get(userID));
            ratingBar.setClickable(false);
            ratingBar.setActivated(false);
            ratingBar.setEnabled(false);
        }


        //Button b = findViewById(R.id.anonymus_button);
        //b.callOnClick();
        doInit(null);

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    public Bundle userToBundle(User u) {
        Bundle b = new Bundle();
        b.putString("profile_pic", u.getImage());
        b.putString("first_name", u.getFirstName());
        b.putString("last_name", u.getLastName());
        b.putString("email", u.getEmail());
        b.putString("gender", u.getGender());
        b.putDouble("avgRankLandLoard", u.getAvgRankLandLoard());
        b.putDouble("avgRankRanker", u.getAvgRankRanker());
        return b;
    }


    public User getUserFromServer(final String ID) {
        new AsyncTask<Void, Void, User>() {
            @Override
            protected User doInBackground(Void... params) {
                HashMap<String, String> header = new HashMap<String, String>();
                header.put("token", ID);
                User response = Communication.makeGetRequest(Communication.ip + "/user/getByToken", header, User.class);
                //ans = response;//
                return response;
            }
        }.execute();
        return null;
    }

    public void doInit(View v) {

        HashMap<String, String> file_maps = new HashMap<String, String>();
        final Bundle data2 = getIntent().getExtras();
        String id = "";
        boolean isAnonymus = (!data2.containsKey("sessionId"));
        Bundle data = null;
        final User[] user = new User[1];
        if (!isAnonymus) {
            id = data2.get("sessionId").toString();
            //user = getUserFromServer(id);
            final String finalId = id;
            Thread t = new Thread(new Runnable() {

                public void run() {
                    HashMap<String, String> header = new HashMap<String, String>();
                    header.put("token", finalId);
                    User response = Communication.makeGetRequest(Communication.ip + "/user/getByToken", header, User.class);
                    //ans = response;//
                    user[0] = response;
                }
            });
            try {t.start();t.join();}catch (Exception e){}


            while (user[0] == null) {
            }
        }



        //new ShowTitle().execute("http://info.radiostyle.ru/inc/getinfo.php?getcurentsong=20383&mount=lezgifm");


        if (!isAnonymus) {
            //final Object pic = user.get("profile_pic");
            file_maps.put("1", user[0].getImage());
        } else {
            file_maps.put("1", "http://profile.actionsprout.com/default.jpeg");
        }


        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(ProfileActivity.this);
            // initialize a SliderLayout
            textSliderView
                    //  .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .setOnSliderClickListener(ProfileActivity.this);


            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new ChildAnimationExample());
        mDemoSlider.setDuration(4000);

        customfonts.MyTextView name = (customfonts.MyTextView) findViewById(R.id.name);
        customfonts.MyTextView emailValue = (customfonts.MyTextView) findViewById(R.id.emailValue);
        customfonts.MyTextView genderValue = (customfonts.MyTextView) findViewById(R.id.genderValue);

        if (!isAnonymus) {
            name.setText(user[0].getFirstName() + " " + user[0].getLastName());
            emailValue.setText(user[0].getEmail());
            genderValue.setText(user[0].getGender());
        } else {
            name.setText("Anonymus");
            emailValue.setText("Unknown");
            genderValue.setText("Unknown");
        }
    }


}