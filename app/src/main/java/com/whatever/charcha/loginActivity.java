package com.whatever.charcha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {

    private EditText loginusername;
    private EditText loginmailid;
    private EditText loginpassword;
    private Button login_login;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth=FirebaseAuth.getInstance();

        loginusername=findViewById(R.id.login_username);
        loginmailid=findViewById(R.id.login_mailid);
        loginpassword=findViewById(R.id.login_password);

        login_login=findViewById(R.id.login_login);


        login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mail=loginmailid.getText().toString();
                String pass=loginpassword.getText().toString();
                onlogin(mail,pass);
            }
        });
    }

    private void onlogin(String mail,String pass)
    {
        auth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                    startActivity(new Intent(loginActivity.this,BeginActivity.class));

                else Toast.makeText(loginActivity.this, "Maybe Wrong Credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}