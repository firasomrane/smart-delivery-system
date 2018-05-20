package com.example.android.delivery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.delivery.Models.PanelItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by ASUS on 14/05/2018.
 */

public class PanelListAdapter extends ArrayAdapter<PanelItem> {

    public interface OnLoadMoreItemsListener{
        void onLoadMoreItems();
    }
    OnLoadMoreItemsListener mOnLoadMoreItemsListener;


    String productAmout;
    long productAmoutLong;

    private static final String TAG = "PanelListAdapter";

    private LayoutInflater mInflater;
    private int mLayoutResource;
    private Context mContext;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;
    private String userID;

    public PanelListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<PanelItem> objects, String userId) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutResource = resource;
        this.mContext = context;
        userID = userId;

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
    }

    static class ViewHolder{
        ImageView mProductImage;
        TextView productName,productDescription, productPrice, itemAmountTextView;
        Button deleteFromPanel;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder holder;

        if(convertView == null){
            Log.d(TAG, "getView: convertView == null");
            convertView = mInflater.inflate(mLayoutResource, parent, false);
            holder = new ViewHolder();

            holder.productName = (TextView) convertView.findViewById(R.id.title);
            //Log.d(TAG, "getView: the productName textviw is "+holder.productName);
            holder.mProductImage = (ImageView) convertView.findViewById(R.id.list_image);
            holder.productDescription = (TextView) convertView.findViewById(R.id.item_short_desc);
            holder.productPrice = (TextView) convertView.findViewById(R.id.item_price);
            holder.itemAmountTextView = (TextView) convertView.findViewById(R.id.iteam_amount);
            holder.deleteFromPanel = (Button) convertView.findViewById(R.id.delete_from_panel);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
            //Log.d(TAG, "getView: 5démt");
        }

        Log.d(TAG, "onDataChange:mFollowing.size the item number " +position + "   is  " + getItem(position));

        final String product_name_text = getItem(position).getProduct_name();
        final String product_price_text = getItem(position).getProduct_price();
        final String product_id = getItem(position).getProduct_id().toString();
        final String product_amount = getItem(position).getProduct_amount().toString();



        Log.d(TAG, "getView: the item is" +product_name_text.toString());
        holder.productName.setText(product_name_text);
        holder.productPrice.setText(product_price_text);
        holder.itemAmountTextView.setText(product_amount);


        //set the profile image
        final ImageLoader imageLoader = ImageLoader.getInstance();

        final String image_path = getItem(position).getProduct_image_path();

        imageLoader.displayImage(image_path, holder.mProductImage);

        if(reachedEndOfList(position)){
            loadMoreData();
        }

        holder.deleteFromPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: added to panel the product that have th id "+ product_id );
                myRef.child("panel")
                        .child(userID)
                        .child(product_id)
                        .removeValue();

                Intent intent = new Intent(mContext, Panel_Activity.class);
                ((Activity)mContext).finish();
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    private boolean reachedEndOfList(int position){
        return position == getCount() - 1;
    }

    private void loadMoreData(){

        try{
            mOnLoadMoreItemsListener = (OnLoadMoreItemsListener) getContext();
        }catch (ClassCastException e){
            Log.e(TAG, "loadMoreData: ClassCastException: " +e.getMessage() );
        }

        try{
            mOnLoadMoreItemsListener.onLoadMoreItems();
        }catch (NullPointerException e){
            Log.e(TAG, "loadMoreData: ClassCastException: " +e.getMessage() );
        }
    }
}
