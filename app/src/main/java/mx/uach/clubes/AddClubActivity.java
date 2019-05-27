package mx.uach.clubes;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import mx.uach.clubes.adapters.ClubsAdapter;
import mx.uach.clubes.clubs.Club;

public class AddClubActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference clubsref = db.collection("clubs");

    private ClubsAdapter adapter;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_club);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        if (bundle != null) {
            uid = bundle.getString("uid", "Club");

        }
        setup();
    }

    private void setup() {
        Query query = clubsref.orderBy("name", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Club> options = new FirestoreRecyclerOptions.Builder<Club>()
                .setQuery(query, Club.class)
                .build();

        adapter = new ClubsAdapter(options, uid);

        System.out.println("hola");
        RecyclerView recyclerView = findViewById(R.id.rv_addClubs);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
