package mx.uach.clubes.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import mx.uach.clubes.AddClubActivity;
import mx.uach.clubes.PostActivity;
import mx.uach.clubes.Posts.AuthorPost;
import mx.uach.clubes.R;
import mx.uach.clubes.Utils.PostsDBUtils;
import mx.uach.clubes.Utils.UserDBUtils;
import mx.uach.clubes.adapters.ClubAdapter;
import mx.uach.clubes.adapters.ClubsAdapter;
import mx.uach.clubes.adapters.PostsAdapter;
import mx.uach.clubes.clubs.Club;
import mx.uach.clubes.users.Student;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyClubsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyClubsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyClubsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String uid;

    private ClubAdapter adapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private OnFragmentInteractionListener mListener;

    private RecyclerView rvClubs;
    private ProgressBar progressBar;

    private ArrayList<Club> clubsList;


    public MyClubsFragment() {}


    public static MyClubsFragment newInstance(String uid) {
        MyClubsFragment fragment = new MyClubsFragment();
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_my_clubs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fabAddClub = view.findViewById(R.id.fab_add_club);
        progressBar = view.findViewById(R.id.pb_loading_clubs);
        rvClubs = view.findViewById(R.id.rv_clubs);
        rvClubs.setVisibility(View.GONE);
        rvClubs.setLayoutManager(new LinearLayoutManager(view.getContext()));

        final Context context = view.getContext();

        fabAddClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddClubActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CollectionReference collections = db.collection("students/" + uid + "/clubs");

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
                        clubsList = clubs;
                        adapter = new ClubAdapter(clubs);
                        if(adapter.getItemCount() > 0){
                            adapter.notifyDataSetChanged();
                            rvClubs.setAdapter(adapter);
                        }
                        rvClubs.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
