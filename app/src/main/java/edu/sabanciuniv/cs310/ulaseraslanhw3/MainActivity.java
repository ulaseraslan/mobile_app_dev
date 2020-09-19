package edu.sabanciuniv.cs310.ulaseraslanhw3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    RecyclerView newsRecView;
    List<NewsItem> data;
    NewsAdapter adp;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("News");

        data = new ArrayList<>();
        newsRecView = findViewById(R.id.newsrec);

        adp = new NewsAdapter(data, this, new NewsAdapter.NewsItemClickedListener() {
            @Override
            public void newItemClicked(NewsItem selectedNewsItem) {

                //Toast.makeText(MainActivity.this,selectedNewsItem.getTitle(),Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),NewsDetailsActivity.class);
                i.putExtra("selectednews",selectedNewsItem);
                startActivity(i);
            }
        });

        spinner = findViewById(R.id.spinner);
        String [] cats = getResources().getStringArray(R.array.cats);
        ArrayAdapter<String> adp_spin = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,cats);

        spinner.setAdapter(adp_spin);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {

                    case 0:
                        NewsTask tsk = new NewsTask();
                        tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getall");
                        break;
                    case 1:
                        NewsTask tsk1 = new NewsTask();
                        tsk1.execute("http://94.138.207.51:8080/NewsApp/service/news/getbycategoryid/4");
                        break;
                    case 2:
                        NewsTask tsk2 = new NewsTask();
                        tsk2.execute("http://94.138.207.51:8080/NewsApp/service/news/getbycategoryid/6");
                        break;
                    case 3:
                        NewsTask tsk3 = new NewsTask();
                        tsk3.execute("http://94.138.207.51:8080/NewsApp/service/news/getbycategoryid/5");
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        newsRecView.setLayoutManager(new LinearLayoutManager(this));
        newsRecView.setAdapter(adp);
        NewsTask tsk = new NewsTask();
        tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getall");

    }



    class NewsTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute () {

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle("Loading");
                progressDialog.setMessage("Please wait...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String urlStr = strings[0];

            StringBuilder buffer = new StringBuilder();


            try {
                URL url = new URL(urlStr);

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));


                String line = "";

                while ((line = reader.readLine()) != null ) {
                     buffer.append(line);

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.getMessage();
            }

            return buffer.toString();

        }

        @Override

        protected void onPostExecute ( String s) {

            data.clear();
            Log.i("DEV",s);

            try {
                JSONObject obj = new JSONObject(s);

                if (obj.getInt("serviceMessageCode") == 1) {
                    //Service is ok

                    JSONArray arr = obj.getJSONArray("items");

                    for(int i = 0 ; i< arr.length(); i++) {

                        JSONObject current = (JSONObject) arr.get(i);


                        long date = current.getLong("date");
                        Date objDate = new Date(date);

                        NewsItem item = new NewsItem(
                                current.getInt("id"),
                                current.getString("title"),
                                current.getString("text"),
                                current.getString("image"),
                                objDate
                                );

                        data.add(item);


                    }
                }

                else {
                    //Service is not ok
                    //Show AlertDialog

                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setTitle("WARNING!");
                    progressDialog.setMessage("Can not load the content!");
                    progressDialog.show();

                }

                Log.i("DEV",String.valueOf(data.size()));
                adp.notifyDataSetChanged();
                progressDialog.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("DEV",e.getMessage());

            }
            
        }
    }
}
