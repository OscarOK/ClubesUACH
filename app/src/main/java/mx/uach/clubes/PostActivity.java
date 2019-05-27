package mx.uach.clubes;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PostActivity extends AppCompatActivity {

    private ImageView ivPostImageProfile;
    private TextView postClubName;
    private TextView postTitle;
    private TextView postDate;
    private TextView postContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ivPostImageProfile = findViewById(R.id.img_full_post_profile);
        postClubName = findViewById(R.id.tv_full_post_club_name);
        postTitle = findViewById(R.id.tv_full_post_title);
        postDate = findViewById(R.id.tv_full_post_time);
        postContent = findViewById(R.id.tv_full_post_content);

        setPost();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setPost() {
        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        if (bundle != null) {
            postClubName.setText(bundle.getString("post_club_name", "Club"));
            postTitle.setText(bundle.getString("post_title", "Title"));
            postDate.setText(bundle.getString("post_date", "-"));
            postContent.setText(bundle.getString("post_content", "..."));

            String imgClubProfile = bundle.getString("post_img_profile", "");

            if (!imgClubProfile.equals("")) {
                Picasso.get().load(imgClubProfile).into(ivPostImageProfile);
            }
        }
    }
}
