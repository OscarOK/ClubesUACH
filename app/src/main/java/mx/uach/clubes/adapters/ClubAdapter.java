package mx.uach.clubes.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import mx.uach.clubes.R;
import mx.uach.clubes.clubs.Club;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ViewHolder> {
    private ArrayList<Club> clubs;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivClubProfile;
        public TextView tvClubName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClubName = itemView.findViewById(R.id.tv_club_name);
            ivClubProfile = itemView.findViewById(R.id.iv_club_profile);
        }
    }

    public ClubAdapter(ArrayList<Club> clubs) {
        this.clubs = clubs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.club_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvClubName.setText(clubs.get(i).getName());
        Picasso.get().load(clubs.get(i).getImage()).into(viewHolder.ivClubProfile);
    }

    @Override
    public int getItemCount() {
        return clubs.size();
    }
}
