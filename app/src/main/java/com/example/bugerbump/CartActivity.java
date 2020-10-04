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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private String foodID, foodName, foodPrice;
    private TextView viewFoodName1, viewFoodName2, viewFoodPrice1, viewFoodPrice2, viewFoodQuantity;
    private Button qMinusBtn, qPlusBtn, addCrtBtn;
    private int foodQuantity=1;

    private DatabaseReference foodDatabaseReference, cartDatabaseReference;

    private String userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add to Cart");

        Intent intent = getIntent();
        foodID =intent.getStringExtra("foodID");
        userPhone =intent.getStringExtra("userPhone");

        foodDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Foods");
        cartDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Cart");

        viewFoodName1 =findViewById(R.id.cart_food_name1);
        viewFoodPrice1 = findViewById(R.id.cart_food_price1);
        viewFoodName2 =findViewById(R.id.cart_food_name2);
        viewFoodPrice2 = findViewById(R.id.cart_food_price2);
        viewFoodQuantity = findViewById(R.id.food_quantity);
        qMinusBtn = findViewById(R.id.q_minus_btn);
        qPlusBtn = findViewById(R.id.q_plus_btn);
        addCrtBtn = findViewById(R.id.add_cart_btn);

        qMinusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DecreaseQuantity();
            }
        });

        qPlusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IncreaseQuantity();
            }
        });

        addCrtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FoodAddToCart();
            }
        });

        //getting food data from the database
        GettingFoodData();
    }

    private void DecreaseQuantity()
    {
        if(foodQuantity > 1)
        {
            foodQuantity = foodQuantity - 1;
            viewFoodQuantity.setText(String.valueOf(foodQuantity));
        }
    }

    private void IncreaseQuantity()
    {
        foodQuantity = foodQuantity + 1;
        viewFoodQuantity.setText(String.valueOf(foodQuantity));
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
                        viewFoodName1.setText(foodName);
                        viewFoodName2.setText(foodName +" : 1");
                    }

                    if(dataSnapshot.hasChild("price"))
                    {
                        foodPrice = dataSnapshot.child("price").getValue().toString();
                        viewFoodPrice1.setText("RS "+foodPrice);
                        viewFoodPrice2.setText("Rs "+foodPrice);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void FoodAddToCart()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMMyyyy");
        String date = simpleDateFormat.format(calendar.getTime());
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hhmmss");
        String time = simpleTimeFormat.format(calendar.getTime());


        HashMap cartMap = new HashMap();
        cartMap.put("foodID",foodID);
        cartMap.put("Quantity",String.valueOf(foodQuantity));
        cartMap.put("foodName",foodName);

        cartDatabaseReference.child(userPhone).child(date+time).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task)
            {
                if(task.isSuccessful())
                {
                    Toast.makeText(CartActivity.this, foodName+" added to cart.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String msg = task.getException().getMessage();
                    Toast.makeText(CartActivity.this, "Error : "+msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
