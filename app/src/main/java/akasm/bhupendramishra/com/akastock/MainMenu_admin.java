package akasm.bhupendramishra.com.akastock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MainMenu_admin extends AppCompatActivity {
    private Button stockdata,aboutus;
    String separator = System.getProperty("line.separator");
    ListView lv1;
    ArrayList<Subscriber_results> results = new ArrayList<Subscriber_results>();
    public Subscriber_list_view_adapter adapter,adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_admin);
        adapter=null;
        adapter1=null;
        lv1=(ListView)findViewById(R.id.subscriber_list);
        GetSubscriberResults();

    }


    private void GetSubscriberResults(){
        //ArrayList<SearchResults> results = new ArrayList<SearchResults>();
        adapter=null;
        DatabaseReference user_ref = Global.firebase_dbreference.child("users");

        user_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    if (child != null){


                        Map<String, Object> map = (Map<String, Object>) child.getValue();
                        Subscriber_results sr1 = new Subscriber_results();
                        if(map!=null && map.get("phone")!=null && map.get("email")!=null) {
                            sr1.setName("Name : "  + child.getKey().toString());
                            sr1.setPhone("Mobile No. : "+map.get("phone").toString());
                            sr1.setemail("Email-id : " + map.get("email").toString());
                            results.add(sr1);
                        }



                    }

                }

                adapter = new Subscriber_list_view_adapter(MainMenu_admin.this, results);
                lv1.setAdapter(adapter);
                adapter.setContext(MainMenu_admin.this);


                lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                        Object o = lv1.getItemAtPosition(position);
                        Subscriber_results fullObject = (Subscriber_results) o;
                         Toast.makeText(MainMenu_admin.this, "You have chosen: " + " " + fullObject.getName()+Global.separator+fullObject.getPhone(), Toast.LENGTH_LONG).show();
                        String subscriber_invite=fullObject.getName();
                        String subscriber=subscriber_invite.split(":")[1].trim();
                        String subscriber_phone=fullObject.getPhone();

                        Intent i1 = new Intent(MainMenu_admin.this,Portfolio_admin.class);
                        //i1.putExtra("subscriber", subscriber);
                        Global.subscriber=subscriber;
                        startActivity(i1);
                        finish();


                    }
                });



            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(MainMenu_admin.this, error.toException().toString(), Toast.LENGTH_LONG).show();

            }
        });

    }

}
