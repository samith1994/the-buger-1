package com.example.bugerbump;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckoutActivity extends AppCompatActivity
{
    Toolbar toolbar;

    private String foodID, foodName, foodPrice;
    private TextView viewFoodName, viewFoodPrice, viewFoodSubTotal, viewFoodTotal;
    private Button orderBtn;

    private DatabaseReference foodDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Checkout");


        Intent intent = getIntent();
        foodID =intent.getStringExtra("foodID");


        foodDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Foods");
        viewFoodName =findViewById(R.id.checkout_food_name);
        viewFoodPrice = findViewById(R.id.checkout_food_price);
        viewFoodSubTotal =findViewById(R.id.checkout_sub_total);
        viewFoodTotal = findViewById(R.id.checkout_total);
        orderBtn = findViewById(R.id.order_button);


        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CheckoutActivity.this, "Do Something!", Toast.LENGTH_SHORT).show();
            }
        });


        //getting food data from the database
        GettingFoodData();
    }

    //getting food data from the database
    private void GettingFoodData()
    {
        foodDatabaseReference.child(foodID).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.hasChild("name"))
                    {
                        foodName = dataSnapshot.child("name").getValue().toString();
                        viewFoodName.setText(foodName);
                    }

                    if(dataSnapshot.hasChild("price"))
                    {
                        foodPrice = dataSnapshot.child("price").getValue().toString();
                        viewFoodPrice.setText("RS "+foodPrice);
                        viewFoodSubTotal.setText("Rs "+foodPrice);
                        viewFoodTotal.setText("Rs "+foodPrice);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}
