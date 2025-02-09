package com.ps.jewellerystore.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ps.jewellerystore.Application;
import com.ps.jewellerystore.Data.CartData;
import com.ps.jewellerystore.R;
import com.ps.jewellerystore.databinding.ActivityProductDetailAcivityBinding;

public class ProductDetailAcivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityProductDetailAcivityBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;
    private CartData cartData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailAcivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        getandsetDatatoView();
        checkdataincart();
    }

    private void getandsetDatatoView() {

        firebaseDatabase.getReference("jewellery_items").child(getIntent().getStringExtra("jewelleryData")).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                cartData=new CartData(task.getResult().child("product_id").getValue(String.class),
                        task.getResult().child("name").getValue(String.class),
                        task.getResult().child("price").getValue(Integer.class),
                        task.getResult().child("weight").getValue(String.class),
                        task.getResult().child("image").getValue(String.class),
                       1);

                binding.tvProductName.setText(task.getResult().child("name").getValue(String.class));
                binding.tvProductPrice.setText("$"+task.getResult().child("price").getValue(Integer.class).toString());
                binding.tvAbout.setText(task.getResult().child("about").getValue(String.class));
                binding.tvProductSize.setText("Weight: "+task.getResult().child("weight").getValue(String.class));

                Glide.with(binding.ivProductImage.getContext()).load(task.getResult().child("image").getValue(String.class)).into(binding.ivProductImage);

            }
        });
    }


    private void init() {
        firebaseDatabase = ((Application) getApplicationContext()).getFirebaseDatabase();
        auth = ((Application) getApplicationContext()).getFirebaseAuth();
        binding.btnAddToCart.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
if (v.getId()== R.id.btnAddToCart){

    firebaseDatabase.getReference("Users").child(auth.getCurrentUser().getUid()).child("Cart").child(getIntent().getStringExtra("jewelleryData")).setValue(cartData).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
           startActivity(new Intent(ProductDetailAcivity.this, CartActivity.class));
        }
    });


}
    }

    private void checkdataincart(){
        firebaseDatabase.getReference("Users")
                .child(auth.getCurrentUser().getUid())
                .child("Cart")
                .child(getIntent().getStringExtra("jewelleryData"))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            binding.btnAddToCart.setText("Already In Cart");
                            binding.btnAddToCart.setEnabled(false);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}