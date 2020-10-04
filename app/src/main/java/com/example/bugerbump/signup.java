package com.example.bugerbump;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;

public class signup extends AppCompatActivity {
    TextInputLayout regusername,regemail,regpnum,regpassword;
    Button regbtn;
    ProgressDialog progressDialog;

    DatabaseReference reference;

    FirebaseDatabase rootNode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        regusername=findViewById(R.id.username);
        regemail=findViewById(R.id.email);
        regpnum=findViewById(R.id.phoneNo);
        regpassword=findViewById(R.id.password);
        regbtn=findViewById(R.id.sign_up);


        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");
                if(!validateusername() || !validateemail() || !validatephonenumber() || !validatepassword()){
                    return;
                }

                String username = regusername.getEditText().getText().toString();
                String email = regemail.getEditText().getText().toString();
                String phoneNo = regpnum.getEditText().getText().toString();
                String password = regpassword.getEditText().getText().toString();
                Helper helperClass = new Helper( username, email, phoneNo, password);
                reference.child(phoneNo).setValue(helperClass);
                Toast.makeText(getApplicationContext(),"signup sucessfull",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),userlogin.class));

            }
        });
    }
    private Boolean validateusername(){
        String val=regusername.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";
        if(val.isEmpty()){
            regusername.setError("Field cannot be empty");
            return false;
        }
        else if (val.length()>=10){
            regusername.setError("username too long");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            regusername.setError("White Spaces are not allowed");
            return false;

        }else{
            regusername.setError(null);
            regusername.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validateemail(){
        String val=regemail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(val.isEmpty()){
            regemail.setError("Field cannot be empty");
            return false;
        }else if (!val.matches(emailPattern)) {
            regemail.setError("Invalid email address");
            return false;
        }

        else{
            regemail.setError(null);
            regemail.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validatephonenumber(){
        String val=regpnum.getEditText().getText().toString();
        if(val.isEmpty()){
            regpnum.setError("Field cannot be empty");
            return false;
        }
        else{
            regpnum.setError(null);
            regpnum.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validatepassword(){
        String val=regpassword.getEditText().getText().toString();
        String passwordVal = "^" +

                "(?=.*[a-zA-Z])" +
                "(?=.*[@#$%^&+=])" +
                "(?=\\S+$)" +
                ".{4,}" +
                "$";
        if(val.isEmpty()){
            regpassword.setError("Field cannot be empty");
            return false;
        }else if (!val.matches(passwordVal)) {
            regpassword.setError("Password is too weak");
            return false;
        }
        else{
            regpassword.setError(null);
            regpassword.setErrorEnabled(false);
            return true;
        }

    }


}