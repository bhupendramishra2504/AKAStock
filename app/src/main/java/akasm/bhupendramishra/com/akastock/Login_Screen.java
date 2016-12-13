package akasm.bhupendramishra.com.akastock;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.app.Activity;
import android.view.ViewConfiguration;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.CheckBox;
import android.view.View;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.util.Map;


public class Login_Screen extends AppCompatActivity {
    TextView signup,needhelp;
    EditText email,password;
    Button login;
    CheckBox cb;
    boolean remember;
    private FirebaseAuth mAuth;
    private ProgressBar spinner;
    private DatabaseReference mDatabase;
    private String type="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__screen);

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ignored) {
        }

        email=(EditText)findViewById(R.id.lemail);
        password=(EditText)findViewById(R.id.lpassword);
        login=(Button)findViewById(R.id.llogin);
        cb=(CheckBox)findViewById(R.id.remember);
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        email.setText(Global.restore_previous_state(Login_Screen.this,cb)[0]);
        password.setText(Global.restore_previous_state(Login_Screen.this,cb)[1]);

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                spinner.setVisibility(View.VISIBLE);
                if (!password.getText().toString().equals("") && !email.getText().toString().equals("")) {
                    remember = ((CheckBox) findViewById(R.id.remember)).isChecked();
                    Global.save_username_password(remember,Login_Screen.this,email.getText().toString(),password.getText().toString());
                    login_user();


                }
            }

        });




    }

    private void login_user()
    {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email.getText().toString()+"@akasm.com", password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("", "signInWithEmail:onComplete:" + task.isSuccessful());
                        spinner.setVisibility(View.GONE);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("", "signInWithEmail", task.getException());
                            Toast.makeText(Login_Screen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            move_from_login();
                            Toast.makeText(Login_Screen.this, "Authentication successful...",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.login_menu, menu);
        // Associate searchable configuration with the SearchView
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.Signup) {

            Intent intent = new Intent(Login_Screen.this, Signup.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
            return true;
        }




        return super.onOptionsItemSelected(item);



    }



    public void move_from_login()
    {


        DatabaseReference user_ref = Global.firebase_dbreference.child("users").child(email.getText().toString());

            user_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {
                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                        Global.username=dataSnapshot.getKey().toString();
                        Global.email=map.get("email").toString();
                        Global.phone=map.get("phone").toString();
                        Global.interest=map.get("interest").toString();
                        Global.password=map.get("password").toString();
                        Global.user_type=map.get("user_type").toString();
                    }
                    if(Global.user_type.equalsIgnoreCase("limited"))
                    {
                        Intent intent = new Intent(Login_Screen.this, Menu_user.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(Global.user_type.equalsIgnoreCase("admin"))
                    {
                        Intent intent = new Intent(Login_Screen.this, MainMenu_admin.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(Login_Screen.this, "Error while checking database", Toast.LENGTH_LONG).show();
                    }


                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Toast.makeText(Login_Screen.this, error.toException().toString(), Toast.LENGTH_LONG).show();

                }
            });

    }






}
