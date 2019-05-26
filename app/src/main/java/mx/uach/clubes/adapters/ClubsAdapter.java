package mx.uach.clubes.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.io.InputStream;

import mx.uach.clubes.R;
import mx.uach.clubes.clubs.Club;

public class ClubsAdapter extends FirestoreRecyclerAdapter<Club, ClubsAdapter.ClubsHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ClubsAdapter(@NonNull FirestoreRecyclerOptions<Club> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ClubsHolder holder, int position, @NonNull Club model) {
        holder.name.setText(model.getName());
        holder.image.setImageBitmap(doInBackground(model.getImage()));
    }

    @NonNull
    @Override
    public ClubsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.club_item, viewGroup, false);
        return new ClubsHolder(v);
    }

    class ClubsHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView image;

        public ClubsHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_club_name);
            image = itemView.findViewById(R.id.iv_club_profile);
        }
    }


    protected Bitmap doInBackground(String... urls) {
        String imageURL = urls[0];
        Bitmap bimage = null;
        try {
            InputStream in = new java.net.URL(imageURL).openStream();
            bimage = BitmapFactory.decodeStream(in);

        } catch (Exception e) {
            Log.e("Error Message", e.getMessage());
            e.printStackTrace();
        }
        return bimage;
    }
}
