package mx.uach.clubes.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mx.uach.clubes.R;
import mx.uach.clubes.clubs.Club;

public class ClubsAdapter extends FirestoreRecyclerAdapter<Club, ClubsAdapter.ClubsHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private String uid;



    public ClubsAdapter(@NonNull FirestoreRecyclerOptions<Club> options, String uid) {
        super(options);
        this.uid = uid;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ClubsHolder holder, int position, @NonNull Club model) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference collections = db.collection("students").document(uid);
        holder.name.setText(model.getName());
        Picasso.get().load(model.getImage()).into(holder.image);
        int i = holder.getLayoutPosition();
        final DocumentReference doc = getItemReference(i);
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collections.update("clubs", FieldValue.arrayUnion(doc.getId()));
            }
        });
        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = holder.getLayoutPosition();
                DocumentReference doc = getItemReference(i);
                collections.update("clubs", FieldValue.arrayRemove(doc.getId()));
            }
        });
        collections.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                ArrayList ar = (ArrayList) document.getData().get("clubs");
                if(ar.contains(doc.getId())){
                    holder.btnDel.setVisibility(View.VISIBLE);
                }else {
                    holder.btnAdd.setVisibility(View.VISIBLE);

                }

            }
        });
    }

    @NonNull
    @Override
    public ClubsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.club_add_item, viewGroup, false);
        return new ClubsHolder(v);
    }

    class ClubsHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView image;
        Button btnDel;
        Button btnAdd;

        public ClubsHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_club_n);
            image = itemView.findViewById(R.id.iv_club_pic);
            btnAdd = itemView.findViewById(R.id.btn_add);
            btnDel = itemView.findViewById(R.id.btn_del);
        }

    }

    public DocumentReference getItemReference(int indx){
        return getSnapshots().getSnapshot(indx).getReference();
    }



}
