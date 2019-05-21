package mx.uach.clubes.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        }
    }

    public ClubAdapter(ArrayList<Club> clubs) {
        this.clubs = clubs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.club_item, viewGroup);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvClubName.setText(clubs.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return clubs.size();
    }
}
