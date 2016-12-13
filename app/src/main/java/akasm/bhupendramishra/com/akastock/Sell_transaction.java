package akasm.bhupendramishra.com.akastock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

public class Sell_transaction extends AppCompatActivity {
    TextView name, noshare, calculation, cash;
    EditText cost, noshare_sell;
    Button submit,cal;
    String share_name;
    int no_of_share;
    float cash_comp;
    int new_no_share;
    float new_cash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_transaction);
        name = (TextView) findViewById(R.id.name);
        noshare = (TextView) findViewById(R.id.noshare);
        calculation = (TextView) findViewById(R.id.calculation);
        cash=(TextView)findViewById(R.id.cash);
        cost=(EditText)findViewById(R.id.cost);
        noshare_sell=(EditText)findViewById(R.id.noshare_sell);
        submit=(Button)findViewById(R.id.submit);
        cal=(Button)findViewById(R.id.cal);
        Intent i=getIntent();
        share_name=i.getStringExtra("share_name");
        no_of_share=Integer.parseInt(i.getStringExtra("no_of_share"));
        cash_comp=Float.parseFloat(i.getStringExtra("cash"));
        name.setText("Stock Selected is : "+share_name);
        noshare.setText("Max No of share avaialbe for transaction : "+String.valueOf(no_of_share));
        //cash.setText("Cash Available : "+String.valueOf(cash_comp));
        cal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                calculate_figures();
            }

        });

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                complete_transaction();
                Intent intent = new Intent(Sell_transaction.this, Portfolio_admin.class);
                startActivity(intent);
                finish();
            }

        });

    }

    private void calculate_figures()
    {

        if(Integer.parseInt(noshare_sell.getText().toString())<=no_of_share) {
            new_no_share = no_of_share - Integer.parseInt(noshare_sell.getText().toString());
            new_cash = cash_comp + Float.parseFloat(cost.getText().toString()) * Integer.parseInt(noshare_sell.getText().toString());
            calculation.setText("New Number of Shares Remaining : " + String.valueOf(new_no_share) + Global.separator + "Number of shares sold : " + noshare_sell.getText().toString() + Global.separator + "Old Cash Compoenent : " + String.valueOf(cash_comp) + Global.separator + "New Cash component : " + String.valueOf(new_cash));
        }
        else
        {
            Toast.makeText(Sell_transaction.this,"Sell quantity should be less or equal to the original quantity", Toast.LENGTH_LONG).show();
        }
    }

    private void complete_transaction()
    {

        if(Integer.parseInt(noshare_sell.getText().toString())<=no_of_share) {
            new_no_share = no_of_share - Integer.parseInt(noshare_sell.getText().toString());
            new_cash = cash_comp + Float.parseFloat(cost.getText().toString()) * Integer.parseInt(noshare_sell.getText().toString());
            DatabaseReference ref = Global.firebase_dbreference.child("users").child(Global.subscriber).child("cash_component");
            ref.setValue(String.valueOf(new_cash));
            DatabaseReference ref5 = Global.firebase_dbreference.child("users").child(Global.subscriber).child("transactions").child(Global.date_time_in_sec());
            ref5.setValue("Cash Changed : " + String.valueOf(new_cash));
            DatabaseReference ref2 = Global.firebase_dbreference.child("users").child(Global.subscriber).child("portfolio").child(share_name).child("no_of_share");
            ref2.setValue(String.valueOf(new_no_share));

        }
        else
        {
            Toast.makeText(Sell_transaction.this,"Sell quantity should be less or equal to the original quantity", Toast.LENGTH_LONG).show();
        }
    }




    @Override
    public void onBackPressed() {

        // your code.
        Intent intent = new Intent(Sell_transaction.this, Portfolio_admin.class);
        startActivity(intent);
        finish();
    }









}

