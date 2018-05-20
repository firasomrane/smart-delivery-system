package com.example.android.delivery;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.delivery.Models.Box;
import com.example.android.delivery.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.android.delivery.R.id.website;
import static com.example.android.delivery.R.id.x_p;
import static com.example.android.delivery.R.id.y_p;

/**
 * Created by ASUS on 14/05/2018.
 */

public class Profile extends AppCompatActivity {

    //EditProfile Fragment widgets
    private EditText mUsername, mEmail, mPhoneNumber;
    private TextView mBoxNumber, mX, mY;
    private Boolean toastAppered;
    private RelativeLayout relativeLayoutBoxNumber, relativeLayoutPhoneNumber,
            signOutRelativeLayout, relativeVerified;

    //vars
    private Box newBox;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;
    private String userID;

    private static final String TAG = "modifierVotreProfil";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        // mDisplayName = (EditText) findViewById(R.id.display_name);
        mUsername = (EditText) findViewById(R.id.username);
        mBoxNumber = (TextView) findViewById(website);
        mX = (TextView) findViewById(x_p);
        mY = (TextView) findViewById(y_p);
        mPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        relativeLayoutPhoneNumber = (RelativeLayout) findViewById(R.id.relative_lay_phone_number);
        signOutRelativeLayout = (RelativeLayout) findViewById(R.id.relative_lay_log_out);

        mFirebaseMethods = new FirebaseMethods(Profile.this);
        setupFirebaseAuth();
        //getIncomingIntent();


        //back arrow for navigating back to "ProfileActivity"
        ImageView backArrow = (ImageView) findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back to ProfileActivity");
                Profile.this.finish();
            }
        });

        ///save chages
        ImageView checkmark = (ImageView) findViewById(R.id.save_changes);
        checkmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to save changes.");
                saveProfileSettings();
                finish();
            }
        });


    }

   /* private void getIncomingIntent() {
        Intent intent = getIntent();

        //if there is an imageUrl attached as an extra, then it was chosen from the gallery/photo fragment
        if (intent.hasExtra(getString(R.string.selected_image))
                || intent.hasExtra(getString(R.string.selected_bitmap)) ){


            Log.d(TAG, "getIncomingIntent: New incoming imgUrl");
            if (intent.getStringExtra(getString(R.string.return_to_fragment)).equals(getString(R.string.modifierVotreProfilActivity))) {

                if(intent.hasExtra(getString(R.string.selected_image))) {
                    //set the new profile picture
                    FirebaseMethods firebaseMethods = new FirebaseMethods(modifierVotreProfil.this);
                    firebaseMethods.uploadNewPhoto(getString(R.string.profile_photo), "", "", "", null, 0,
                            intent.getStringExtra(getString(R.string.selected_image)),null);
                }
                else if(intent.hasExtra(getString(R.string.selected_bitmap))){
                    //set the new profile picture
                    FirebaseMethods firebaseMethods = new FirebaseMethods(modifierVotreProfil.this);
                    firebaseMethods.uploadNewPhoto(getString(R.string.profile_photo),"","" ,"",null, 0,null,
                            (Bitmap) intent.getParcelableExtra(getString(R.string.selected_bitmap)));
                }
            }
        }

    }*/


    /**
     * Retrieves the data contained in the widgets and submits it to the database
     * Before donig so it chekcs to make sure the username chosen is unqiue
     */
    private void saveProfileSettings() {
        //final String displayName = mDisplayName.getText().toString();
        final String username = mUsername.getText().toString();
        final String boxNumber = mBoxNumber.getText().toString();
        final long phoneNumber = Long.parseLong(mPhoneNumber.getText().toString());


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                toastAppered = false;

                mFirebaseMethods.updateUserAccountSettings(username, boxNumber, phoneNumber);
                if (!toastAppered) {
                    Toast.makeText(Profile.this, "Changes made.", Toast.LENGTH_SHORT).show();
                    toastAppered = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * Check is @param username already exists in teh database
     *
     * @param username
     */
    private void checkIfUsernameExists(final String username) {
        Log.d(TAG, "checkIfUsernameExists: Checking if  " + username + " already exists.");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_users))
                .orderByChild(getString(R.string.field_username))
                .equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {
                    //add the username
                    mFirebaseMethods.updateUsername(username);
                    Toast.makeText(Profile.this, "nom enregistré.", Toast.LENGTH_SHORT).show();

                }
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    if (singleSnapshot.exists()) {
                        Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH: " + singleSnapshot.getValue(User.class).getUsername());
                        Toast.makeText(Profile.this, "Le nom est déja pris.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void setProfileWidgets(User userSettings) {
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.toString());


        signOutRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
            }
        });

        User user = userSettings;
        //set the common widgets
        //mDisplayName.setText(settings.getDisplay_name());
        mUsername.setText(user.getUsername());
        mBoxNumber.setText(user.getBox_number());
        mPhoneNumber.setText(String.valueOf(user.getPhone_number()));
        getBoxAddress(user.getBox_number());


    }

    private void getBoxAddress(final String boxNumber) {
        Log.d(TAG, "getBoxAddress: created");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        newBox = new Box();
        Query query = reference
                .child(getString(R.string.dbname_addresses))
                .child(getString(R.string.dbname_id));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                    //mFollowing.add(singleSnapshot.child(getString(R.string.field_user_id)).getValue().toString());

                    newBox = (singleSnapshot.getValue(Box.class));
                    if(newBox.getId().equals(boxNumber)){
                        Log.d(TAG, "onDataChange: the box is " + singleSnapshot.getValue((Box.class)).toString());
                        mX.setText(newBox.getX());
                        mY.setText(newBox.getY());
                    }


                }
                /*mX.setText(newBox.getX());
                mY.setText(newBox.getY());*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


     /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //retrieve user information from the database

                setProfileWidgets(mFirebaseMethods.getUserSettings(dataSnapshot));

                //retrieve images for the user in question

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

