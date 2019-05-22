package mx.uach.clubes.Utils;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
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

    public static void signInUser(final FirebaseUser user, final OnSuccessUserListener listener) {
        CollectionReference studentsReference = db.collection(COLLECTION_STUDENTS);

        DocumentReference doc = studentsReference.document(user.getUid());

        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot documentReference = task.getResult();

                    assert documentReference != null;

                    if (!documentReference.exists()) {
                        final Student student = new Student(user);
                        CollectionReference studentsReference = db.collection(COLLECTION_STUDENTS);
                        studentsReference.document(student.getUID()).set(student).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(listener != null){
                                    listener.onSuccessUserListener(student);
                                }
                            }
                        });
                    } else if (listener != null) {
                        Student student = documentReference.toObject(Student.class);
                        listener.onSuccessUserListener(student);
                    }
                }
            }
        });
    }

    public static void pushUser(Student user) {
        CollectionReference studentsReference = db.collection(COLLECTION_STUDENTS);
        studentsReference.document(user.getUID()).set(user);
    }

    public static void getUser(String uid, final OnSuccessUserListener onSuccessUserListener) {
        CollectionReference studentsReference = db.collection(COLLECTION_STUDENTS);
        DocumentReference doc = studentsReference.document(uid);

        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentReference = task.getResult();

                    assert documentReference != null;

                    if (documentReference.exists() && onSuccessUserListener != null) {
                        Student student = documentReference.toObject(Student.class);
                        onSuccessUserListener.onSuccessUserListener(student);
                    }
                }
            }
        });
    }

    public interface OnSuccessUserListener {
        void onSuccessUserListener(Student student);
    }
}
