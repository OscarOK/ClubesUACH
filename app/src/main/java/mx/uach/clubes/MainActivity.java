package mx.uach.clubes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import mx.uach.clubes.Utils.UserDBUtils;
import mx.uach.clubes.fragments.DashboardFragment;
import mx.uach.clubes.fragments.MyClubsFragment;
import mx.uach.clubes.fragments.ProfileFragment;
import mx.uach.clubes.users.Student;

public class MainActivity extends AppCompatActivity implements MyClubsFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener {

    private static final int RC_SIGN_IN = 1;

    private Student student = null;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    try {
                        loadFragment(DashboardFragment.newInstance(student.getUID()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                case R.id.navigation_my_clubs:
                    loadFragment(new MyClubsFragment());
                    return true;
                case R.id.navigation_profile:
                    try {
                        loadFragment(ProfileFragment.newInstance(student));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mFirebaseAuth = FirebaseAuth.getInstance();


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    UserDBUtils.checkNewUser(user, new UserDBUtils.OnSuccessUserListener() {
                        @Override
                        public void onSuccessUserListener(Student student) {
                            MainActivity.this.student = student;

                            try {
                                loadFragment(DashboardFragment.newInstance(student.getUID()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(
                                            Arrays.asList(
                                                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                                                    new AuthUI.IdpConfig.EmailBuilder().build()
                                            )
                                    )
                                    .build(), RC_SIGN_IN);
                }
            }
        };
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();

            return true;
        }

        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void onFragmentInteraction(Student user) {
        student = user;
    }
}
