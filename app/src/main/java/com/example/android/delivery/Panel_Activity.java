package com.example.android.delivery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.delivery.Models.Order;
import com.example.android.delivery.Models.PanelItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ASUS on 14/05/2018.
 */

public class Panel_Activity extends AppCompatActivity implements
        Dashboard.OnLoadMoreItemsListener {

    @Override
    public void onLoadMoreItems() {

        displayMorePhotos();

    }
    private static final String TAG = "Panel_Avtivity";

    private ListView mListView;
    private ImageView backArrow;
    private Button mOrder;

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private  String userID;
    private FirebaseMethods mFirebaseMethods;

    private ArrayList<PanelItem> mPaginatedPhotos;
    private ArrayList<PanelItem> mProductElements;
    public String lastUserId;
    private int mResults;
    private PanelListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panel);

        mOrder = (Button) findViewById(R.id.order);
        mListView = (ListView) findViewById(R.id.panel_item_list);

        mProductElements  = new ArrayList<>();

        //set the user
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        /*if(mAuth.getCurrentUser() != null){

        }*/
        mFirebaseMethods = new FirebaseMethods(Panel_Activity.this);
        /*backArrow = (ImageView) findViewById(R.id.m_icon) ;

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(Panel_Activity.this));
        setupFirebaseAuth();
    }


    private void getProducts(){
        Log.d(TAG, "getProducts: created");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        mProductElements = new ArrayList<>();

        Query query = reference
                .child(getString(R.string.dbname_panel))
                .child(userID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    //Log.d(TAG, "onDataChange: found product: " +singleSnapshot.getValue(Product.class).toString());

                    //mFollowing.add(singleSnapshot.child(getString(R.string.field_user_id)).getValue().toString());
                    mProductElements.add(singleSnapshot.getValue(PanelItem.class));


                }

                /*//get the photos
                getCreatorCaracteristics();*/
                displayProducts();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void displayProducts(){

        Log.d(TAG, "displayProducts: created");
        mPaginatedPhotos = new ArrayList<>();
        if(mProductElements != null){
            try{
                /*int iterations = mCreateursElements.size();

                if(iterations > 40){
                    iterations = 40;
                }

                mResults = 40;
                for(int i = 0; i < iterations; i++){
                    mPaginatedPhotos.add(mCreateursElements.get(i));
                }
                Log.d(TAG, "onDataChange:mFollowing.size the mPaginatedPhotos     " + mPaginatedPhotos);*/

                mAdapter = new PanelListAdapter(Panel_Activity.this, R.layout.panel_item, mProductElements, userID);
                mListView.setAdapter(mAdapter);

            }catch (NullPointerException e){
                //Log.e(TAG, "displayPhotos: NullPointerException: " + e.getMessage() );
            }catch (IndexOutOfBoundsException e){
                //Log.e(TAG, "displayPhotos: IndexOutOfBoundsException: " + e.getMessage() );
            }
        }

        mOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mProductElements.isEmpty()){

                    int totalSum = 0;
                    Order order = new Order();
                    order.setOrder_elements(mProductElements);
                    order.setOrder_time(Calendar.getInstance().getTime().toString());
                    order.setExpected_delevery_time("not yet defined");
                    order.setDelivered(Boolean.FALSE.toString());
                    for (int x=0; x<mProductElements.size(); x++){
                        String priceString= mProductElements.get(x).getProduct_price();
                        String elementNumber = mProductElements.get(x).getProduct_amount();
                        totalSum += Integer.parseInt(priceString.substring(0,priceString.length()-2)) * Integer.parseInt(elementNumber);
                    }

                    order.setOrder_sum(totalSum);

                    String newKey = myRef.child(getString(R.string.dbname_orders))
                            .child(userID)
                            .push().getKey();

                    myRef.child(getString(R.string.dbname_orders))
                            .child(userID)
                            .child(newKey)
                            .setValue(order);

                    myRef.child(Panel_Activity.this.getString(R.string.dbname_panel))
                            .child(userID)
                            .removeValue();
                    Intent intent = new Intent(Panel_Activity.this, Panel_Activity.class);
                    ((Activity)Panel_Activity.this).finish();
                    Panel_Activity.this.startActivity(intent);
                }
                else{
                    Toast.makeText(Panel_Activity.this, "You have no items in the panel to order", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void displayMorePhotos(){
        Log.d(TAG, "displayMorePhotos: displaying more photos");

        try{

            if(mProductElements.size() > mResults && mProductElements.size() > 0){

                int iterations;
                if(mProductElements.size() > (mResults + 10)){
                    //Log.d(TAG, "displayMorePhotos: there are greater than 10 more photos");
                    iterations = 10;
                }else{
                    //Log.d(TAG, "displayMorePhotos: there is less than 10 more photos");
                    iterations = mProductElements.size() - mResults;
                }

                //add the new photos to the paginated results
                for(int i = mResults; i < mResults + iterations; i++){
                    mPaginatedPhotos.add(mProductElements.get(i));
                }
                mResults = mResults + iterations;
                mAdapter.notifyDataSetChanged();
            }
        }catch (NullPointerException e){
            //Log.e(TAG, "displayPhotos: NullPointerException: " + e.getMessage() );
        }catch (IndexOutOfBoundsException e){
            //Log.e(TAG, "displayPhotos: IndexOutOfBoundsException: " + e.getMessage() );
        }
    }









    public boolean notEmpty(ArrayList a) {
        return !a.isEmpty();
    }



    /**************************Firebase****************/

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        //Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //check if the user is logged in

                if (user != null) {
                    // User is signed in
                    userID = mAuth.getCurrentUser().getUid();
                    getProducts();

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    // toastMessage("Successfully signed out.");
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

    private void toastMessage(String message){
        Toast.makeText(Panel_Activity.this,message,Toast.LENGTH_SHORT).show();
    }
}
