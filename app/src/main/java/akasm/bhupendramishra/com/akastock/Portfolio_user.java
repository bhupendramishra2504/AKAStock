package akasm.bhupendramishra.com.akastock;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class Portfolio_user extends AppCompatActivity {
    String share_name,share_cost,share_current_price,no_of_share,total_value,weightage;
    int count=1;
    TableLayout stk;
    EditText cash;
    TextView total;
    private ProgressBar spinner;
    JSONArray array;
    JSONObject obj;
    Bitmap bitmap = null;
    ImageView hist_data;
    String stock_sym;
    boolean valid_data=false;
    String idt="INVALID",current_price,exchange,per_change, previous_close,sname;
    String json_string="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_user);
        stk = (TableLayout) findViewById(R.id.table_main);
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);
        cash=(EditText)findViewById(R.id.cash);
        total=(TextView)findViewById(R.id.total);
        cash.setEnabled(false);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        init();
        add_row();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.portfolio_user, menu);

        // Associate searchable configuration with the SearchView
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.refresh)
        {
            update_prices();
            return true;

        }
        return super.onOptionsItemSelected(item);



    }



    public void total_value_cal()
    {

        float total_value=0.0f;
        for (int i = 1; i < stk.getChildCount(); i++) {
            View child = stk.getChildAt(i);

            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;
                TextView view= (TextView)row.getChildAt(4);
                total_value =total_value+Float.parseFloat(view.getText().toString());




            }
        }
        total_value=total_value+Float.parseFloat(cash.getText().toString());
        total.setText(String.valueOf(total_value));
    }


    public void setWeightage()
    {


        for (int i = 1; i < stk.getChildCount(); i++) {
            View child = stk.getChildAt(i);

            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;
                TextView view= (TextView)row.getChildAt(4);
                TextView view1= (TextView)row.getChildAt(5);
                Float weight=Float.parseFloat(view.getText().toString())*100/Float.parseFloat(total.getText().toString());
                view1.setText(String.valueOf(Global.round(weight,2)));





            }
        }

    }



    public void update_prices()
    {
        spinner.setVisibility(View.VISIBLE);
        for (int i = 1; i < stk.getChildCount(); i++) {
            View child = stk.getChildAt(i);

            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;
                TextView view= (TextView)row.getChildAt(0);
                TextView noshares=(TextView)row.getChildAt(1);
                TextView value=(TextView)row.getChildAt(4);
                TextView view1= (TextView)row.getChildAt(3);
                stock_sym=view.getText().toString();
                try {
                    json_string=getResponseText("https://finance.google.com/finance/info?client=ig&q="+stock_sym);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    String json_string1[]=json_string.split("//");
                    if(json_string1.length>=2) {
                        valid_data=true;
                        array = new JSONArray(json_string1[1]);
                        obj = array.getJSONObject(0);
                        current_price = obj.getString("l");
                        DatabaseReference ref3 = Global.firebase_dbreference.child("users").child(Global.username).child("portfolio").child(stock_sym).child("current_price");
                        ref3.setValue(current_price);
                        view1.setText(current_price);
                        value.setText(String.valueOf(Float.parseFloat(current_price.replaceAll(",",""))*Integer.parseInt(noshares.getText().toString())));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Portfolio_user.this,e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }
        total_value_cal();
        setWeightage();
        spinner.setVisibility(View.GONE);
        Toast.makeText(Portfolio_user.this,"All prices updated",Toast.LENGTH_LONG).show();

    }

    public void init() {



        TableRow tbrow0 = new TableRow(this);
        TextView tv1 = new TextView(this);
        tv1.setTextSize(20);
        tv1.setPadding(0,0,20,0);
        tv1.setText(" Script ");

        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setTextSize(20);
        tv2.setPadding(0,0,20,0);
        tv2.setText(" No Shares ");

        tbrow0.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setTextSize(20);
        tv3.setPadding(0,0,20,0);
        tv3.setText(" Avg Cost ");
        tbrow0.addView(tv3);
        TextView tv4 = new TextView(this);
        tv4.setTextSize(20);
        tv4.setPadding(0,0,20,0);
        tv4.setText(" Curr Cost ");

        tbrow0.addView(tv4);
        TextView tv5 = new TextView(this);
        tv5.setTextSize(20);
        tv5.setPadding(0,0,20,0);
        tv5.setText(" Value ");

        tbrow0.addView(tv5);
        TextView tv6 = new TextView(this);
        tv6.setTextSize(20);
        tv6.setPadding(0,0,20,0);
        tv6.setText(" Weightage ");

        tbrow0.addView(tv6);
        stk.addView(tbrow0);

    }


    public void add_row()
    {


        DatabaseReference user_ref = Global.firebase_dbreference.child("users").child(Global.username).child("portfolio");

        user_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child != null) {
                        Map<String, Object> map = (Map<String, Object>) child.getValue();
                        final TableRow tbrow0 = new TableRow(Portfolio_user.this);
                        final TextView tv1 = new TextView(Portfolio_user.this);
                        tv1.setText(map.get("share_name").toString());

                        tbrow0.addView(tv1);
                        final TextView tv2 = new TextView(Portfolio_user.this);
                        tv2.setText(map.get("no_of_share").toString());

                        tbrow0.addView(tv2);
                        TextView tv3 = new TextView(Portfolio_user.this);
                        tv3.setText(map.get("share_cost").toString());
                        tbrow0.addView(tv3);
                        TextView tv4 = new TextView(Portfolio_user.this);
                        tv4.setText(map.get("current_price").toString());

                        tbrow0.addView(tv4);
                        TextView tv5 = new TextView(Portfolio_user.this);
                        tv5.setText(String.valueOf(Float.parseFloat(map.get("current_price").toString().replaceAll(",",""))*Float.parseFloat(map.get("no_of_share").toString())));

                        tbrow0.addView(tv5);
                        TextView tv6 = new TextView(Portfolio_user.this);
                        tv6.setText(" Weightage ");

                        tbrow0.addView(tv6);
                        stk.addView(tbrow0);
                        tbrow0.setClickable(false);




                    }

                }

                read_cash_component();

                spinner.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(Portfolio_user.this, error.toException().toString(), Toast.LENGTH_LONG).show();

            }
        });

    }

    public void read_cash_component()
    {


        DatabaseReference user_ref = Global.firebase_dbreference.child("users").child(Global.username).child("cash_component");

        user_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    cash.setText(dataSnapshot.getValue().toString());
                    total_value_cal();
                    setWeightage();
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(Portfolio_user.this, error.toException().toString(), Toast.LENGTH_LONG).show();

            }
        });

    }

    private String getResponseText(String stringUrl) throws IOException
    {
        StringBuilder response  = new StringBuilder();

        URL url = new URL(stringUrl);
        HttpURLConnection httpconn = (HttpURLConnection)url.openConnection();
        if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK)
        {
            BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()),8192);
            String strLine = null;
            while ((strLine = input.readLine()) != null)
            {
                response.append(strLine);
            }
            input.close();
        }
        return response.toString();
    }




    @Override
    public void onBackPressed() {

        // your code.
        Intent intent = new Intent(Portfolio_user.this, Menu_user.class);
        startActivity(intent);
        finish();
    }

}
