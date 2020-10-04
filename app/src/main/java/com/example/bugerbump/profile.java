package com.example.bugerbump;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class profile extends AppCompatActivity {
    TextInputLayout prousername,proemail,prophoneNO,propassword;
    Button proupdate,prodelete,aleart;
    String _username,_email,_phoneNo,_password;
    DatabaseReference reference;
    ImageView signoutLogo,aleartimg;
    Dialog dialog;

    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        prousername=findViewById(R.id.pro_username);
        proemail=findViewById(R.id.pro_email);
        prophoneNO=findViewById(R.id.pro_pnum);
        propassword=findViewById(R.id.pro_password);
        proupdate=findViewById(R.id.pro_update);
        signoutLogo =findViewById(R.id.signoutlogo);
        prodelete=findViewById(R.id.pro_delete);
        reference= FirebaseDatabase.getInstance().getReference("users");
        firebaseAuth=FirebaseAuth.getInstance();
        dialog=new Dialog(this);




        prodelete.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               showprodelete();

            }
       });
        signoutLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(profile.this,userlogin.class));
            }
        });


        showUserData();
    }

    private void showprodelete() {
        dialog.setContentView(R.layout.deletealert);
        aleartimg= (ImageView) dialog.findViewById(R.id.wrong);
        aleart=(Button) dialog.findViewById(R.id.deletealeart);
        aleartimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        aleart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child("phoneNo");
                reference.removeValue();
                startActivity(new Intent(profile.this,userlogin.class));
                dialog.dismiss();
            }
        });
    }

    private void showUserData() {
        Intent intent=getIntent();
        _username=intent.getStringExtra("username");
        _email=intent.getStringExtra("email");
        _phoneNo=intent.getStringExtra("phoneNo");
        _password=intent.getStringExtra("password");


        prousername.getEditText().setText(_username);
        proemail.getEditText().setText(_email);
        prophoneNO.getEditText().setText(_phoneNo);
        propassword.getEditText().setText(_password);

    }
    public void update(View view){

        if(isusernamechanged() || ispasswordchanged() || isemailchanged() || isphoneNochanged()){
            Toast.makeText(this,"Data has been updated",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Data is same and cannot be updated", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean ispasswordchanged(){
        if(!_password.equals(propassword.getEditText().getText().toString()))
        {
            reference.child(_phoneNo).child("password").setValue(propassword.getEditText().getText().toString());

            return true;
        }else{
            return false;
        }
    }

    private boolean isusernamechanged() {
        if (!_username.equals(prousername.getEditText().getText().toString())){
            reference.child(_phoneNo).child("username").setValue(prousername.getEditText().getText().toString());
            return true;


        }
        else {
            return false;
        }
    }

    private  boolean isemailchanged() {
        if (!_email.equals(proemail.getEditText().getText().toString())){
            reference.child(_phoneNo).child("email").setValue(proemail.getEditText().getText().toString());
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isphoneNochanged() {
        if (!_phoneNo.equals(prophoneNO.getEditText().getText().toString())){
            reference.child(_phoneNo).child("phoneNo").setValue(prophoneNO.getEditText().getText().toString());
            return true;


        }
        else {
            return false;
        }
    }



}