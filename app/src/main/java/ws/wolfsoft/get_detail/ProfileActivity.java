package ws.wolfsoft.get_detail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String userID = "";
            if (getIntent().getExtras().containsKey("notProfile") || (!getIntent().getExtras().containsKey("isProfile"))) {
                userID = getIntent().getExtras().getString("LandLordID");
            } else
                {
                    if(!LoginActivity.isAno) {
                        userID = LoginActivity.sessionId;
                    }
        }


        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        final String finalUserID = userID;
        String finalUserID4 = userID;
        String finalUserID6 = userID;
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(final RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                if(LoginActivity.isAno){
                    ProfileActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ProfileActivity.this, "You can't rate", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                    return;
                }
                if(LoginActivity.sessionId == null||  HomeActivity.usersRatings.containsKey(finalUserID6)){
                    return;
                }
                 Boolean check = true;
                if (finalUserID.equals(LoginActivity.sessionId)) {
                    check = false;


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
                else {
                    HomeActivity.usersRatings.put(finalUserID, ratingBar.getRating());
                    //TODO: add rating in server
                    ratingBar.setClickable(false);
                    ratingBar.setActivated(false);
                    ratingBar.setEnabled(false);
                }
                if (!check){
                    return;
                }

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        HashMap<String, String> header = new HashMap<String, String>();
                        header.put("rank", String.valueOf(rating));
                        header.put("userId", finalUserID4);
                        //header.put("userID",getIntent().getExtras().getString("landLordID"));
                        //header.put("userID",LoginActivity.sessionId);
                        Communication.makePostRequestGetCode(Communication.ip + "/rank/user", header, null);
                        return null;
                    }
                }.execute();
            }
        });
        ImageView comment = (ImageView) findViewById(R.id.commentImage);
        String finalUserID3 = userID;
        String finalUserID1 = userID;
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

                        if(LoginActivity.isAno){
                            ProfileActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(ProfileActivity.this, "You can't comment", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });
                            return;
                        }
                        if(LoginActivity.sessionId == null){
                            return;
                        }
                        Bundle b = getIntent().getExtras();
                        if (finalUserID3.equals(LoginActivity.sessionId)) {

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
                        }
                        else {
                            if (LoginActivity.sessionId == null) {
                                Thread t3 = new Thread(new Runnable() {

                                    public void run() {
                                        ProfileActivity.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(ProfileActivity.this, "You are not a valid user", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                });

                            } else {
                                m_Text = input.getText().toString();
                                new AsyncTask<Void, Void, Void>() {
                                    @Override
                                    protected Void doInBackground(Void... params) {
                                        HashMap<String, String> header = new HashMap<String, String>();
                                        header.put("text", m_Text);
                                        header.put("userTo", finalUserID3);
                                        //header.put("userID",getIntent().getExtras().getString("landLordID"));
                                        //header.put("userFrom", LoginActivity.sessionId);
                                        header.put("userFrom", LoginActivity.sessionId);
                                        Communication.makePostRequestGetCode(Communication.ip + "/comment/toUser", header, null);
                                        Handler mainHandler = new Handler(ProfileActivity.this.getMainLooper());

                                        Runnable myRunnable = new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(ProfileActivity.this, "comment published", Toast.LENGTH_SHORT).show();
                                            } // This is your code
                                        };
                                        mainHandler.post(myRunnable);
                                        //if (temp!= null){

                                        //}
                                        //ans = response;//
                                        return null;
                                    }
                                }.execute();
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




        String finalUserID2 = userID;
        String finalUserID5 = userID;
        String finalUserID7 = userID;
        String finalUserID8 = userID;
        String finalUserID9 = userID;
        String finalUserID10 = userID;
        String finalUserID11 = userID;
        ProfileActivity.this.runOnUiThread(new Runnable() {
            public void run() {



                android.support.v7.widget.Toolbar tb = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
                LinearLayout profileLayout = (LinearLayout) tb.findViewById(R.id.profileLayout);
                LinearLayout logoutLayout = (LinearLayout) tb.findViewById(R.id.logoutLayout);
                LinearLayout searchLayout = (LinearLayout) tb.findViewById(R.id.searchLayout);

                    ImageView se = (ImageView) tb.findViewById(R.id.search);
                    TextView seText = (TextView) tb.findViewById(R.id.searchText);
                    seText.setText("add new");
                    String uri = "@drawable/plus";  // where myresource (without the extension) is the file
                    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                    Drawable res = getResources().getDrawable(imageResource);
                    se.setImageDrawable(res);


                profileLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getBaseContext(), ProfileActivity.class);

                        if(LoginActivity.sessionId!=null) {
                            // b.putString("idFacebook", LoginActivity.sessionId);
                            try {
                                new AsyncTask<Void, Void, Void>() {
                                    @Override
                                    protected Void doInBackground(Void... params) {
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
                                        b.putString("lastName", user.getLastName());
                                        b.putString("rank", user.getAvgRankRanker().toString());
                                        b.putString("token", user.getToken());
                                        b.putString("isProfile", "yes");
                                        b.putString("sessionId", LoginActivity.sessionId);
                                        intent.putExtras(b);
                                        startActivity(intent);
                                        return null;
                                    }
                                }.execute();
                            } catch (Exception e) {
                                ProfileActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(ProfileActivity.this, "Some bug", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                        else {
                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... params) {
                                    startActivity(intent);
                                    return null;
                                }
                            }.execute();

                    }}
                });

                searchLayout.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick (View view){

                        if(LoginActivity.isAno){
                            ProfileActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(ProfileActivity.this, "You can't add assets", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });
                            return;
                        }

                        Intent intent = new Intent(getBaseContext(), AddApartmentActivity.class);
                        try {
                            startActivity(intent);}
                        catch (Exception e){}            }
                });

                LinearLayout toFavs = (LinearLayout) findViewById(R.id.myFavs);
                LinearLayout toChat = (LinearLayout) findViewById(R.id.chatLayout);

                LinearLayout blockLayout = (LinearLayout) findViewById(R.id.blockLayout);


                blockLayout.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {

                        if (LoginActivity.isAno) {
                            ProfileActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(ProfileActivity.this, "You can't block users", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });
                            return;
                        }
                        if (finalUserID10.equals(LoginActivity.sessionId)) {
                            Thread t3 = new Thread(new Runnable() {

                                public void run() {
                                    ProfileActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(ProfileActivity.this, "You can't block yourself", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    });
                                    return;
                                }
                            });
                            try {
                                t3.start();
                                t3.join();
                            } catch (Exception e) {
                            }
                        }
                        else {


                            final CharSequence[] items = {" block for posts "," block chat messages "};
// arraylist to keep the selected items
                            final ArrayList seletedItems=new ArrayList();

                            AlertDialog dialog = new AlertDialog.Builder(ProfileActivity.this)
                                    .setTitle("Select blocking option")
                                    .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                                            if (isChecked) {
                                                // If the user checked the item, add it to the selected items
                                                seletedItems.add(indexSelected);
                                            } else if (seletedItems.contains(indexSelected)) {
                                                // Else, if the item is already in the array, remove it
                                                seletedItems.remove(Integer.valueOf(indexSelected));
                                            }
                                        }
                                    }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            boolean conPosts = seletedItems.contains(new Integer(0));
                                            boolean conChat= seletedItems.contains(new Integer(1));
                                            if (conPosts){

                                                new AsyncTask<Void, Void, Void>() {
                                                    @Override
                                                    protected Void doInBackground(Void... params) {

                                                        HashMap<String, String> header = new HashMap<String, String>();
                                                        header.put("myToken", LoginActivity.sessionId);
                                                        header.put("blockToken", finalUserID11);
                                                        Boolean response = Communication.makePostRequest(Communication.ip + "/user/blockApartmentUser", header, null,Boolean.class);
                                                        if (response) {
                                                            ProfileActivity.this.runOnUiThread(new Runnable() {
                                                                public void run() {
                                                                    Toast.makeText(ProfileActivity.this, "user blocked for posts successfully :)", Toast.LENGTH_SHORT).show();
                                                                    return;
                                                                }
                                                            });
                                                        }
                                                        else {
                                                            ProfileActivity.this.runOnUiThread(new Runnable() {
                                                                public void run() {
                                                                    Toast.makeText(ProfileActivity.this, "user address already exists in posts blocked list ", Toast.LENGTH_SHORT).show();
                                                                    return;
                                                                }
                                                            });
                                                        }


                                                        return null;
                                                    }
                                                }.execute();


                                            }
                                            if (conChat){
                                                new AsyncTask<Void, Void, Void>() {
                                                    @Override
                                                    protected Void doInBackground(Void... params) {

                                                        HashMap<String, String> header = new HashMap<String, String>();
                                                        header.put("myToken", LoginActivity.sessionId);
                                                        header.put("blockToken", finalUserID11);
                                                        Boolean response = Communication.makePostRequest(Communication.ip + "/user/blockChatUser", header, null,Boolean.class);
                                                        if (response) {
                                                            ProfileActivity.this.runOnUiThread(new Runnable() {
                                                                public void run() {
                                                                    Toast.makeText(ProfileActivity.this, "user blocked for chat successfully :)", Toast.LENGTH_SHORT).show();
                                                                    return;
                                                                }
                                                            });
                                                        }
                                                        else {
                                                            ProfileActivity.this.runOnUiThread(new Runnable() {
                                                                public void run() {
                                                                    Toast.makeText(ProfileActivity.this, "user address already exists in chat blocked list ", Toast.LENGTH_SHORT).show();
                                                                    return;
                                                                }
                                                            });
                                                        }


                                                        return null;
                                                    }
                                                }.execute();
                                            }

                                            //  Your code when user clicked on OK
                                            //  You can write the code  to save the selected item here
                                        }
                                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            //  Your code when user clicked on Cancel
                                        }
                                    }).create();
                            dialog.show();

                        }

                    }
                });

                toChat.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick (View view) {

                        if (LoginActivity.isAno) {
                            ProfileActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(ProfileActivity.this, "You can't access chat", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });
                            return;
                        }
                        if (finalUserID9.equals(LoginActivity.sessionId)) {
                            Thread t3 = new Thread(new Runnable() {

                                public void run() {
                                    ProfileActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(ProfileActivity.this, "You can't chat yourself", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    });
                                    return;
                                }
                            });
                            try {
                                t3.start();
                                t3.join();
                            } catch (Exception e) {
                            }
                        }
                       else {

                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... params) {
                                    HashMap<String, String> header = new HashMap<String, String>();
                                    header.put("myToken", LoginActivity.sessionId);
                                    header.put("blockToken", finalUserID11);
                                    Boolean response = Communication.makePostRequest(Communication.ip + "/user/isBlockForChat", header, null,Boolean.class);
                                    if (response){
                                        ProfileActivity.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(ProfileActivity.this, "You blocked this user", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        });
                                        return null;
                                    }
                                    else{
                                        header = new HashMap<String, String>();
                                        header.put("myToken", finalUserID11);
                                        header.put("blockToken", LoginActivity.sessionId);
                                        Boolean response2 = Communication.makePostRequest(Communication.ip + "/user/isBlockForChat", header, null,Boolean.class);
                                        if (response2){
                                            ProfileActivity.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Toast.makeText(ProfileActivity.this, "You are blocked by this user", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                            });
                                            return null;
                                        }
                                        else{
                                            Intent intent = new Intent(getBaseContext(), Chat.class);
                                            UserDetails.chatWith = getIntent().getExtras().get("firstName").toString() + " " + getIntent().getExtras().get("lastName").toString();
                                            startActivity(intent);
                                        }

                                    }
                                    return null;
                                }
                                }.execute();


                            // ConversationActivity.show(ProfileActivity.this);

                        }
                    }


                        });









                //mDemoSlider.addOnPageChangeListener();
                toFavs.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick (View view){
                        if(LoginActivity.isAno){
                            ProfileActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(ProfileActivity.this, "You can't have/access favourites", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });
                            return;

                        }
                        if (finalUserID7.equals(LoginActivity.sessionId)){

                        Intent intent = new Intent(getBaseContext(), FavouritesActivity.class);
                        try {
                            startActivity(intent);}
                        catch (Exception e){}
                        }
                        else{
                            ProfileActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(ProfileActivity.this, "You can't access", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });
                            return;
                        }
                }});


                logoutLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                        if (LoginActivity.sessionId!=null) {
                            LoginManager.getInstance().logOut();
                        }
                        try {
                            startActivity(intent);}
                        catch (Exception e){}                      }
                });


                if (HomeActivity.usersRatings.containsKey(finalUserID2) && !LoginActivity.isAno) {
                    ratingBar.setRating(HomeActivity.usersRatings.get(finalUserID2));
                    ratingBar.setClickable(false);
                    ratingBar.setActivated(false);
                    ratingBar.setEnabled(false);
                }


            }
        });


        //Button b = findViewById(R.id.anonymus_button);
        //b.callOnClick();

 //       ProfileActivity.this.runOnUiThread(new Runnable() {
   //         public void run() {
                doInit(null, finalUserID2);
     //       }
       // });

        if(userID!=null) {

            final HashMap<String, String> header = new HashMap<String, String>();
            header.put("commented", userID);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    List<Comment> response = Communication.makeGetRequestGetList(Communication.ip + "/comment/getAllComments", header, Comment.class);
                    listview = (ExpandableHeightListView) findViewById(R.id.listview);
                    Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                    Bean = new ArrayList<UI_objects.Bean>();
                    for (int i = 0; i < response.size(); i++) {
                        Comment com = response.get(i);
                        //Log.d("hidvir",com.getText());
                        cal.setTimeInMillis(Long.parseLong(com.getTimeStamp().toString()));
                        String date = DateFormat.format("dd-MM-yyyy", cal).toString();

                        UI_objects.Bean bean = new UI_objects.Bean(com.getUserPictureUrl().toString(), com.getText(), date, com.getUserName(), com.getUserToken());
                        //                     //Bean bean = new Bean(0, com.getText(), com.getTimeStamp().toString(), com.getUserName());
                        Bean.add(bean);
                    }
                    baseAdapter = new JayBaseAdapter(ProfileActivity.this, Bean) {
                    };
                    listview = (ExpandableHeightListView) findViewById(R.id.listview);


                    Handler mainHandler = new Handler(ProfileActivity.this.getMainLooper());

                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            listview.setAdapter(baseAdapter);
                        } // This is your code
                    };
                    mainHandler.post(myRunnable);

                    return null;
                }
            }.execute();
        }
    }




    @Override
    public void onSliderClick(BaseSliderView slider) {

    }


    public void doInit(View v, String userID) {
        HashMap<String, String> file_maps = new HashMap<String, String>();
        final Bundle data2 = getIntent().getExtras();
        boolean isAnonymus = LoginActivity.isAno && !getIntent().getExtras().containsKey("notProfile");
        //new ShowTitle().execute("http://info.radiostyle.ru/inc/getinfo.php?getcurentsong=20383&mount=lezgifm");
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);


        if (!isAnonymus) {
            //final Object pic = user.get("profile_pic");
            Bundle e  = data2;
            file_maps.put("1", data2.get("image").toString());
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
        customfonts.MyTextView rankValue = (customfonts.MyTextView) findViewById(R.id.rankValue);


        if (!isAnonymus) {
            name.setText(data2.get("firstName").toString() + " " + data2.get("lastName").toString());
            emailValue.setText(data2.get("email").toString());
            genderValue.setText(data2.get("gender").toString());
            float f = Float.parseFloat(data2.get("rank").toString());
            String formattedString = String.format("%.02f", f);
            rankValue.setText(formattedString);
        } else {
            name.setText("Anonymus");
            emailValue.setText("Unknown");
            genderValue.setText("Unknown");
            rankValue.setText("Unknown");
        }
    }

}