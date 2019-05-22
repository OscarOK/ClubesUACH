package mx.uach.clubes.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import mx.uach.clubes.Posts.AuthorPost;
import mx.uach.clubes.R;
import mx.uach.clubes.Posts.Post;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> implements View.OnClickListener {
    private ArrayList<AuthorPost> posts;
    private View.OnClickListener onClickListener;

    @Override
    public void onClick(View v) {
        if (onClickListener != null) {
            onClickListener.onClick(v);
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivClubProfile;
        public TextView tvClubName;
        public TextView tvPostDate;
        public TextView tvPostTitle;
        public TextView tvPostContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvClubName = itemView.findViewById(R.id.tv_post_club_name);
            tvPostDate = itemView.findViewById(R.id.tv_post_time);
            tvPostTitle = itemView.findViewById(R.id.tv_post_title);
            tvPostContent = itemView.findViewById(R.id.tv_post_content);
        }
    }

    public PostsAdapter(ArrayList<AuthorPost> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_item, viewGroup, false);

        return new PostsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.ViewHolder viewHolder, int i) {
        AuthorPost post = posts.get(i);

        viewHolder.itemView.setOnClickListener(this);

        viewHolder.tvClubName.setText(post.getClubName());
        viewHolder.tvPostDate.setText(post.getDateStr());
        viewHolder.tvPostTitle.setText(post.getTitle());
        viewHolder.tvPostContent.setText(post.getContent());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void add(AuthorPost post) {
        posts.add(post);
    }
}
