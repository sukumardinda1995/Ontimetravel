package in.ontimetravel.ontimetravel;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.google.firebase.auth.FirebaseAuth;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchData extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_data);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout2);
        Bundle bundle = getIntent().getExtras();
        listView = findViewById(R.id.listView);
        ArrayList<HashMap<String, String>> list = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("key");
//        ArrayList<String> numbersList = (ArrayList<String>) getIntent().getSerializableExtra("key");
//           textView.setText(String.valueOf(numbersList));


        try {
            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                String location = jo.getString(Config5.KEY_NAME);
                String dloction1 = jo.getString(Config5.KEY_CITY);
                String busnumber = jo.getString(Config5.KEY_BUSNUMBER);
                String date3 = jo.getString(Config5.KEY_DTIME);
                String date5 = jo.getString(Config5.KEY_TIMEN);
                String diff = jo.getString("tduration");
                String bookingnumber = jo.getString(Config5.KEY_BOOKINGNUMBER);
                String tseat = jo.getString(Config5.KEY_TSEAT);
                String tprice = jo.getString(Config5.KEY_TPRICE);
                String busname = jo.getString(Config5.KEY_BUSNAME);
                String seattype = jo.getString(Config5.KEY_SEATTYPE);
                String type = jo.getString(Config5.KEY_TYPE);
                String jdays = jo.getString(Config5.KEY_JDAYS);

                final HashMap<String, String> employees = new HashMap<>();
                employees.put(Config5.KEY_NAME, "Location=" + location);
                employees.put(Config5.KEY_CITY, "Dloction1 = " + dloction1);
                employees.put(Config5.KEY_BUSNUMBER, "BusNumber = " + busnumber);
                employees.put(Config5.KEY_DTIME, "Dtime = " + date3);
                employees.put(Config5.KEY_TIMEN, "Time = " + date5);
                employees.put("tduration", diff);
                employees.put(Config5.KEY_BOOKINGNUMBER, "Booking Number = " + bookingnumber);
                employees.put(Config5.KEY_TSEAT, "Total Seat = " + tseat);
                employees.put(Config5.KEY_TPRICE, "Total Price = " + tprice);
                employees.put(Config5.KEY_BUSNAME, "Busname = " + busname);
                employees.put(Config5.KEY_SEATTYPE, "Seat Type = " + seattype);
                employees.put(Config5.KEY_TYPE, "Type = " + type);
                employees.put(Config5.KEY_JDAYS, "Journey Days = " + jdays);
                list.add(employees);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                SearchData.this, list, R.layout.list_item,
                new String[]{Config5.KEY_NAME, Config5.KEY_CITY, Config5.KEY_BUSNUMBER,
                        Config5.KEY_DTIME, Config5.KEY_TIMEN, "tduration", Config5.KEY_BOOKINGNUMBER,
                        Config5.KEY_TSEAT, Config5.KEY_TPRICE, Config5.KEY_BUSNAME,
                        Config5.KEY_SEATTYPE, Config5.KEY_TYPE, Config5.KEY_JDAYS
                },

                new int[]{R.id.tvaddress, R.id.tvgender, R.id.tvclass, R.id.tvdtime, R.id.tvtime, R.id.tduration, R.id.bookingnumber,
                        R.id.tvtseat, R.id.tvtprice, R.id.tvbusname, R.id.tvseattype, R.id.tvtype, R.id.tvjdays});

        listView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

//        case R.id.item1:
//            Toast.makeText(this, R.string.about_toast, Toast.LENGTH_LONG).show();
//            return(true);
            case R.id.item2:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(SearchData.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
        return (super.onOptionsItemSelected(item));
    }

}