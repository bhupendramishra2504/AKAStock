package akasm.bhupendramishra.com.akastock;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.EditText;
import android.os.StrictMode;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.InputStream;
import android.widget.ImageView;
import android.text.InputFilter;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.firebase.database.DatabaseReference;


public class Add_to_portfolio extends AppCompatActivity {
    TextView share_name,share_cost;
    EditText shareno,cost;
    Button add;
    String separator = System.getProperty("line.separator");
    String json_string="";
    String idt="INVALID",current_price,exchange,per_change, previous_close,sname;
    JSONArray array;
    JSONObject obj;
    Bitmap bitmap = null;
    ImageView hist_data;
    String stock_sym;
    boolean valid_data=false;
    String cash;
    private ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_portfolio);
        share_name=(TextView)findViewById(R.id.share_name);
        share_cost=(TextView)findViewById(R.id.share_cost);
        shareno=(EditText)findViewById(R.id.noshares);
        cost=(EditText)findViewById(R.id.cost);
        add=(Button)findViewById(R.id.add);
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        Intent i=getIntent();
        cash=i.getStringExtra("cash");
        if(cash.equalsIgnoreCase("")|cash.equalsIgnoreCase(null))
        {
            cash="0";
        }
        share_name.setText("Please Search share to add it to portfolio");
        cost.setEnabled(false);
        share_cost.setVisibility(View.GONE);
        shareno.setEnabled(false);
        add.setEnabled(false);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        add.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(add_detail_to_firebase()) {
                    Intent intent = new Intent(Add_to_portfolio.this, Portfolio_admin.class);
                    startActivity(intent);
                    finish();
                }
            }
        });




        handleIntent(getIntent());
    }



    public boolean add_detail_to_firebase()
    {
        boolean added=false;
                if(Float.parseFloat(cost.getText().toString())*Float.parseFloat(shareno.getText().toString())<=Float.parseFloat(cash)) {
                    added=true;
                    DatabaseReference ref = Global.firebase_dbreference.child("users").child(Global.subscriber).child("portfolio").child(sname).child("share_name");
                    ref.setValue(sname);
                    DatabaseReference ref1 = Global.firebase_dbreference.child("users").child(Global.subscriber).child("portfolio").child(sname).child("share_cost");
                    ref1.setValue(cost.getText().toString());
                    DatabaseReference ref2 = Global.firebase_dbreference.child("users").child(Global.subscriber).child("portfolio").child(sname).child("no_of_share");
                    ref2.setValue(shareno.getText().toString());
                    DatabaseReference ref3 = Global.firebase_dbreference.child("users").child(Global.subscriber).child("portfolio").child(sname).child("current_price");
                    ref3.setValue(current_price);
                    DatabaseReference ref4=Global.firebase_dbreference.child("users").child(Global.subscriber).child("cash_component");
                    ref4.setValue(String.valueOf(Float.parseFloat(cash)-Float.parseFloat(cost.getText().toString())*Float.parseFloat(shareno.getText().toString())));
                    DatabaseReference ref5 = Global.firebase_dbreference.child("users").child(Global.subscriber).child("transactions").child(Global.date_time_in_sec());
                    ref5.setValue("Share Buy : "+sname+","+cost.getText().toString()+","+shareno.getText().toString()+","+String.valueOf(Float.parseFloat(cash)-Float.parseFloat(cost.getText().toString())*Float.parseFloat(shareno.getText().toString())));
                }
        else
                {
                    Toast.makeText(Add_to_portfolio.this,"No Sufficient Balance",Toast.LENGTH_LONG).show();
                }
        return added;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.search_stock, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextChange(String query)
            {
                // this is your adapter that will be filtered
                spinner.setVisibility(View.VISIBLE);
                stock_sym=query;
                FetchOperation runner =new FetchOperation();
                runner.execute("");
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                // this is your adapter that will be filtered

                return true;
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);



        // Associate searchable configuration with the SearchView
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.edit) {

          return true;
        }





        return super.onOptionsItemSelected(item);



    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            //query = intent.getStringExtra(SearchManager.QUERY);
           // Toast.makeText(Main_Screen.this,"qurey is : "+query,Toast.LENGTH_LONG).show();


        }
    }






    private class FetchOperation extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {
            String ee="pp";
            try {
                json_string=getResponseText("https://finance.google.com/finance/info?client=ig&q="+stock_sym);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                getResponseImage("https://www.google.com/finance/getchart?q="+stock_sym);

            } catch (IOException e) {
                e.printStackTrace();
            }



            try {
                String json_string1[]=json_string.split("//");
                if(json_string1.length>=2) {
                    valid_data=true;
                    array = new JSONArray(json_string1[1]);
                    obj = array.getJSONObject(0);
                    idt = obj.getString("id");
                    current_price = obj.getString("l");
                    exchange = obj.getString("e");
                    per_change = obj.getString("c_fix");
                    previous_close = obj.getString("pcls_fix");
                    sname = obj.getString("t");
                }
                else
                {
                    valid_data=false;
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Add_to_portfolio.this,e.toString(),Toast.LENGTH_LONG).show();
            }

            return ee;

        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(String result11) {
            spinner.setVisibility(View.GONE);
            // execution of result of Long time consuming operation
            if(valid_data) {
                //hist_data.setImageBitmap(bitmap);
                cost.setEnabled(true);
                share_cost.setVisibility(View.VISIBLE);
                shareno.setEnabled(true);
                add.setEnabled(true);
                share_name.setText("Share Name : "+sname);
                share_cost.setText("Current Price Fetched : "+current_price);

                //result.setText("Exchange : " + exchange + separator + "Stock Name : " + sname + separator + "id : " + idt + separator + "Current Price : Rs " + current_price + separator + "% Change : " + per_change + separator + "Prev Close : Rs " + previous_close + separator);
            }
            else
            {
                share_name.setText("No Data Found, Check your search string");
                cost.setEnabled(false);
                share_cost.setVisibility(View.GONE);
                shareno.setEnabled(false);
                add.setEnabled(false);

            }
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onProgressUpdate(Progress[])
         */
        @Override
        protected void onProgressUpdate(String... text) {
            //finalResult.setText(text[0]);
            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }
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

    private void getResponseImage(String stringUrl) throws IOException
    {
        StringBuilder response  = new StringBuilder();

        URL url = new URL(stringUrl);
        HttpURLConnection httpconn = (HttpURLConnection)url.openConnection();
        if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK)
        {
            InputStream input=httpconn.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);
            input.close();
        }

    }

    @Override
    public void onBackPressed() {

        // your code.
        Intent intent = new Intent(Add_to_portfolio.this, Portfolio_admin.class);
        startActivity(intent);
        finish();
    }




}
