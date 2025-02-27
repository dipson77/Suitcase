package com.example.suitcase;

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

public class Signup extends AppCompatActivity {
    EditText edit_email, edit_username, edit_password, edit_cpassword;
    Button signup_btn;
    TextView login_txt;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edit_email=findViewById(R.id.txt_email);
        edit_username=findViewById(R.id.txt_username);
        edit_password=findViewById(R.id.txt_password);
        edit_cpassword=findViewById(R.id.txt_cpassword);
        signup_btn=findViewById(R.id.btn_signup);
        login_txt=findViewById(R.id.txt_login);
        firebaseAuth=FirebaseAuth.getInstance();

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=edit_email.getText().toString().trim();
                String password=edit_password.getText().toString().trim();
                String cpassword=edit_cpassword.getText().toString().trim();

                if(email.isEmpty()){
                    Toast.makeText(Signup.this,"Please Enter Email", Toast.LENGTH_SHORT).show();
                }
                if (password.isEmpty()){
                    Toast.makeText(Signup.this,"Please Enter Password", Toast.LENGTH_SHORT).show();
                }
                if(cpassword.isEmpty()){
                    Toast.makeText(Signup.this,"Please Enter Confirm Password", Toast.LENGTH_SHORT).show();
                }
                if(password.equals(cpassword)){
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent=new Intent(getApplicationContext(),Login.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(Signup.this,"Signup Completed",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(Signup.this,"Signup Failed", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                }else {
                    Toast.makeText(Signup.this,"Password or Confirm Password doesn't match",Toast.LENGTH_SHORT).show();
                }


            }
        });

        login_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginintent = new Intent(Signup.this, Login.class);
                startActivity(loginintent);
                finish();
            }
        });
    }
}