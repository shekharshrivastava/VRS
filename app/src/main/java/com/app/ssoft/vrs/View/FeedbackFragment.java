package com.app.ssoft.vrs.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.ssoft.vrs.Model.FeedbackData;
import com.app.ssoft.vrs.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Shekahar.Shrivastava on 13-Mar-18.
 */

public class FeedbackFragment extends Fragment {
    private EditText userName;
    private EditText bookingID;
    private EditText feedback;
    private Button submitBTN;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feedback_layout, container, false);
        userName = view.findViewById(R.id.userName);
        bookingID = view.findViewById(R.id.bookingID);
        feedback = view.findViewById(R.id.feedback);
        submitBTN = view.findViewById(R.id.submitBTN);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();

        mDatabase = mFirebaseInstance.getReference("Feedback");
        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = userName.getText().toString();
                String bookingIDNo = bookingID.getText().toString();
                String feedBackValue = feedback.getText().toString();
                String userLoginID = currentUser.getEmail();
                String userId = mDatabase.push().getKey();
                if (!name.isEmpty() && !feedBackValue.isEmpty()) {
                    FeedbackData feedbackData = new FeedbackData(userLoginID, name, bookingIDNo, feedBackValue);
                    mDatabase.child(userId).setValue(feedbackData);
                    Toast.makeText(getActivity(), "Thank you for your feedback", Toast.LENGTH_SHORT).show();
                    userName.setText("");
                    bookingID.setText("");
                    feedback.setText("");
                } else {
                    Toast.makeText(getActivity(), "Please enter all required values", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("FeedBack");
    }
}
