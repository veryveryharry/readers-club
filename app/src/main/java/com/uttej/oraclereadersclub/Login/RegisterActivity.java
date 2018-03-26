package com.uttej.oraclereadersclub.Login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uttej.oraclereadersclub.R;
import com.uttej.oraclereadersclub.Utils.FirebaseMethods;

/**
 * Created by Clean on 25-03-2018.
 */

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText mUsername;
    private EditText mRollNumber;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mPasswordConfirmation;
    private Button mRegisterButton;
    private ProgressBar mProgressBar;

    private FirebaseMethods firebaseMethods;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initWidgets();
        firebaseMethods = new FirebaseMethods(RegisterActivity.this);
        setupFirebaseAuth();

        init();
    }

    private void initWidgets(){
        mUsername = (EditText)findViewById(R.id.registerName);
        mRollNumber = (EditText)findViewById(R.id.registerRollNumber);
        mEmail = (EditText)findViewById(R.id.registerEmail);
        mPassword = (EditText)findViewById(R.id.registerPassword);
        mPasswordConfirmation = (EditText)findViewById(R.id.registerConfirmPassword);
        mRegisterButton = (Button)findViewById(R.id.registerButton);
        mProgressBar = (ProgressBar)findViewById(R.id.registeringUserInProgress);

        mProgressBar.setVisibility(View.GONE);

    }

    private void init(){
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsername.getText().toString();
                String rollnumber = mRollNumber.getText().toString();
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                String confirmpassword = mPasswordConfirmation.getText().toString();

                switch(validateFields(username, rollnumber, email, password, confirmpassword)){
                    case 0:
                        firebaseMethods.registerNewUser(username, rollnumber, email, password);
                        break;
                    case 1:
                        Toast.makeText(RegisterActivity.this, "All fields must be filled to proceed", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(RegisterActivity.this, "Username must be between 4 and 15 characters", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(RegisterActivity.this, "Rollnumber must of format: 160215733114", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    //TODO: Add more validations
    private int validateFields(String username,
                                   String rollnumber,
                                   String email,
                                   String password, String confirmpassword){
        if(username.equals("") ||
                rollnumber.equals("") ||
                email.equals("") ||
                password.equals("") ||
                confirmpassword.equals(""))
            return 1;

        if(username.length() < 4 || username.length() > 15 )
            return 2;

        if(!password.equals(confirmpassword))
            return 3;

        if(rollnumber.length() != 12 ||
                !rollnumber.matches("[0-9]+") ||
                !rollnumber.substring(0,4).equals("1602"))
            return 4;

        return 0;
    }

    private void validationTrue(String username,
                                String rollnumber,
                                String email,
                                String password, String confirmpassword){
        mProgressBar.setVisibility(View.VISIBLE);

    }

//    -------------------------FIREBASE--------------------------------
    private void setupFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener(){

        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if(user != null){
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //make sure that username is unique

                        //make sure that rollnumber is unique

                        //make sure that email is unique

                        //add user to the database

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    };
}

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
