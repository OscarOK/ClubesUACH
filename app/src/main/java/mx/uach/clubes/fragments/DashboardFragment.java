package mx.uach.clubes.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import mx.uach.clubes.PostActivity;
import mx.uach.clubes.Posts.AuthorPost;
import mx.uach.clubes.R;
import mx.uach.clubes.Utils.PostsDBUtils;
import mx.uach.clubes.Utils.UserDBUtils;
import mx.uach.clubes.adapters.PostsAdapter;
import mx.uach.clubes.Posts.Post;
import mx.uach.clubes.clubs.Club;
import mx.uach.clubes.users.Student;

public class DashboardFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private String uid;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private PostsAdapter adapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public DashboardFragment() {}

    public static DashboardFragment newInstance(String uid) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uid = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_dashboard);
        progressBar = view.findViewById(R.id.pb_loading_posts);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CollectionReference collections = db.collection("posts");

        collections.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots,
                                @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    update();
                }
            }
        });


        update();
    }

    private void update() {
        UserDBUtils.getUser(uid, new UserDBUtils.OnSuccessUserListener() {
            @Override
            public void onSuccessUserListener(Student student) {

                PostsDBUtils.getClubs(student.getClubs(), new PostsDBUtils.OnSuccessLoadClubs() {
                    @Override
                    public void onSuccessLoadClubs(ArrayList<Club> clubs) {

                        PostsDBUtils.getClubsPosts(clubs, new PostsDBUtils.OnSuccessLoadPosts() {
                            @Override
                            public void onSuccessLoadPosts(final ArrayList<AuthorPost> posts) {
                                adapter = new PostsAdapter(posts);

                                if (adapter.getItemCount() > 0) {
                                    adapter.notifyDataSetChanged();
                                    recyclerView.setAdapter(adapter);
                                    adapter.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            int index = recyclerView.getChildAdapterPosition(v);
                                            AuthorPost post = posts.get(index);

                                            Bundle args = new Bundle();
                                            args.putString("post_club_name", post.getClubName());
                                            args.putString("post_title", post.getTitle());
                                            args.putString("post_content", post.getContent());
                                            args.putString("post_date", post.getDateStr());
                                            args.putString("post_img_profile", post.getImgClub());

                                            Intent i = new Intent(getActivity(), PostActivity.class);
                                            i.putExtras(args);
                                            startActivity(i);
                                        }
                                    });

                                    recyclerView.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}
