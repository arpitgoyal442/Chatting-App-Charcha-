package com.whatever.charcha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class StartActivity extends AppCompatActivity {

    private EditText username;
    private EditText email;
    private EditText password;
    private Button registerme;

     private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Toolbar t=findViewById(R.id.mytoolbar);
        setActionBar(t);



        username =findViewById(R.id.register_username);
        email=findViewById(R.id.register_mailid);
        password=findViewById(R.id.register_password);
        registerme=findViewById(R.id.registerme);

        auth=FirebaseAuth.getInstance();






        registerme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mail=email.getText().toString();
                String pass=password.getText().toString();
                String  name=username.getText().toString();

                onregister(mail,pass,name);

            }
        });



    }

    private  void onregister(String mail,String pass,String name)
    {
        auth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser currentuser=auth.getCurrentUser();
                    String uid=currentuser.getUid();
                    HashMap<String,String> map=new HashMap<>();
                    map.put("uid",uid);
                    map.put("name",name);
                    map.put("image_url","default");

                    FirebaseDatabase.getInstance().getReference().child("Users").child(uid).setValue(map);



                    //

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(StartActivity.this, "Nice Work !!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    //

                    startActivity(new Intent(StartActivity.this,BeginActivity.class));
                }

                else Toast.makeText(StartActivity.this, "bad", Toast.LENGTH_SHORT).show();
            }
        });
    }


}