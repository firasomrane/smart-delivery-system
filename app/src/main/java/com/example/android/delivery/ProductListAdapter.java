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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.delivery.Models.PanelItem;
import com.example.android.delivery.Models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by ASUS on 08/05/2018.
 */

public class ProductListAdapter extends ArrayAdapter<Product> {

    public interface OnLoadMoreItemsListener{
        void onLoadMoreItems();
    }
    OnLoadMoreItemsListener mOnLoadMoreItemsListener;


    String productAmout;
    long productAmoutLong;

    private static final String TAG = "ProductListAdapter";

    private LayoutInflater mInflater;
    private int mLayoutResource;
    private Context mContext;
    private String currentUsername = "";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;
    private String userID;

    public ProductListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Product> objects, String userId) {
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
        TextView productName,productDescription, productPrice, addItemTextView, removeItemTextView, itemAmountTextView;
        Button addToPanel;
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
            holder.addItemTextView = (TextView) convertView.findViewById(R.id.add_item);
            holder.removeItemTextView = (TextView) convertView.findViewById(R.id.remove_item);
            holder.itemAmountTextView = (TextView) convertView.findViewById(R.id.iteam_amount);
            holder.addToPanel = (Button) convertView.findViewById(R.id.add_to_panel);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
            //Log.d(TAG, "getView: 5démt");
        }

        Log.d(TAG, "onDataChange:mFollowing.size the item number " +position + "   is  " + getItem(position));

        final String product_name_text = getItem(position).getProduct_name();
        final String product_price_text = getItem(position).getProduct_price();
        final String product_id = getItem(position).getId().toString();


        Log.d(TAG, "getView: the item is" +product_name_text.toString());
        holder.productName.setText(product_name_text);
        holder.productDescription.setText(getItem(position).getProduct_description());
        holder.productPrice.setText(getItem(position).getProduct_price());


        //set the profile image
        final ImageLoader imageLoader = ImageLoader.getInstance();

        final String image_path = getItem(position).getProduct_image_path();

        imageLoader.displayImage(image_path, holder.mProductImage);

        if(reachedEndOfList(position)){
            loadMoreData();
        }

        holder.addItemTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productAmout = holder.itemAmountTextView.getText().toString();
                productAmout = productAmout.replace(" ","");
                productAmout = productAmout.split("\\.")[0];
                Log.d(TAG, "onClick: montant est "+productAmout.getClass().getName());
                Log.d(TAG, "onClick: the type of  Long.valueOf(montant) is  "+ Long.valueOf(productAmout).getClass().getName());
                productAmoutLong = Long.parseLong(productAmout);
                productAmoutLong++;
                holder.itemAmountTextView.setText(String.valueOf(productAmoutLong));
                productAmout = holder.itemAmountTextView.getText().toString();
            }
        });

        holder.removeItemTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productAmout = holder.itemAmountTextView.getText().toString().split("\\.")[0];
                productAmoutLong = Long.parseLong(productAmout);  
                if(productAmoutLong >=1){
                    productAmoutLong--;
                }

                holder.itemAmountTextView.setText(String.valueOf(productAmoutLong));
                productAmout = holder.itemAmountTextView.getText().toString();
            }
        });

        holder.addToPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Long.parseLong(productAmout) >0){
                    PanelItem panelItem = new PanelItem();

                    panelItem.setProduct_amount(productAmout);
                    panelItem.setProduct_id(product_id);
                    panelItem.setProduct_image_path(image_path);
                    panelItem.setProduct_name(product_name_text);
                    panelItem.setProduct_price(product_price_text);

                    Log.d(TAG, "onClick: added to panel the product " +panelItem.toString());
                    myRef.child(mContext.getString(R.string.dbname_panel))
                            .child(userID)
                            .child(product_id)
                            .setValue(panelItem);

                    productAmoutLong= 0;
                    holder.itemAmountTextView.setText(String.valueOf(productAmoutLong));
                }
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
