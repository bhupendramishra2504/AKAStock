package akasm.bhupendramishra.com.akastock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.content.Intent;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                Intent intent = new Intent(Splash.this, Login_Screen.class);
                startActivity(intent);
                finish();
            }
        }, 500);

    }
}
