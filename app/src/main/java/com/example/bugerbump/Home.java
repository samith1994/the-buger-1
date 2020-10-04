package com.example.bugerbump;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Home extends AppCompatActivity
{
    private Toolbar toolbar;
    private ImageView profileBtn;
    private RecyclerView foodList;
    private DatabaseReference foodsDatabaseReference;
    String _username, _email, _phoneNo, _password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Burger Bump");
        profileBtn = findViewById(R.id.profile_btn);

        Intent intent=getIntent();
        _username=intent.getStringExtra("username");
        _email=intent.getStringExtra("email");
        _phoneNo=intent.getStringExtra("phoneNo");
        _password=intent.getStringExtra("password");

        foodList = findViewById(R.id.food_list);
        foodsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Foods");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //define foods order
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        foodList.setLayoutManager(linearLayoutManager);

        //profile icon action
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(Home.this, profile.class);
                profileIntent.putExtra("email",_email);
                profileIntent.putExtra("password",_password);
                profileIntent.putExtra("phoneNo",_phoneNo);
                profileIntent.putExtra("username",_username);
                startActivity(profileIntent);
            }
        });

        LoadFoods();

    }

    //retrieving foods
    private void LoadFoods()
    {
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<foods>()
                .setQuery(foodsDatabaseReference, foods.class)
                .build();

        FirebaseRecyclerAdapter<foods,FoodViewHolder> adapter = new FirebaseRecyclerAdapter<foods, FoodViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull final FoodViewHolder foodViewHolder, int i, @NonNull final foods foods)
            {
                final String foodID = getRef(i).getKey();

                foodViewHolder.foodName.setText(foods.name);
                foodViewHolder.foodPrice.setText("Rs "+foods.price);

                foodViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu(foodID);
                    }
                });
            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_layout, parent, false);
                FoodViewHolder foodViewHolder = new FoodViewHolder(view);
                return foodViewHolder;
            }
        };

        foodList.setAdapter(adapter);
        adapter.startListening();
    }


    public static class FoodViewHolder extends RecyclerView.ViewHolder
    {
        TextView foodName, foodPrice;
        ImageView foodImage;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);

            foodName =itemView.findViewById(R.id.food_name);
            foodPrice =itemView.findViewById(R.id.food_price);
        }
    }

    //home menu
    private void PopupMenu(final String foodID)
    {
        final Dialog homeMenu = new Dialog(this);
        homeMenu.requestWindowFeature(Window.FEATURE_NO_TITLE);
        homeMenu.setContentView(R.layout.home_menu_layout);
        homeMenu.setTitle("Home Menu Window");
        homeMenu.show();
        Window window = homeMenu.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        RelativeLayout cartBtn = (RelativeLayout) homeMenu.findViewById(R.id.add_to_cart_button);
        cartBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent cartIntent = new Intent(Home.this, CartActivity.class);
                cartIntent.putExtra("foodID", foodID);
                cartIntent.putExtra("userPhone", _phoneNo);
                startActivity(cartIntent);
                homeMenu.dismiss();
            }
        });

        RelativeLayout buyBtn = (RelativeLayout) homeMenu.findViewById(R.id.buy_button);
        buyBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent checkoutIntent = new Intent(Home.this, CheckoutActivity.class);
                checkoutIntent.putExtra("foodID", foodID);
                startActivity(checkoutIntent);
                homeMenu.dismiss();
            }
        });
    }

}