package ws.wolfsoft.get_detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.adroitandroid.chipcloud.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import DataObjects.Apartment;

public class CatSearchActivity extends AppCompatActivity {
    private List<String> catsList;
    List<Apartment> allResults= null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cat_search_layout);
        //getSupportActionBar().hide();

        catsList = new ArrayList<String>();
        catsList.add("5th floor and higher");
        catsList.add("4 rooms");
        catsList.add("for rent");
        catsList.add("80 cubic meter or more");
        catsList.add("3 stars rank or higher");
        catsList.add("parking available");
        catsList.add("less than 1M NIS");





        ChipCloud chipCloud = (ChipCloud) findViewById(R.id.chip_cloud);
        chipCloud = (ChipCloud) findViewById(R.id.chip_cloud);
        chipCloud.setGravity(FlowLayout.Gravity.STAGGERED);
        chipCloud.addChips(catsList.toArray(new String[0]));
        HashSet chipsSelectedSet = new HashSet<>();
        chipCloud.setChipListener(new ChipListener() {
            @Override
            public void chipSelected(int i) {
                chipsSelectedSet.add(catsList.get(i));
            }
            @Override
            public void chipDeselected(int i) {
                chipsSelectedSet.remove(catsList.get(i));
            }
        });

        Button search = (Button) findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener() {
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
                List<Apartment> afterFilter = new ArrayList<Apartment>();


                Boolean is5thFloor  = chipsSelectedSet.contains("5th floor or less");
                Boolean is4Rooms  = chipsSelectedSet.contains("4 rooms");
                Boolean is80Cubic  = chipsSelectedSet.contains("80 cubic meter or more");
                Boolean isForRent  = chipsSelectedSet.contains("for rent");
                Boolean is3start  = chipsSelectedSet.contains("3 stars rank or higher");
                Boolean isParkingAvailable  = chipsSelectedSet.contains("parking available");
                Boolean isLess1M = chipsSelectedSet.contains("less than 1M NIS");


                for (Apartment c : allResults) {

                    if (is5thFloor && c.getFloor() >= 6) {
                        continue;
                    } else {
                        if (is4Rooms && c.getNumRooms() != 4) {
                            continue;
                        } else {
                            if (is80Cubic && c.getSize() < 80) {
                                continue;
                            } else {
                                if (isForRent && !c.getIsRent()) {
                                    continue;
                                } else {
                                    if (is3start && c.getAverageRank() < 3) {
                                        continue;
                                    } else {
                                        if (isParkingAvailable && !c.getParking()) {
                                            continue;
                                        }
                                         else {
                                            if (isLess1M&& c.getPrice()>1000000) {
                                                continue;
                                            }
                                                else {

                                                afterFilter.add(c);
                                            }
                                        }

                                    }
                                }
                            }
                        }
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
}
