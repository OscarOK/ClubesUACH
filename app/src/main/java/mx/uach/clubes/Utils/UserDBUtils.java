package mx.uach.clubes.Utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import mx.uach.clubes.users.Student;

public abstract class UserDBUtils {
    private static final String COLLECTION_STUDENTS = "students";

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void checkNewUser(final FirebaseUser user, final OnOscarListener listener) {
        CollectionReference studentsReference = db.collection(COLLECTION_STUDENTS);

        DocumentReference doc = studentsReference.document(user.getUid());

        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentReference = task.getResult();

                    assert documentReference != null;

                    if (!documentReference.exists()) {
                        pushUser(new Student(user));
                    } else if(listener != null){
                        Student student = documentReference.toObject(Student.class);
                        listener.oscarin(student);
                    }
                }
            }
        });
    }

    public static void pushUser(Student user) {
        CollectionReference studentsReference = db.collection(COLLECTION_STUDENTS);
        studentsReference.document(user.getUID()).set(user);
    }

    public interface OnOscarListener {
        void oscarin(Student student);
    }
}
