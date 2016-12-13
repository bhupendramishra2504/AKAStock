package akasm.bhupendramishra.com.akastock;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by bhupendramishra on 25/10/16.
 */

public class Global {
    public static FirebaseDatabase firebase_database = FirebaseDatabase.getInstance();
    public static DatabaseReference firebase_dbreference=firebase_database.getReference("Stockmanager");
    public static String username="NA";
    public static String user_desc_name="NA";
    public static String separator = System.getProperty("line.separator");
    public static long high=0,med=0,low=0;
    public static String rr="LOW";
    public static String channel_id="";
    public static boolean authenticated=false;
    public static boolean block;
    public static boolean broadcasting=false;
    public static int ch_list_pos=-1;
    public static String dob="",gender="",city="",country="";
    public static String email="";
    public static String phone="";
    public static String interest="";
    public static String password="";
    public static String user_type="";
    public static String subscriber="";


    public static String date_time()
    {
        String datetime="";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        datetime = sdf.format(cal.getTime());
        return datetime;
    }

    public static String date_time_in_sec()
    {
        String datetime="";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
        datetime = sdf.format(cal.getTime());
        return datetime;
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public static void set_action_bar_details(AppCompatActivity context, String title, String sub_title)
    {
        ActionBar ab = context.getSupportActionBar();
        ab.setTitle(title);
        ab.setSubtitle(sub_title);
        //ab.setDisplayHomeAsUpEnabled(true);

    }


    public static String[] restore_previous_state(Context context,CheckBox cb)
    {
        SharedPreferences prefs = context.getSharedPreferences("akasm", MODE_PRIVATE);
        String username = prefs.getString("email", null);
        String password1 =prefs.getString("password", null);
        String[] data=new String[2];
        boolean remember=prefs.getBoolean("remember",false);
        if(remember)
        {
            cb.setChecked(true);
            if(username != null && password1 != null)
            {
                data[0]=username;
                data[1]=password1;


            }
        }
        else
        {
            cb.setChecked(false);
        }
        return data;
    }

    public static void save_username_password(boolean check,Context context,String data1,String data2)
    {
        if(check)
        {
            SharedPreferences.Editor editor = context.getSharedPreferences("akasm", MODE_PRIVATE).edit();
            editor.putString("email", data1);
            editor.putString("password", data2);
            editor.putBoolean("remember", true);
            editor.commit();
        }
        else
        {
            SharedPreferences.Editor editor = context.getSharedPreferences("akasm", MODE_PRIVATE).edit();
            editor.putString("email", "");
            editor.putString("password", "");
            editor.putBoolean("remember",false);
            editor.commit();
        }
    }






}
