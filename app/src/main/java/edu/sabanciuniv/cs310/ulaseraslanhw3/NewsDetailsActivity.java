package edu.sabanciuniv.cs310.ulaseraslanhw3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsDetailsActivity extends AppCompatActivity {


    NewsItem selectNews;
    TextView newstitle;
    TextView newsdetail;
    TextView newsdate;
    ImageView newsimage;
    String URL;
    NewsAdapter adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        //setTitle("News Details");

        ActionBar currentBar = getSupportActionBar();
        currentBar.setHomeButtonEnabled(true);
        currentBar.setDisplayHomeAsUpEnabled(true);
        currentBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24px);


        newstitle = findViewById(R.id.newstitle);
        newsdetail = findViewById(R.id.newsdetail);
        newsdate = findViewById(R.id.newsdate);
        
        selectNews = (NewsItem) getIntent().getSerializableExtra("selectednews");

        URL = selectNews.getImagePath();
        newsimage = findViewById(R.id.newsimage);

        newstitle.setText(selectNews.getTitle());
        newsdetail.setText(selectNews.getText());

        Date date = selectNews.getNewsDate();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy ");
        String strDate = dateFormat.format(date);
        newsdate.setText(strDate);

        new ImageDownloadTask(newsimage).execute(URL);

        getSupportActionBar().setTitle("News Details");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_news_details,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

         if (item.getItemId() == R.id.commentbutton) {

                Intent i = new Intent(this,CommentActivity.class);
                i.putExtra("selectednews",selectNews);
                startActivity(i);
        }

         else if (item.getItemId() == android.R.id.home) {

             finish();

         }

            return true;

    }
}
