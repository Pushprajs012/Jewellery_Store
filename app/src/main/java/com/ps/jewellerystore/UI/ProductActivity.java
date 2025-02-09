package com.ps.jewellerystore.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ps.jewellerystore.AdapterAndHolder.HomeRecycleViewAdapter;
import com.ps.jewellerystore.Application;

import com.ps.jewellerystore.Data.JewelleryData;
import com.ps.jewellerystore.R;

import com.ps.jewellerystore.databinding.ActivityProductBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityProductBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;
    private HomeRecycleViewAdapter adapter;
    private List<JewelleryData> jewelleryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        init();

    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchDataFromFirebase();
       // binding.toolbar.setTitle(auth.getCurrentUser().getDisplayName());
        setname();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jewelleryData =new ArrayList<>();
        adapter=new HomeRecycleViewAdapter(jewelleryData,firebaseDatabase,auth.getCurrentUser().getUid());
        binding.recyclerView.setAdapter(adapter);
    }


    private void init(){
        firebaseDatabase= ((Application) getApplicationContext()).getFirebaseDatabase();
        auth=((Application) getApplicationContext()).getFirebaseAuth();
    }


    @Override
    public void onClick(View v) {


    }

    private void fetchDataFromFirebase(){
        firebaseDatabase.getReference("jewellery_items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jewelleryData.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    JewelleryData jewelleryData = dataSnapshot.getValue(JewelleryData.class);
                    ProductActivity.this.jewelleryData.add(jewelleryData);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here.
      if (item.getItemId()==R.id.cart){
        startActivity(new Intent(ProductActivity.this, CartActivity.class));
      }
      return super.onOptionsItemSelected(item);
    }


    private void setname(){
        firebaseDatabase.getReference("Users").child(auth.getCurrentUser().getUid()).child("User").child("fullName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.toolbar.setTitle(R.string.app_name);
                binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}