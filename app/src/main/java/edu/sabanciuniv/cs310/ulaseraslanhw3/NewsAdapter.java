package edu.sabanciuniv.cs310.ulaseraslanhw3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    List<NewsItem> newsItems;
    Context context;
    NewsItemClickedListener listener;

    public NewsAdapter(List<NewsItem> newsItems, Context context,NewsItemClickedListener listener) {
        this.newsItems = newsItems;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.news_row_layout,parent,false);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, final int position) {

        holder.txtDate.setText(new SimpleDateFormat("dd/MM/yyy").format(newsItems.get(position).getNewsDate()));
        holder.txtTitle.setText(newsItems.get(position).getTitle());

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.newItemClicked(newsItems.get(position));
            }
        });

        //Loading image


       /* if (newsItems.get(position).getBitmap() == null) {

            new ImageDownloadTask(holder.imgNews).execute(newsItems.get(position));
        }

        else {

            holder.imgNews.setImageBitmap(newsItems.get(position).getBitmap());
        }*/

       new ImageDownloadTask(holder.imgNews).execute(newsItems.get(position).getImagePath());

    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }


    public interface NewsItemClickedListener {

        public void newItemClicked(NewsItem selectedNewsItem);

    }



    class NewsViewHolder extends RecyclerView.ViewHolder {


        ImageView imgNews;
        TextView txtTitle;
        TextView txtDate;
        ConstraintLayout root;


        public NewsViewHolder(@NonNull View itemView) {
            super (itemView);

            imgNews = itemView.findViewById(R.id.imgnews);
            txtTitle = itemView.findViewById(R.id.txtlisttitle);
            txtDate = itemView.findViewById(R.id.txtlistdate);

            root = itemView.findViewById(R.id.container);

        }


    }
}
