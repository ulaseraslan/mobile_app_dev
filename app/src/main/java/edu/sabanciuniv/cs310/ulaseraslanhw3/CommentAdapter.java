package edu.sabanciuniv.cs310.ulaseraslanhw3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    List<CommentItem> commentItems;
    Context context;
    CommentItemClickListener listener;


    public CommentAdapter(List<CommentItem> commentItems, Context context,CommentItemClickListener listener) {
        this.commentItems = commentItems;
        this.context = context;
        this.listener = listener;
    }


    public List<CommentItem> getCommentItems() {
        return commentItems;
    }

    public void setCommentItems(List<CommentItem> commentItems) {
        this.commentItems = commentItems;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.comments_row_layout,parent,false);
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, final int position) {

        holder.txtTitle.setText(commentItems.get(position).getName());
        holder.message.setText(commentItems.get(position).getMessage());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.commentItemClicked(commentItems.get(position));
            }
        });


    }

    @Override
    public int getItemCount() {
        return commentItems.size();
    }

    public interface CommentItemClickListener {

        public void commentItemClicked(CommentItem selectedComment);

    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView message;
        ConstraintLayout root;

        public CommentViewHolder(@NonNull View itemView) {
            super (itemView);

            txtTitle = itemView.findViewById(R.id.commentname);
            message = itemView.findViewById(R.id.commentmessage);
            root = itemView.findViewById(R.id.commentcontainer);

        }

    }

}
