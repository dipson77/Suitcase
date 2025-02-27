package com.example.suitcase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Login extends AppCompatActivity {
    EditText login_email, login_password;
    Button login_btn;
    TextView signup_txt, forget_password;
    FirebaseAuth firebaseAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Initialize UI components


        login_email=findViewById(R.id.txt_email);
        login_password=findViewById(R.id.txt_password);
        login_btn=findViewById(R.id.btn_login);
        signup_txt=findViewById(R.id.txt_signup);
        forget_password=findViewById(R.id.txt_forget_password);
        firebaseAuth=FirebaseAuth.getInstance();

        //Setup login button click listener
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email =login_email.getText().toString().trim();
                String password=login_password.getText().toString().trim();

                if(email.isEmpty()){
                    Toast.makeText(Login.this,"Please Enter Email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.isEmpty()){
                    Toast.makeText(Login.this,"Please Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Firebase authentication for Login
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent= new Intent(Login.this, Manage_Items.class);
                            startActivity(intent);
                            Toast.makeText(Login.this,"Login Completed",Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(Login.this,"Login Failed"  + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

        //Setup signup text ClickListener

        signup_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });

        //Setup ForgetPassword text ClickListener
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgetintent=new Intent(Login.this, Forget_Password.class);
                startActivity(forgetintent);
            }
        });
    }
}