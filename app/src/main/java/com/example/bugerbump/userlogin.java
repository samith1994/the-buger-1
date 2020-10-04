package com.example.bugerbump;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Objects;

public class userlogin extends AppCompatActivity {
    TextView signupscren;
    TextInputLayout phoneNo, password;
    TextView logoText;
    ImageView image;
    Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);
        signupscren = findViewById(R.id.signup_scre);
        phoneNo = findViewById(R.id.phonenumber);
        password = findViewById(R.id.password);
        logoText = findViewById(R.id.textlogo);
        image = findViewById(R.id.logoimage);
        login_btn = findViewById(R.id.logbutton);

        


        signupscren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(userlogin.this, signup.class);
                Pair[] pairs = new Pair[6];
                pairs[0] = new Pair<View, String>(image, "logo_image");
                pairs[1] = new Pair<View, String>(logoText, "logo_text");
                pairs[2] = new Pair<View, String>(phoneNo, "user_Name");
                pairs[3] = new Pair<View, String>(password, "user_password");
                pairs[4] = new Pair<View, String>(login_btn, "user_btn");
                pairs[5] = new Pair<View, String>(signupscren, "user_signupscre");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(userlogin.this, pairs);
                    startActivity(intent, options.toBundle());
                }
            }
        });


    }


    public void loginUser(View view) {
        //Validate Login Info

       /* Intent profileIntent = new Intent(userlogin.this, Home.class);
        startActivity(profileIntent);*/
        if (!validatephoneNo() | !validatePassword()) {
            return;
        } else {
            isUser();
        }
    }

    private void isUser() {
        final String userEnteredUsername=phoneNo.getEditText().getText().toString().trim();
        final String userEnteredPassword=password.getEditText().getText().toString().trim();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users");
        Query checkuser=reference.orderByChild("phoneNo").equalTo(userEnteredUsername);
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    phoneNo.setError(null);
                    phoneNo.setErrorEnabled(false);

                    String passwordFromDB=snapshot.child(userEnteredUsername).child("password").getValue(String.class);


                    if(Objects.equals(passwordFromDB, userEnteredPassword)){
                        phoneNo.setError(null);
                        phoneNo.setErrorEnabled(false);



                        String emailFromDB=snapshot.child(userEnteredUsername).child("email").getValue(String.class);
                        String usernameFromDB=snapshot.child(userEnteredUsername).child("username").getValue(String.class);
                        String phoneNoFromDB=snapshot.child(userEnteredUsername).child("phoneNo").getValue(String.class);
                        Intent intent=new Intent(getApplicationContext(),Home.class);
                        intent.putExtra("email",emailFromDB);
                        intent.putExtra("password",passwordFromDB);
                        intent.putExtra("phoneNo",phoneNoFromDB);
                        intent.putExtra("username",usernameFromDB);
                        startActivity(intent);



                    }
                    else {
                        password.setError("wrong password");
                        password.requestFocus();
                    }

                }
                else {
                    phoneNo.setError("No such exist");
                    phoneNo.requestFocus();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });














    }
    private Boolean validatephoneNo() {
        String val = phoneNo.getEditText().getText().toString();
        if (val.isEmpty()) {
            phoneNo.setError("Field cannot be empty");
            return false;
        } else {
            phoneNo.setError(null);
            phoneNo.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString();
        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }








}