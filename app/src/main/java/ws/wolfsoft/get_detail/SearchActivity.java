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
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import DataObjects.Apartment;

public class SearchActivity extends AppCompatActivity {
    List<Apartment> allResults= null;
    LatLng selectedLatLng =  null;
    Location loc2 = null;
    LocationManager mLocationManager =  null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        final Spinner spinnerRoomsTo = (Spinner) findViewById(R.id.rooms_spinnerTo);
        mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(SearchActivity.this,
                R.array.rooms_arrays, android.R.layout.simple_spinner_dropdown_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerRoomsTo.setAdapter(adapter);
        spinnerRoomsTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        final Spinner spinnerFloorTo = (Spinner) findViewById(R.id.floor_spinnerTo);
        spinnerFloorTo.setAdapter(adapter);
        spinnerFloorTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        final Spinner spinnerFloorFrom = (Spinner) findViewById(R.id.floor_spinnerFrom);
        spinnerFloorFrom.setAdapter(adapter);
        spinnerFloorFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        final Spinner spinnerRoomsFrom= (Spinner) findViewById(R.id.rooms_spinnerFrom);
        spinnerRoomsFrom.setAdapter(adapter);
        spinnerRoomsFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        Button search_button = (Button) findViewById(R.id.search_button);
        final EditText startSize = (EditText) findViewById(R.id.myEditText3);
        final EditText startPrice = (EditText) findViewById(R.id.myEditText);
        final EditText endSize = (EditText) findViewById(R.id.myEditText4);
        final EditText endPrice = (EditText) findViewById(R.id.myEditText2);
        final Switch parking = (Switch) findViewById(R.id.parkingSwitch);
        final Switch warehouse = (Switch) findViewById(R.id.warehouseSwitch);
        final Switch elevator = (Switch) findViewById(R.id.elevatorSwitch);
        final Switch renting = (Switch) findViewById(R.id.rentingSwitch);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                                                            @Override
                                                            public void onPlaceSelected(Place place) {
                                                                selectedLatLng = place.getLatLng();
                                                                loc2 = new Location("");
                                                                loc2.setLatitude(selectedLatLng.latitude);
                                                                loc2.setLongitude(selectedLatLng.longitude);
                                                            }

                                                            @Override
                                                            public void onError(Status status) {

                                                            }
                                                        });







        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = getIntent().getExtras();
                int numAp = bundle.getInt("apartmentsNum");
                allResults = new ArrayList<Apartment>();
                for (HashMap.Entry<String, Apartment> entry : HomeActivity.homeToSearch.entrySet())
                {
                    allResults.add(entry.getValue()) ;
                }
                HomeActivity.homeToSearch = new HashMap<>();
                /*for(int i = 0 ; i < numAp; i ++) {
                    Apartment tmp = (Apartment) bundle.get("ap" + i);
                    allResults.add(tmp);
                }
                */
                int startSizeValue = Integer.valueOf(startSize.getText().toString());
                int startPriceValue = Integer.valueOf(startPrice.getText().toString());
                int endSizeValue = Integer.valueOf(endSize.getText().toString());
                int endPriceValue = Integer.valueOf(endPrice.getText().toString());
                int numFloorFrom =  Integer.valueOf(spinnerFloorFrom.getSelectedItem().toString());
                int numRoomsFrom =  Integer.valueOf(spinnerRoomsFrom.getSelectedItem().toString());
                int numFloorTo =  Integer.valueOf(spinnerFloorTo.getSelectedItem().toString());
                int numRoomsTo =  Integer.valueOf(spinnerRoomsTo.getSelectedItem().toString());
                final boolean parkingFilter = parking.isChecked();
                final boolean elevatorFilter = elevator.isChecked();
                boolean warehouseFilter = warehouse.isChecked();
                boolean rentingFilter = renting.isChecked();

                Geocoder coder = new Geocoder(SearchActivity.this);


                List<Apartment> afterFilter = new ArrayList<Apartment>();

                if (loc2 == null) {
                    if (ActivityCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SearchActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    } else {
                        if (canGetLocation()) {
                            Thread t3 = new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    Location lastKnownLocation = getLastKnownLocation();
                                    LatLng newLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                                    loc2 = new Location("");
                                    loc2.setLatitude(newLatLng.latitude);
                                    loc2.setLongitude(newLatLng.longitude);
                                }

                            });
                            try {
                                t3.start();
                                t3.join();
                            } catch (Exception e) {
                            }
                        }
                        else{
                            SearchActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(SearchActivity.this, "Can't get location", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });
                        return;
                        }
                    }}

                for (Apartment c : allResults){
                    String strAddress = c.getAddress();
                    try {
                        List<Address> cAddress = coder.getFromLocationName(strAddress, 5);
                        Address location = cAddress.get(0);


                        LatLng lg = new LatLng(location.getLatitude(), location.getLongitude());
                        Location loc1 = new Location("");
                        loc1.setLatitude(lg.latitude);
                        loc1.setLongitude(lg.longitude);


                        float distanceInMeters = loc1.distanceTo(loc2)/1000;
                        if  ( c.getIsRent() == rentingFilter && c.getElevator() == elevatorFilter && c.getParking() == parkingFilter
                                && c.getWareHouse() == warehouseFilter && c.getNumRooms() <=numRoomsTo && c.getFloor() <= numFloorTo
                                && c.getNumRooms() >=numRoomsFrom && c.getFloor() >= numFloorFrom
                                &&  c.getSize() >= startSizeValue && c.getSize() <= endSizeValue
                                &&  c.getPrice() >= startPriceValue && c.getPrice() <= endPriceValue && distanceInMeters<30)
                            afterFilter.add(c);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                }


                Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                Bundle b = new Bundle();
                b.putBoolean("isInSearch",true);
                int i = 0;
                for (Apartment ap : afterFilter){
                    HomeActivity.searchToHome.put("ap"+i,ap);
                    i+=1;
                }
                b.putInt("apartmentsNum", afterFilter.size());
                if(getIntent().getExtras().containsKey("idFacebook")) {
                    b.putString("idFacebook", getIntent().getExtras().get("idFacebook").toString());
                }
                if(getIntent().getExtras().containsKey("idFacebook")) {

                    b.putString("sessionId", getIntent().getExtras().get("idFacebook").toString());
                }
                intent.putExtras(b);
                startActivity(intent);
            }
        });


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

}
