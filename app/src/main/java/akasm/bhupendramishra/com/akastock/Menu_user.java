package akasm.bhupendramishra.com.akastock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu_user extends AppCompatActivity {
    Button portfolio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_user);
        portfolio=(Button)findViewById(R.id.portfolio_user);
        portfolio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Menu_user.this, Portfolio_user.class);
                startActivity(intent);
                finish();
            }

        });

    }
}
