package akasm.bhupendramishra.com.akastock;

/**
 * Created by bhupendramishra on 26/10/16.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhupendramishra on 12/10/16.
 */

public class Subscriber_list_view_adapter extends BaseAdapter {

    private static ArrayList<Subscriber_results> searchArrayList;
    private List<String> originalData = null;
    private List<String>filteredData = null;
    private static Context context;
    //private final List<SearchResults> stocks;

    private LayoutInflater mInflater;


    public Subscriber_list_view_adapter(Context context, ArrayList<Subscriber_results> results) {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return searchArrayList.size();
    }

    public Object getItem(int position) {
        return searchArrayList.get(position);
    }

    public  void setContext(Context context){this.context=context;}

    public long getItemId(int position) {
        return position;
    }



    public View getView(final int position, View convertView, ViewGroup parent) {
        final Subscriber_list_view_adapter.ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.subscriber_list_view, null);
            holder = new ViewHolder();

            holder.txtName = (TextView) convertView.findViewById(R.id.sname);
            holder.txtPhone = (TextView) convertView.findViewById(R.id.smobile);
            holder.txtemail = (TextView) convertView.findViewById(R.id.semail);

            convertView.setTag(holder);
        } else {
            holder = (Subscriber_list_view_adapter.ViewHolder) convertView.getTag();
        }

        holder.txtName.setText(searchArrayList.get(position).getName());
        holder.txtPhone.setText(searchArrayList.get(position).getPhone());
        holder.txtemail.setText(searchArrayList.get(position).getemail());

        return convertView;
    }

    static class ViewHolder {
        TextView txtName;
        TextView txtPhone;
        TextView txtemail;


    }

    public int getCount_filter() {
        return filteredData.size();
    }

    public Object getItem_filter(int position) {
        return filteredData.get(position);
    }

    public long getItemId_filter(int position) {
        return position;
    }

   /* public void filter(String charText) {

        stocks.clear();
        if (charText.length() == 0) {
            stocks.addAll(searchArrayList);
        } else {
            for (SearchResults cs : searchArrayList) {
                if (cs.getName().contains(charText)) {
                    stocks.add(cs);
                }
            }
        }
        notifyDataSetChanged();
    }*/


}

