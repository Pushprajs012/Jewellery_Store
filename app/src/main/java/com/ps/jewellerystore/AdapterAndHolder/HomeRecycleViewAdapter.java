package com.ps.jewellerystore.AdapterAndHolder;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ps.jewellerystore.Data.CartData;

import com.ps.jewellerystore.Data.JewelleryData;
import com.ps.jewellerystore.UI.ProductDetailAcivity;
import com.ps.jewellerystore.databinding.RowLayoutProdectBinding;

import java.util.List;

public class HomeRecycleViewAdapter extends RecyclerView.Adapter<HomeHolder> {

    private List<JewelleryData> jewelleryDataList;
    private FirebaseDatabase firebaseDatabase;
    private String uid;

    // Corrected Constructor Name
    public HomeRecycleViewAdapter(List<JewelleryData> jewelleryDataList, FirebaseDatabase firebaseDatabase, String uid) {
        this.jewelleryDataList = jewelleryDataList;
        this.firebaseDatabase = firebaseDatabase;
        this.uid = uid;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowLayoutProdectBinding binding = RowLayoutProdectBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new HomeHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
        JewelleryData jewelleryData = jewelleryDataList.get(position);

        // Binding data to views
        holder.binding.productName.setText(jewelleryData.getName());
        holder.binding.productPrice.setText("$"+jewelleryData.getPrice());
        holder.binding.productSize.setText("Weight : "+jewelleryData.getWeight());
        Glide.with(holder.binding.productImage.getContext())
                .load(jewelleryData.getImage())
                .into(holder.binding.productImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ProductDetailAcivity.class);
            intent.putExtra("jewelleryData", jewelleryData.getProduct_id());
            holder.itemView.getContext().startActivity(intent);
        });

        holder.binding.btncart.setOnClickListener(v -> {
            //check to data is already in cart
            firebaseDatabase.getReference("Users")
                    .child(uid)
                    .child("Cart")
                    .child(jewelleryData.getProduct_id())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(holder.itemView.getContext(), "Product already in cart", Toast.LENGTH_SHORT).show();

                    } else {
                        CartData cartData=new CartData(jewelleryData.getProduct_id(),jewelleryData.getName(),jewelleryData.getPrice(),jewelleryData.getWeight(),jewelleryData.getImage(),1);
                        firebaseDatabase.getReference("Users").child(uid).child("Cart").child(jewelleryData.getProduct_id()).setValue(cartData).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(holder.itemView.getContext(), "Product added to cart", Toast.LENGTH_SHORT).show();
                            }
                        });
                         }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        });
    }

    @Override
    public int getItemCount() {

        return jewelleryDataList.size();
    }
}
