package ws.wolfsoft.get_detail;

/**
 * Created by דבירבןשבת on 10/06/2018.
 */
        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Iterator;

        import Communication.Communication;

public class Users extends AppCompatActivity {
    ListView usersList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        usersList = (ListView)findViewById(R.id.usersList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);

        pd = new ProgressDialog(Users.this);
        pd.setMessage("Loading...");
        pd.show();

        String url = "https://finalproject-72668.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Users.this);
        rQueue.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        HashMap<String, String> header = new HashMap<String, String>();
                        header.put("userName", al.get(position));
                        String token = Communication.makeGetRequestGetString(Communication.ip + "/user/getTokenByUserName", header);
                        if (token.equals("")) {
                            UserDetails.chatWith = al.get(position);
                            startActivity(new Intent(Users.this, Chat.class));
                        }
                        else{
                        header = new HashMap<String, String>();
                        header.put("myToken", LoginActivity.sessionId);
                        header.put("blockToken", token);
                        Boolean response = Communication.makePostRequest(Communication.ip + "/user/isBlockForChat", header, null, Boolean.class);
                        if (response) {
                            Users.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(Users.this, "You blocked this user", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });
                            return null;
                        } else {
                            header = new HashMap<String, String>();
                            header.put("myToken", token);
                            header.put("blockToken", LoginActivity.sessionId);
                            Boolean response2 = Communication.makePostRequest(Communication.ip + "/user/isBlockForChat", header, null, Boolean.class);
                            if (response2) {
                                Users.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(Users.this, "You are blocked by this user", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                });
                                return null;
                            } else {

                                UserDetails.chatWith = al.get(position);
                                startActivity(new Intent(Users.this, Chat.class));
                            }
                        }
                    }
                        return null;
                    }
                }.execute();








            }
        });
    }

    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();

                if(!key.equals(UserDetails.username)) {
                    al.add(key);
                }

                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalUsers <=1){
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
        }

        pd.dismiss();
    }
}