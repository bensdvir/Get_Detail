package ws.wolfsoft.get_detail;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.facebook.login.LoginManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import Communication.Communication;
import DataObjects.Apartment;

public class HomeActivity extends AppCompatActivity  implements OnMapReadyCallback {
    private static final String TAG = "";
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    public HashMap<String,Bundle> apartments = new HashMap<>();
    public HashMap<LatLng,String> apartmentsAddresses = new HashMap<>();
    public static HashMap<String,Float> aparttmentsRatings = new HashMap<>();
    public static HashMap<String,Float> usersRatings = new HashMap<>();
    boolean preesed = false;



    private GoogleMap mMap;
    private int n;
    private PopupWindow mPopupWindow;
    private String m_Text = "";
    private List<Apartment> ans = null;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location myLoc;
    LocationManager locationManager;
    LocationManager mLocationManager;
    //Location myLocation = getLastKnownLocation();
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);
        final Button locButton = (Button) findViewById(R.id.buttonLoc);
        locButton.setBackgroundResource(R.drawable.loc_black);

        locButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                //locButton.setClickable(false);
                if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }else
                {
                    if(canGetLocation()) {

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Location lastKnownLocation  = getLastKnownLocation();
                                LatLng newLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(newLatLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(17.4f));                            }
                        }, 1000);
                        //locButton.setClickable(true);


                        /*Runnable r = new Runnable(){
                            public void run() {
                                try {
                                    wait(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Location lastKnownLocation  = getLastKnownLocation();
                                LatLng newLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                                try {
                                    wait(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(newLatLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(17.4f));
                            }
                        };
                        runOnUiThread(r);
                        */

                       /* Handler mainHandler = new Handler(Looper.getMainLooper());
                        Runnable myRunnable = new Runnable() {
                            @Override
                            public void run() {
                                Location lastKnownLocation  = getLastKnownLocation();
                                LatLng newLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(newLatLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(17.4f));} // This is your code
                        };
                        mainHandler.post(myRunnable);
                        */

                    }
                    else{
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }

                    //displayLocationSettingsRequest( HomeActivity.this);
                    // Write you code here if permission already given.
                }
            }
        });


        android.support.v7.widget.Toolbar tb = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        LinearLayout profileLayout  = (LinearLayout) tb.findViewById(R.id.profileLayout);
        LinearLayout logoutLayout  = (LinearLayout) tb.findViewById(R.id.logoutLayout);
        LinearLayout searchLayout  = (LinearLayout) tb.findViewById(R.id.searchLayout);

        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preesed){
                    mPopupWindow.dismiss();
                    preesed = false;
                    return;
                }
                LayoutInflater inflater = (LayoutInflater) HomeActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);

                View customView = inflater.inflate(R.layout.custom_layout,null);
                Spinner spinner = (Spinner) customView.findViewById(R.id.rooms_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(HomeActivity.this,
                        R.array.rooms_arrays, android.R.layout.simple_spinner_dropdown_item);
// Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                      @Override
                                                      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                      }

                                                      @Override
                                                      public void onNothingSelected(AdapterView<?> parent) {

                                                      }

                                                  });

                Spinner spinnerFloor = (Spinner) customView.findViewById(R.id.floor_spinner);
                spinnerFloor.setAdapter(adapter);
                spinnerFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                });

                    // An item was selected. You can retrieve the selected item using
                    // parent.getItemAtPosition(pos)

                /*Button b = customView.findViewById(R.id.anonymus_button);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                    });
*/
                mPopupWindow = new PopupWindow(
                        customView,
                        1200,
                        1900
                );
                RelativeLayout mRelativeLayout = (RelativeLayout) findViewById(R.id.drawer_layout);
                preesed = true;
                mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);

            }
        });


        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
                Bundle facebookData = getIntent().getExtras();
                Bundle b = new Bundle();
               // b.putString("idFacebook", LoginActivity.sessionId);
                intent.putExtras(facebookData);
                startActivity(intent);
            }
        });

        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                if(getIntent().getExtras().containsKey("sessionId")) {
                    LoginManager.getInstance().logOut();
                }
                startActivity(intent);
            }
        });
    }

    public void onMapReady(GoogleMap googleMap) {
        Bundle b = getIntent().getExtras();
        mMap = googleMap;
        LatLng home = new LatLng(32,35);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //LatLng newLatLng = new LatLng(myLoc.getLatitude(), myLoc.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(home));
        //mMap.addMarker(new MarkerOptions().position(home).title("Marker in Home"));

        List<Apartment> result = getAllApartmentsFromServer();
        while(ans==null){
        }
        setApartmentsOnMap(ans);

        mMap.animateCamera( CameraUpdateFactory.zoomTo( 15.0f ) );
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            //onInfoWindowClick();

            @Override
            public void onInfoWindowClick(Marker marker) {
                dosomthing(marker);
                /*DialogFragment newFragment = new DialogFragment();
                newFragment.setStyle(4,0);
                newFragment.show(getSupportFragmentManager(), "missiles");
                */


        }});


        //mMap.moveCamera(CameraUpdateFactory.newLatLng(home));
    }


    public void setApartmentsOnMap(List<Apartment> ans) {
        if (ans==null){
        }
        for (Apartment ap: ans){
            aparttmentsRatings.put(ap.getAddress(),null);
        }
        Geocoder coder = new Geocoder(this);
        for(Apartment ap: ans){
            String strAddress = ap.getAddress();
            try {
                List<Address> address = coder.getFromLocationName(strAddress,5);
                if(address.isEmpty()){
                    continue;
                }
                Address location = address.get(0);
                LatLng lg = new LatLng(location.getLatitude(), location.getLongitude());
                apartmentsAddresses.put(lg,strAddress);
                mMap.addMarker(new MarkerOptions().position(lg).title(strAddress));
                apartments.put(strAddress,apartmentToBundle(ap));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Apartment> getAllApartmentsFromServer(){
        new AsyncTask<Void, Void, List<Apartment>>() {
            @Override
            protected List<Apartment> doInBackground(Void... params) {
                HashMap<String,String> header = new HashMap<String,String>();
                //header.put("token",getIntent().getExtras().get("idFacebook").toString());
                List<Apartment> response = Communication.makeGetRequestGetList(Communication.ip+"/apartment/getAll", header, Apartment.class);
                ans = response;//
                return response;
            }
        }.execute();
        return null;
    }

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);

                bestLocation = l;
            }
        }
        return bestLocation;
    }
    public boolean canGetLocation() {
        boolean result = true;
        LocationManager lm = null;
        boolean gps_enabled = false;
        boolean network_enabled = false;
        if (lm == null)

            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // exceptions will be thrown if provider is not permitted.
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {

        }
        try {
            network_enabled = lm
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        if (gps_enabled == false || network_enabled == false) {
            result = false;
        } else {
            result = true;
        }

        return result;
    }

    public void dosomthing(Marker marker) {
        DialogFragment newFragment = new DialogFragment();
        newFragment.setStyle(4,0);
        newFragment.show(getSupportFragmentManager(), "missiles");

        Intent intent = new Intent(getBaseContext(), ApartmentActivity.class);
            LatLng lg = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
            String ad = apartmentsAddresses.get(lg);
            Bundle b = apartments.get(ad);
            if(getIntent().getExtras().containsKey("idFacebook"))
                b.putString("idFacebook",getIntent().getExtras().get("idFacebook").toString());
            intent.putExtras(b);
            startActivity(intent);
    }

    private void getUser(final String m_text) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                HashMap<String,String> header = new HashMap<String,String>();
                header.put("token",getIntent().getExtras().get("idFacebook").toString());
                header.put("address",m_text);
                Communication.makePostRequestGetCode(Communication.ip+"/addApartment", header,null);
                return null;
            }
        }.execute();
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
        b.putInt("numToilets",ap.getNumToilet().intValue());
        b.putInt("numRooms",ap.getNumRooms().intValue());
        b.putDouble("size",ap.getSize());
        b.putDouble("averageRank", ap.getAverageRank());
        b.putString("image",ap.getImage());
        b.putString("landLordID",ap.getLandLordID());
        return b;
    }


}



