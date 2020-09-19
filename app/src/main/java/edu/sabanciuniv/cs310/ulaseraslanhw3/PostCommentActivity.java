package edu.sabanciuniv.cs310.ulaseraslanhw3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Console;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PostCommentActivity extends AppCompatActivity {


    NewsItem selectNews;
    EditText name;
    EditText message;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    String id_str;
    String name_str;
    String message_str;

    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comment);
        setTitle("Post Comment");

        ActionBar currentBar = getSupportActionBar();
        currentBar.setHomeButtonEnabled(true);
        currentBar.setDisplayHomeAsUpEnabled(true);
        currentBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24px);

        name = findViewById(R.id.namecomment);
        message = findViewById(R.id.commentmessage);

        selectNews = (NewsItem) getIntent().getSerializableExtra("selectednews");
        id = selectNews.getId();
        id_str = Integer.toString(id);

    }


    public void taskCalledClicked (View v) {

        name_str = name.getText().toString();
        message_str = message.getText().toString();
        id_str = Integer.toString(id);

        JsonTask tsk = new JsonTask();
        tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/savecomment", name_str,message_str,id_str);

    }

    class JsonTask extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute () {

            progressDialog = new ProgressDialog(PostCommentActivity.this);
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... strings) {

            StringBuilder strBuilder = new StringBuilder();
            String urlStr = strings[0];
            String name = strings[1];
            String message = strings[2];
            String id = strings[3];


            JSONObject obj = new JSONObject();

            try {
                obj.put("name",name);
                obj.put("text",message);
                obj.put("news_id",id);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                //conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.connect();

                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(obj.toString());

                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Intent i = new Intent(getApplicationContext(),CommentActivity.class);
                    startActivity(i);
                }

                else {

                    alertDialog = new ProgressDialog(PostCommentActivity.this);
                    alertDialog.setTitle("WARNING!");
                    alertDialog.setMessage("Message can not posted!");
                    alertDialog.show();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent i = new Intent(getApplicationContext(),CommentActivity.class);
            i.putExtra("selectednews",selectNews);
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_postcomment,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(getApplicationContext(),CommentActivity.class);
            startActivity(i);
        }
        return true;
    }
}
