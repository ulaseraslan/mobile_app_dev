package edu.sabanciuniv.cs310.ulaseraslanhw3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    NewsItem selectNews;
    ProgressDialog progressDialog;
    List<CommentItem> data;
    CommentAdapter adpCom;
    RecyclerView commentRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        setTitle("Comments");

        ActionBar currentBar = getSupportActionBar();
        currentBar.setHomeButtonEnabled(true);
        currentBar.setDisplayHomeAsUpEnabled(true);
        currentBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24px);

        selectNews = (NewsItem) getIntent().getSerializableExtra("selectednews");
        int id = selectNews.getId();

        commentRecView = findViewById(R.id.commentrec);
        data = new ArrayList<>();

        adpCom = new CommentAdapter(data, this, new CommentAdapter.CommentItemClickListener() {

            @Override
            public void commentItemClicked(CommentItem selectedComment) {


            }
        });


        String url = "http://94.138.207.51:8080/NewsApp/service/news/getcommentsbynewsid/" + id;
        commentRecView.setLayoutManager(new LinearLayoutManager(this));
        commentRecView.setAdapter(adpCom);

        CommentTask tsk = new CommentTask();
        tsk.execute(url);
    }



    class CommentTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute () {

        progressDialog = new ProgressDialog(CommentActivity.this);
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

                    CommentItem item = new CommentItem(
                            current.getInt("id"),
                            current.getString("text"),
                            current.getString("name")
                    );

                    data.add(item);


                }
            }

            else {
                //Service is not ok
                //Show AlertDialog

                progressDialog = new ProgressDialog(CommentActivity.this);
                progressDialog.setTitle("WARNING!");
                progressDialog.setMessage("Can not load the content!");
                progressDialog.show();

            }

            Log.i("DEV",String.valueOf(data.size()));
            adpCom.notifyDataSetChanged();
            progressDialog.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("DEV",e.getMessage());

        }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_comment_activity,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.makeComments) {

            Intent i = new Intent(this,PostCommentActivity.class);
            i.putExtra("selectednews",selectNews);
            startActivity(i);
        }

        if (item.getItemId() == android.R.id.home) {

            finish();

        }

        return true;

    }
}
