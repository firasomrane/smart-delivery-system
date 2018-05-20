package com.example.android.delivery;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.delivery.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by ASUS on 21/04/2018.
 */

public class Create_account extends AppCompatActivity{
    private static final String TAG = "Signup";

    Button termine;
    private EditText mEmail;
    private EditText mPswd;
    private EditText mPrenom;
    private EditText mNom;
    private EditText mRpswd;
    private EditText boxNumberEditText;
    private ProgressDialog mProgressDialog1;
    private Context mContext;
    private String email, pswr, rpswr, nom, prenom, username, boxNumberString;
    private String append = "";
    private String userID;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebaseMethods;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        mContext = Create_account.this;
        firebaseMethods = new FirebaseMethods(mContext);
        Log.d(TAG, "onCreate: started.");


        mProgressDialog1 = new ProgressDialog(this);
        mPrenom = (EditText) findViewById(R.id.prenom);
        mNom = (EditText) findViewById(R.id.nom);
        mPswd = (EditText) findViewById(R.id.password);
        mEmail = (EditText) findViewById(R.id.email);
        mRpswd = (EditText) findViewById(R.id.confpassword);
        termine = (Button) findViewById(R.id.buttont);
        boxNumberEditText = (EditText) findViewById(R.id.box_number);

        init();
        setupFirebaseAuth();
    }

    private void init() {
        termine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEmail.getText().toString();
                username = mPrenom.getText().toString() +" "+ mNom.getText().toString();
                pswr = mPswd.getText().toString();
                rpswr = mRpswd.getText().toString();
                boxNumberString = boxNumberEditText.getText().toString();


                if (checkInputs(email, username, pswr, boxNumberString)) {

                    //mProgressBar.setVisibility(View.VISIBLE);
                    //loadingPleaseWait.setVisibility(View.VISIBLE);
                    if (rpswr.equals(pswr)) {
                        firebaseMethods.registerNewEmail(email, pswr);
                        //termine.isEnabled(false);

                    } else {
                        Toast.makeText(mContext, "la confirmation du mot de passe est erroné.", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });
    }

    private boolean checkInputs(String email, String username, String password, String boxString) {
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if (email.equals("") || username.equals("") || password.equals("") ||boxString.equals("")) {
            Toast.makeText(mContext, "Tous les champs doivent être remplis !", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void addNewUser(String email, String username, String boxNumberString){
        User user = new User( userID,  0,  username, boxNumberString, email);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .setValue(user);


    }


    private void killActivity() {
        finish();
    }


 /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    userID = mAuth.getCurrentUser().getUid();
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            addNewUser(email, username, boxNumberString);
                            Intent intent = new Intent(Create_account.this,Dashboard.class);
                            finish();
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }



    @Override
    public void onStart() {
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


