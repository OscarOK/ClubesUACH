package mx.uach.clubes.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import mx.uach.clubes.R;
import mx.uach.clubes.Utils.UserDBUtils;
import mx.uach.clubes.users.Student;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "fist_name";
    private static final String ARG_PARAM2 = "last_name";
    private static final String ARG_PARAM3 = "enrollment";
    private static final String ARG_PARAM4 = "email";
    private static final String ARG_PARAM5 = "uid";

    private OnFragmentInteractionListener mListener;

    private Student student;

    private boolean editState = false;

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEnrollment;
    private TextView tvEmail;
    private FloatingActionButton fabEditProfile;

    public ProfileFragment() {}

    public static ProfileFragment newInstance(Student student) throws NullPointerException {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, student.getFirstName());
        args.putString(ARG_PARAM2, student.getLastName());
        args.putString(ARG_PARAM3, student.getEnrollment());
        args.putString(ARG_PARAM4, student.getEmail());
        args.putString(ARG_PARAM5, student.getUID());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String firstName = getArguments().getString(ARG_PARAM1);
            String lastName = getArguments().getString(ARG_PARAM2);
            String enrollment = getArguments().getString(ARG_PARAM3);
            String email = getArguments().getString(ARG_PARAM4);
            String uid = getArguments().getString(ARG_PARAM5);

            student = new Student(uid, firstName, lastName, email, enrollment);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Context context = view.getContext();

        etFirstName = view.findViewById(R.id.et_first_name);
        etLastName = view.findViewById(R.id.et_last_name);
        etEnrollment = view.findViewById(R.id.et_enrollment);
        tvEmail = view.findViewById(R.id.tv_email);
        fabEditProfile = view.findViewById(R.id.fab_edit_profile);

        etFirstName.setText(student.getFirstName());
        etLastName.setText(student.getLastName());
        etEnrollment.setText(student.getEnrollment());
        tvEmail.setText(student.getEmail());

        turnOffEditTexts();

        fabEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editState = !editState;
                fabAction(context);
            }
        });
    }

    private void fabAction(Context context) {
        if (editState) {
            turnOnEditTexts();
            fabEditProfile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_done_white_24dp));
        } else {
            if (isValidInput()) {
                turnOffEditTexts();
                fabEditProfile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_edit_white_24dp));
                updateStudent();

            }
        }
    }

    private boolean isValidInput() {
        if (etFirstName.getText().toString().trim().equals("")) {
            etFirstName.setError(getResources().getString(R.string.err_first_name));
            return false;
        }

        return true;
    }

    private void updateStudent() {
        student.setFirstName(etFirstName.getText().toString());
        student.setLastName(etLastName.getText().toString());
        student.setEnrollment(etEnrollment.getText().toString());
    }

    private void turnOffEditTexts() {
        turnOffEditText(etFirstName);
        turnOffEditText(etLastName);
        turnOffEditText(etEnrollment);
    }

    private void turnOnEditTexts() {
        turnOnEditText(etFirstName);
        turnOnEditText(etLastName);
        turnOnEditText(etEnrollment);
    }

    private void turnOffEditText(EditText et) {
        et.setFocusableInTouchMode(false);
        et.setFocusable(false);
        et.setClickable(false);
    }

    private void turnOnEditText(EditText et) {
        et.setFocusableInTouchMode(true);
        et.setFocusable(true);
        et.setClickable(true);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(student);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mListener != null) {
            mListener.onFragmentInteraction(student);
        }
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
        void onFragmentInteraction(Student user);
    }
}
