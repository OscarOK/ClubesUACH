package mx.uach.clubes.Utils;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.uach.clubes.Posts.AuthorPost;
import mx.uach.clubes.Posts.Post;
import mx.uach.clubes.clubs.Club;

public abstract class PostsDBUtils {
    private static final String COLLECTION_POSTS = "posts";
    private static final String COLLECTION_CLUBS = "clubs";

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void getClubs(final ArrayList<String> clubsId, final OnSuccessLoadClubs onSuccessLoadClubs) {
        CollectionReference clubRef = db.collection(COLLECTION_CLUBS);
        ArrayList<Task<DocumentSnapshot>> tasks = new ArrayList<>();

        for (final String id : clubsId) {
            DocumentReference document = clubRef.document(id);

            tasks.add(document.get());
        }

        Tasks.whenAllComplete(tasks).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
            @Override
            public void onComplete(@NonNull Task<List<Task<?>>> task) {
                ArrayList<Club> clubs = new ArrayList<>();
                int size = task.getResult().size();

                for (int i = 0; i < size; i++) {
                    DocumentSnapshot documentSnapshot = (DocumentSnapshot) task.getResult().get(i).getResult();
                    Club club = documentSnapshot.toObject(Club.class);
                    club.setId(documentSnapshot.getId());
                    clubs.add(club);
                }

                onSuccessLoadClubs.onSuccessLoadClubs(clubs);
            }
        });
    }

    public static void getClubsPosts(ArrayList<Club> clubs, final OnSuccessLoadPosts onSuccessLoadPosts) {
        CollectionReference postsRef = db.collection(COLLECTION_POSTS);
        ArrayList<Task<QuerySnapshot>> tasks = new ArrayList<>();
        final Map<String, String> names = new HashMap<>();

        for (Club club : clubs) {
            names.put(club.getId(), club.getName());
        }

        for (Club club : clubs) {
            Query query = postsRef.whereEqualTo("clubId", club.getId()).limit(11);
            tasks.add(query.get());
        }

        Tasks.whenAllComplete(tasks).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
            @Override
            public void onComplete(@NonNull Task<List<Task<?>>> task) {
                ArrayList<AuthorPost> posts = new ArrayList<>();
                int size = task.getResult().size();

                for (int i = 0; i < size; i++) {
                    QuerySnapshot snapshot = (QuerySnapshot) task.getResult().get(i).getResult();

                    for (DocumentSnapshot documentSnapshot : snapshot.getDocuments()) {
                        AuthorPost post = documentSnapshot.toObject(AuthorPost.class);

                        post.setClubName(names.get(post.getClubId()));
                        posts.add(post);
                    }
                }

                Collections.sort(posts);
                onSuccessLoadPosts.onSuccessLoadPosts(posts);
            }
        });
    }

public interface OnSuccessLoadClubs {
        public void onSuccessLoadClubs(ArrayList<Club> clubs);


    }

    public interface OnSuccessLoadPosts {
        public void onSuccessLoadPosts(ArrayList<AuthorPost> posts);
    }
}
