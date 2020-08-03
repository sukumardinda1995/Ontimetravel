package in.ontimetravel.ontimetravel;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;

public class Feedback extends AppCompatActivity {

    EditText name,city,state,country;
    Button insertdata;
    ProgressDialog mProgressDialog;
    private static final String INSERTDATA_URL = "https://ontimetravel.in/ottjson/feedback.php";



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_feedback);


        name = (EditText)findViewById(R.id.editTextname);
        city = (EditText)findViewById(R.id.editTextcity);
        state = (EditText)findViewById(R.id.editTextstate);
        country = (EditText)findViewById(R.id.editTextcountry);

        insertdata = (Button)findViewById(R.id.buttoninsertdata);

        insertdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = name.getText().toString().trim().toLowerCase();
                String City = city.getText().toString().trim().toLowerCase();
                String State = state.getText().toString().trim().toLowerCase();
                String Country = country.getText().toString().trim().toLowerCase();

                if (Name.equals("")||City.equals("")||State.equals("")||Country.equals("")){
                    Toast.makeText(Feedback.this, "Please Enter Detail", Toast.LENGTH_SHORT).show();
                }
                InsertData();

            }
        });

    }
    private void InsertData() {

        String Name = name.getText().toString().trim().toLowerCase();
        String City = city.getText().toString().trim().toLowerCase();
        String State = state.getText().toString().trim().toLowerCase();
        String Country = country.getText().toString().trim().toLowerCase();

        if (Name.equals("")||City.equals("")||State.equals("")||Country.equals("")){
            Toast.makeText(Feedback.this, "Please Enter Detail", Toast.LENGTH_SHORT).show();
        }
        else {


            register(Name, City, State, Country);
        }
    }

    private void register(String Name, String City, String State, String Country) {
        class RegisterUsers extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialog = new ProgressDialog(Feedback.this);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setMessage(getString(R.string.progress_detail));
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
                mProgressDialog.setProgress(0);
                mProgressDialog.setProgressNumberFormat(null);
                mProgressDialog.setProgressPercentFormat(null);
                mProgressDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Feedback.this,ProfileActivity.class);
                startActivity(intent);
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String, String>();


                data.put("name",params[0]);
                data.put("email", params[1]);
                data.put("number", params[2]);
                data.put("comment", params[3]);


                String result = ruc.sendPostRequest(INSERTDATA_URL, data);

                return result;
            }
        }

        RegisterUsers ru = new RegisterUsers();
        ru.execute(Name, City, State, Country);
    }
}
