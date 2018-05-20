package com.example.android.delivery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.android.delivery.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by ASUS on 07/05/2018.
 */

public class FirebaseMethods {


        private static final String TAG = "FirebaseMethods";

        //firebase
        private FirebaseAuth mAuth;
        private FirebaseAuth.AuthStateListener mAuthListener;
        private FirebaseDatabase mFirebaseDatabase;
        private DatabaseReference myRef;
        private StorageReference mStorageReference;
        private String userID;

        //vars
        private Context mContext;
        private double mPhotoUploadProgress = 0;

        public FirebaseMethods(Context context) {

            mAuth = FirebaseAuth.getInstance();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            myRef = mFirebaseDatabase.getReference();
            mStorageReference = FirebaseStorage.getInstance().getReference();
            mContext = context;

            if(mAuth.getCurrentUser() != null){
                userID = mAuth.getCurrentUser().getUid();
            }
        }

        /**
         * Update 'user_account_settings' node for the current user
         * @param displayName
         * @param phoneNumber
         */
        public void updateUserAccountSettings(String displayName, String boxNumber, long phoneNumber){

            Log.d(TAG, "updateUserAccountSettings: updating user account settings.");

            if(displayName != null){
                myRef.child(mContext.getString(R.string.dbname_users))
                        .child(userID)
                        .child(mContext.getString(R.string.dbname_username))
                        .setValue(displayName);
            }


            if(boxNumber != null) {
                myRef.child(mContext.getString(R.string.dbname_users))
                        .child(userID)
                        .child(mContext.getString(R.string.dbname_box_number))
                        .setValue(boxNumber);
            }

            if(phoneNumber != 0) {
                myRef.child(mContext.getString(R.string.dbname_users))
                        .child(userID)
                        .child(mContext.getString(R.string.field_phone_number))
                        .setValue(phoneNumber);
            }
        }

        public void updateUsername(String username){
            Log.d(TAG, "updateUsername: upadting username to: " + username);

            myRef.child(mContext.getString(R.string.dbname_users))
                    .child(userID)
                    .child(mContext.getString(R.string.field_username))
                    .setValue(username);
        }




        /**
         * Register a new email and password to Firebase Authentication
         * @param email
         * @param password
         */
        public void registerNewEmail(final String email, String password){
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(mContext, R.string.auth_failed,
                                        Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onComplete: Failed=" + task.getException().getMessage());


                            }
                            else if(task.isSuccessful()){
                                userID = mAuth.getCurrentUser().getUid();
                                Log.d(TAG, "onComplete: Authstate changed: " + userID);
                            }

                        }
                    });
        }


        /**
         * Retrieves the account settings for teh user currently logged in
         * Database: user_acount_Settings node
         * @param dataSnapshot
         * @return
         */
        public User getUserSettings(DataSnapshot dataSnapshot){
            Log.d(TAG, "getUserAccountSettings: retrieving user account settings from firebase.");


/*
            UserAccountSettings settings  = new UserAccountSettings();
*/
            User user = new User();

            for(DataSnapshot ds: dataSnapshot.getChildren()){

                // users node
                Log.d(TAG, "getUserSettings: snapshot key: " + ds.getKey());
                if(ds.getKey().equals(mContext.getString(R.string.dbname_users))) {
                    Log.d(TAG, "getUserAccountSettings: users node datasnapshot: " + ds);

                    user.setUsername(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getUsername()
                    );
                    user.setEmail(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getEmail()
                    );
                    user.setPhone_number(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getPhone_number()
                    );
                    user.setUser_id(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getUser_id()
                    );
                    user.setBox_number(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getBox_number()
                    );

                    Log.d(TAG, "getUserAccountSettings: retrieved users information: " + user.toString());
                }
            }
            return user;

        }

    }

