package in.ontimetravel.ontimetravel;
import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    TextView disablePastDate;
    AutoCompleteTextView editTextname, editTextcity;
    Button buttonfetch;
    ListView listview;
    String Location, Dloction1;
    ProgressDialog mProgressDialog;
    Menu menu;
    ImageView b, c;

    public static final String KEY_NAME = "location";
    public static final String KEY_CITY = "dloction1";
    public static final String KEY_BUSNUMBER = "busnumber";
    public static final String KEY_DTIME = "dtime";
    public static final String KEY_TIME = "time";
    public static final String KEY_TIMEN = "timen";
    public static final String KEY_BOOKINGNUMBER = "bookingnumber";
    public static final String KEY_TSEAT = "tseat";
    public static final String KEY_TPRICE = "tprice";
    public static final String KEY_BUSNAME = "busname";
    public static final String KEY_SEATTYPE = "seattype";
    public static final String KEY_TYPE = "type";
    public static final String KEY_JDAYS = "jdays";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        b = (ImageView) findViewById(R.id.button_swap);
        editTextname = (AutoCompleteTextView) findViewById(R.id.etname);
        editTextcity = (AutoCompleteTextView) findViewById(R.id.etcity);
        c = (ImageView) findViewById(R.id.imagefeed);

        editTextname.setAdapter(new SuggestionAdapter(this, editTextname.getText().toString()));
        editTextcity.setAdapter(new SuggestionAdapter(this, editTextcity.getText().toString()));

        buttonfetch = (Button) findViewById(R.id.btnfetch);

//       this is button menu logout, I have added logout in menu item
//        findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//
//                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                startActivity(intent);
//            }
//        });

        listview = (ListView) findViewById(R.id.listView);
        buttonfetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Location = editTextname.getText().toString().trim();
                Dloction1 = editTextcity.getText().toString().trim();

                if (Location.equals("") || (Dloction1.equals(""))) {
                    Toast.makeText(ProfileActivity.this, "Please Enter Source and Destination Location", Toast.LENGTH_SHORT).show();
                } else {

                    GetMatchData();
                }

            }
        });

        disablePastDate = findViewById(R.id.disable_past_date);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String date = formatter.format(new Date());
        disablePastDate.setText(date);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        disablePastDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        String sDate = dayOfMonth + "-" + "0" + (month + 1) + "-" + year;
                        disablePastDate.setText(sDate);
                    }
                }, year, month, day
                );
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity();
            }
        });


    }

    public void openNewActivity() {
        Intent intent = new Intent(this, Feedback.class);
        startActivity(intent);
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
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
        return (super.onOptionsItemSelected(item));
    }

    private void GetMatchData() {

        Location = editTextname.getText().toString().trim();
        Dloction1 = editTextcity.getText().toString().trim();
        mProgressDialog = new ProgressDialog(ProfileActivity.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMessage(getString(R.string.app_name));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgress(0);
        mProgressDialog.setProgressNumberFormat(null);
        mProgressDialog.setProgressPercentFormat(null);
        mProgressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config5.MATCHDATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")) {

                            showJSON(response);
                            mProgressDialog.dismiss();

                        } else {
                            showJSON(response);

                            mProgressDialog.dismiss();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileActivity.this, "" + error, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(KEY_NAME, Location);
                map.put(KEY_CITY, Dloction1);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config5.JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String location = jo.getString(Config5.KEY_NAME);
                String dloction1 = jo.getString(Config5.KEY_CITY);
                String busnumber = jo.getString(Config5.KEY_BUSNUMBER);
                String dtime = jo.getString(Config5.KEY_DTIME);
                String time = jo.getString(Config5.KEY_TIME);
                String timen = jo.getString(Config5.KEY_TIMEN);
                String bookingnumber = jo.getString(Config5.KEY_BOOKINGNUMBER);
                String tseat = jo.getString(Config5.KEY_TSEAT);
                String tprice = jo.getString(Config5.KEY_TPRICE);
                String busname = jo.getString(Config5.KEY_BUSNAME);
                String seattype = jo.getString(Config5.KEY_SEATTYPE);
                String type = jo.getString(Config5.KEY_TYPE);
                String jdays = jo.getString(Config5.KEY_JDAYS);
                String time1 = dtime;
                String time2 = time;
                String time4 = timen;
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
//              DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
                timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date1 = timeFormat.parse(time1);
                Date date2 = timeFormat.parse(time2);
                Date date4 = timeFormat.parse(time4);

                long sum = date1.getTime() + date2.getTime();
                long sum4 = date1.getTime() + date4.getTime();

                String date3 = timeFormat.format(new Date(sum));
                String date5 = timeFormat.format(new Date(sum4));

                Date date6 = timeFormat.parse(date3);
                Date date7 = timeFormat.parse(date5);

                long mills = date7.getTime() - date6.getTime();
                Log.v("Data1", "" + date6.getTime());
                Log.v("Data2", "" + date7.getTime());
                int hours = (int) (mills / (1000 * 60 * 60));
                int mins = (int) (mills / (1000 * 60)) % 60;

                String diff = hours + "h" + " " + mins + "m"; // updated value every1 second

                final HashMap<String, String> employees = new HashMap<>();
                employees.put(Config5.KEY_NAME, location);
                employees.put(Config5.KEY_CITY, dloction1);
                employees.put(Config5.KEY_BUSNUMBER, busnumber);
                employees.put(Config5.KEY_DTIME, date3);
                employees.put(Config5.KEY_TIMEN, date5);
                employees.put("tduration", diff);
                employees.put(Config5.KEY_BOOKINGNUMBER, bookingnumber);
                employees.put(Config5.KEY_TSEAT, tseat + " Seats");
                employees.put(Config5.KEY_TPRICE, "₹ " + tprice);
                employees.put(Config5.KEY_BUSNAME, busname);
                employees.put(Config5.KEY_SEATTYPE, " " + "(" + seattype + ")" + " ");
                employees.put(Config5.KEY_TYPE, "|" + " " + type);
                employees.put(Config5.KEY_JDAYS, "Days - " + jdays);
                list.add(employees);

//               boolean comdate= employees.containsKey(date3);

            }

            if (list.size() != 0) {
                Intent intent = new Intent(ProfileActivity.this, SearchData.class);
                intent.putExtra("key", list);
                startActivity(intent);
            } else {
                String easyPuzzle = " ";
                Intent i = new Intent(this, SearchData1.class);
                i.putExtra("puzzle", easyPuzzle);
                startActivity(i);
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

//        ListAdapter adapter = new SimpleAdapter(
//                MainActivity.this, list, R.layout.list_item,
//                new String[]{Config5.KEY_NAME, Config5.KEY_CITY, Config5.KEY_BUSNUMBER},
//                new int[]{R.id.tvaddress, R.id.tvgender, R.id.tvclass});
//        listview.setAdapter(adapter);
    }

    public void onSwap(View view) {
        editTextname = (AutoCompleteTextView) findViewById(R.id.etname);
        editTextcity = (AutoCompleteTextView) findViewById(R.id.etcity);
        String getFirstString = String.valueOf(editTextname.getText());
        String getSecondString = String.valueOf(editTextcity.getText());
        editTextname.setText(getSecondString);
        editTextcity.setText(getFirstString);
    }

    public void openBrowser(View view) {
        //Get url from tag
        String url = (String) view.getTag();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        //pass the url to intent data
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    public void openBrowser1(View view) {
        //Get url from tag
        String url1 = (String) view.getTag();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        //pass the url to intent data
        intent.setData(Uri.parse(url1));
        startActivity(intent);
    }
}
