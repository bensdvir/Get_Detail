package ws.wolfsoft.get_detail;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import DataObjects.Apartment;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    HomeActivity homeActivity = new HomeActivity();

    ProfileActivity profile = new ProfileActivity();
    @Test
    public void chackMarkers() throws Exception {
        //a.findViewById(R.id.address);
        Apartment ap = new Apartment(500,6,true,1990,true,"hi",new Double(5),new Double(5),"Meromit 60006 Lehbhjhavim",false,5,6,"78798738923" ,"he");
        List<Apartment> l =  new ArrayList<>();
        l.add(ap);
        homeActivity.setApartmentsOnMap(l);
        //Bundle b = profile.userToBundle(user);
        //assertEquals(b.size(),7);


    }
}