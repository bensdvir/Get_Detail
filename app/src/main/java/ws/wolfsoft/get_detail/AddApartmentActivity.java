package ws.wolfsoft.get_detail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import Communication.Communication;
import DataObjects.Apartment;

public class AddApartmentActivity extends AppCompatActivity {
    List<Apartment> allResults= null;
    LatLng selectedLatLng =  null;
    Location loc2 = null;
    private static int RESULT_LOAD_IMAGE = 1;
    LocationManager mLocationManager =  null;
    byte[] image = null;
    String tmp = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_apartment);
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.no_image);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        image = stream.toByteArray();
        final Spinner spinner = (Spinner) findViewById(R.id.rooms_spinner);
        mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AddApartmentActivity.this,
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

        final Spinner spinnerFloor = (Spinner) findViewById(R.id.floor_spinner);
        spinnerFloor.setAdapter(adapter);
        spinnerFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        Button addNew_button = (Button) findViewById(R.id.finish_button);
        final EditText size = (EditText) findViewById(R.id.myEditText3);
        final EditText desc = (EditText) findViewById(R.id.myEditText4);
        final EditText price = (EditText) findViewById(R.id.myEditText);
        final EditText conYear = (EditText) findViewById(R.id.myEditText5);
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

        Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);

        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);


                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        Button finish_button = (Button)this.findViewById(R.id.finish_button);
        finish_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                    finish();
            }
            });


        addNew_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numFloor =  Integer.valueOf(spinnerFloor.getSelectedItem().toString());
                int numRooms =  Integer.valueOf(spinner.getSelectedItem().toString());
                int sizeValue = Integer.valueOf(size.getText().toString());
                int yearValue = Integer.valueOf(conYear.getText().toString());
                int priceValue = Integer.valueOf(price.getText().toString());
                String descValue = desc.getText().toString();
                Geocoder coder = new Geocoder(AddApartmentActivity.this);
                String address = "";
                if (loc2 == null){
                    AddApartmentActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(AddApartmentActivity.this, "You must enter location", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                }
                try {
                    if (loc2 == null){
                        return;
                    }
                    List<Address> locs =coder.getFromLocation(loc2.getLatitude(),loc2.getLongitude(),5);
                    address= locs.get(0).getAddressLine(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                final boolean parkingFilter = parking.isChecked();
                final boolean elevatorFilter = elevator.isChecked();
                boolean warehouseFilter = warehouse.isChecked();
                boolean rentingFilter = renting.isChecked();
                if (sizeValue==0){
                    AddApartmentActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(AddApartmentActivity.this, "illegal size ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });

                }
                if (yearValue<1900 || yearValue>Calendar.getInstance().get(Calendar.YEAR)){
                    AddApartmentActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(AddApartmentActivity.this, "illegal year", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                }

                if (sizeValue== 0 || yearValue<1900 || yearValue>Calendar.getInstance().get(Calendar.YEAR)){
                    return;
                }


                String finalAddress = address;

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                        HashMap<String, String> header = new HashMap<String, String>();
                        header.put("Content-Type","application/json");
                        Apartment ap = new Apartment(new Integer(priceValue),new Integer(numFloor),new Boolean(elevatorFilter)
                                ,new Integer(yearValue),new Boolean(warehouseFilter),descValue, new Double(sizeValue), new Double(0.0), finalAddress
                                ,new Boolean(parkingFilter),new Integer(0), new Integer(numRooms),LoginActivity.sessionId.toString(),image,new Boolean(rentingFilter));
                        HomeActivity.apartmentsImages.put(finalAddress.toString(),image);
                        HomeActivity.tmpImage = image;
                        //header.put("userID",getIntent().getExtras().getString("landLordID"));
                        //header.put("userID",LoginActivity.sessionId);
                        Boolean response = Communication.makePostRequest(Communication.ip + "/apartment/addNew", header, ap,Boolean.class);
                       if (!response) {
                           AddApartmentActivity.this.runOnUiThread(new Runnable() {
                               public void run() {
                                   Toast.makeText(AddApartmentActivity.this, "Apartment added successfully :)", Toast.LENGTH_SHORT).show();
                                   return;
                               }
                           });
                           finish();
                       }
                       else {
                           AddApartmentActivity.this.runOnUiThread(new Runnable() {
                               public void run() {
                                   Toast.makeText(AddApartmentActivity.this, "Apartment address already exists ", Toast.LENGTH_SHORT).show();
                                   return;
                               }
                           });
                       }
                        return null;
                    }
                }.execute();
            }});



      /*  search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = getIntent().getExtras();
                int numAp = bundle.getInt("apartmentsNum");
                allResults = new ArrayList<Apartment>();
                for(int i = 0 ; i < numAp; i ++) {
                    Apartment tmp = (Apartment) bundle.get("ap" + i);
                    allResults.add(tmp);
                }
                int startSizeValue = Integer.valueOf(startSize.getText().toString());
                int startPriceValue = Integer.valueOf(startPrice.getText().toString());
                int endSizeValue = Integer.valueOf(endSize.getText().toString());
                int endPriceValue = Integer.valueOf(endPrice.getText().toString());
                int numFloor =  Integer.valueOf(spinnerFloor.getSelectedItem().toString());
                int numRooms =  Integer.valueOf(spinner.getSelectedItem().toString());
                final boolean parkingFilter = parking.isChecked();
                final boolean elevatorFilter = elevator.isChecked();
                boolean warehouseFilter = warehouse.isChecked();
                Geocoder coder = new Geocoder(AddApartmentActivity.this);


                List<Apartment> afterFilter = new ArrayList<Apartment>();

                if (loc2 == null) {
                    if (ActivityCompat.checkSelfPermission(AddApartmentActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddApartmentActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AddApartmentActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
                            AddApartmentActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(AddApartmentActivity.this, "Can't get location", Toast.LENGTH_SHORT).show();
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
                        if  ( c.getElevator() == elevatorFilter && c.getParking() == parkingFilter
                                && c.getWareHouse() == warehouseFilter && c.getNumRooms() == numRooms && c.getFloor() == numFloor
                                &&  c.getSize() > startSizeValue && c.getSize() < endSizeValue
                                &&  c.getPrice() > startPriceValue && c.getPrice() < endPriceValue && distanceInMeters<30)
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
                    intent.putExtra("ap"+i,ap);
                    i+=1;
                }
                b.putInt("apartmentsNum", afterFilter.size());
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        */


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
           /* String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            */
            InputStream iStream = null;
            try {
                iStream = getContentResolver().openInputStream(selectedImage);
                byte[] inputData = getBytes(iStream);
                image = inputData;
                String s = new String(image);
                tmp =s;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

           /* new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(
                                selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    Bitmap bmp = BitmapFactory.decodeStream(imageStream);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    image = byteArray;
                    try {
                        stream.close();
                        stream = null;
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                    return null;
                }
                }.execute();
                */

            //ImageView imageView = (ImageView) findViewById(R.id.imgView);
            //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }



    }
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
