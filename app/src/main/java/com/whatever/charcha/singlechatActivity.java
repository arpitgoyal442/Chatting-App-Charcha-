package com.whatever.charcha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class singlechatActivity extends AppCompatActivity {

   private String  receiver_uid;
     private TextView message;
     private ImageButton send;
     private  FirebaseAuth auth;
     private RecyclerView recyclerView_chats;
     private ArrayList<MessageModel> allmessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlechat);

        Toolbar t=findViewById(R.id.singlechattoolbar);
        setActionBar(t);








        allmessages=new ArrayList<>();
        recyclerView_chats=findViewById(R.id.recyclerview_chats);
        CustomAdapter customAdapter=new CustomAdapter(this,allmessages);
        recyclerView_chats.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_chats.setAdapter(customAdapter);

        message=findViewById(R.id.message);
        send=findViewById(R.id.btn_send);

       
        auth=FirebaseAuth.getInstance();
        receiver_uid= getIntent().getStringExtra("receiver_uid");


        String receivername=getIntent().getStringExtra("receiver_name");



        FirebaseDatabase.getInstance().getReference().child("Chats").child(gettotaluid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                   String sender=snapshot.child("sender").getValue().toString();
                   String m=snapshot.child("message").getValue().toString();

                   if(sender.equals(auth.getCurrentUser().getDisplayName()))
                       allmessages.add(new MessageModel(m,2));

                   else allmessages.add(new MessageModel(m,1));

                   customAdapter.notifyDataSetChanged();
                   recyclerView_chats.scrollToPosition(allmessages.size()-1);



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void onclksend(View view) {

        String  m=message.getText().toString();
        message.setText("");

        String totaluid=gettotaluid();
        HashMap<String ,String> map=new HashMap<>();
        map.put("sender",auth.getCurrentUser().getDisplayName());
        map.put("message",m);

        FirebaseDatabase.getInstance().getReference().child("Chats").child(gettotaluid()).push().setValue(map);

    }

    private String gettotaluid()
    {
        String s1=auth.getCurrentUser().getUid();
        String s2=receiver_uid;




        if(s1.compareTo(s2)==0||s1.compareTo(s2)>0)
            return s1+s2;
        else return s2+s1;

    }
}