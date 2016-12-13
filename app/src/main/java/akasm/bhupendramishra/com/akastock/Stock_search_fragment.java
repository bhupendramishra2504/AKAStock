package akasm.bhupendramishra.com.akastock;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by bhupendramishra on 26/10/16.
 */

public class Stock_search_fragment  extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /** Inflating the layout for this fragment **/
        View v = inflater.inflate(R.layout.stock_search,container, true);
        return v;
    }
}
