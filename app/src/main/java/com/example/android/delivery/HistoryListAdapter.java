package com.example.android.delivery;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.delivery.Models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by ASUS on 16/05/2018.
 */

public class HistoryListAdapter extends ArrayAdapter<Order> {

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

    public HistoryListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Order> objects, String userId) {
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
    TextView orderTime, deleviryTime, orderPrice;
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

            holder.orderTime = (TextView) convertView.findViewById(R.id.order_time_text);
            holder.orderPrice = (TextView) convertView.findViewById(R.id.delivery_price_text);
            holder.deleviryTime = (TextView) convertView.findViewById(R.id.delivery_time_text);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
            //Log.d(TAG, "getView: 5démt");
        }

        Log.d(TAG, "onDataChange:mFollowing.size the item number " +position + "   is  " + getItem(position));

       /* final String product_name_text = getItem(position).getProduct_name();
        final String product_price_text = getItem(position).getProduct_price();
        final String product_id = getItem(position).getProduct_id().toString();
        final String product_amount = getItem(position).getProduct_amount().toString();*/



       /* Log.d(TAG, "getView: the item is" +product_name_text.toString());*/
        holder.orderTime.setText(getItem(position).getOrder_time());
        holder.orderPrice.setText(String.valueOf(getItem(position).getOrder_sum()));
        holder.deleviryTime.setText(getItem(position).getExpected_delevery_time());



        if(reachedEndOfList(position)){
            loadMoreData();
        }

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
