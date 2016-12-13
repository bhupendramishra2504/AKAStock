package akasm.bhupendramishra.com.akastock;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Signup extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button signup;
    private EditText email,password,c_password,phone_number,interest,name;
    private boolean set_signup=false;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signup=(Button)findViewById(R.id.suplogin);
        email=(EditText)findViewById(R.id.supemail);
        password=(EditText)findViewById(R.id.suppassword);
        c_password=(EditText)findViewById(R.id.supcpassword);
        phone_number=(EditText)findViewById(R.id.supphonenumber);
        interest=(EditText)findViewById(R.id.supinterest);
        name=(EditText)findViewById(R.id.supusername);
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);


        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (password.getText().toString().equals(c_password.getText().toString())&& !password.getText().toString().equals("")&& !email.getText().toString().equals("")) {
                    spinner.setVisibility(View.VISIBLE);
                    signup_process();


                }
            }

        });


    }

    private void signup_process()
    {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(name.getText().toString()+"@akasm.com", password.getText().toString())
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
                            Toast.makeText(Signup.this, "Authentication failed."+task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Signup.this, "User created successfully",
                                    Toast.LENGTH_SHORT).show();
                            writeNewUser();
                            Intent intent = new Intent(Signup.this, Login_Screen.class);
                            startActivity(intent);
                            finish();
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Signup.this, Login_Screen.class);
        startActivity(intent);
        finish();
    }

    private void writeNewUser() {
        DatabaseReference ref=Global.firebase_dbreference.child("users").child(name.getText().toString()).child("email");
        ref.setValue(email.getText().toString());
        DatabaseReference ref1=Global.firebase_dbreference.child("users").child(name.getText().toString()).child("password");
        ref1.setValue(password.getText().toString());
        DatabaseReference ref3=Global.firebase_dbreference.child("users").child(name.getText().toString()).child("phone");
        ref3.setValue(phone_number.getText().toString());
        DatabaseReference ref4=Global.firebase_dbreference.child("users").child(name.getText().toString()).child("interest");
        ref4.setValue(interest.getText().toString());
        DatabaseReference ref5=Global.firebase_dbreference.child("users").child(name.getText().toString()).child("user_type");
        ref5.setValue("limited");
        DatabaseReference ref6=Global.firebase_dbreference.child("users").child(name.getText().toString()).child("createdon");
        ref6.setValue(Global.date_time());
    }







    private class User {

        public String phone;
        public String email;
        public String interest;
        public String password;
        public String name;
        public String user_type;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User( String name,String email,String password,String phone,String interest,String user_type) {
            this.name=name;
            this.phone = phone;
            this.email = email;
            this.interest=interest;
            this.password=password;
            this.user_type=user_type;
        }

    }

}
