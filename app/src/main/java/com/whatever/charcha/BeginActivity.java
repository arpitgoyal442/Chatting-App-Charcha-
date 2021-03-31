package com.whatever.charcha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BeginActivity extends AppCompatActivity {

    private Toolbar t;
    GoogleSignInClient mgooglesigninclient;
    FirebaseAuth auth;
    ListView list_users;
    ArrayAdapter adapter;

    ImageView userimage;

    private ArrayList<String> user_uids;

    public ArrayList<String> username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);

        userimage=findViewById(R.id.userimage);

        //
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        t=findViewById(R.id.begin_toolbar);
        setActionBar(t);

        user_uids=new ArrayList<>();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mgooglesigninclient= GoogleSignIn.getClient(this, gso);
        username=new ArrayList<>();

        auth=FirebaseAuth.getInstance();
        FirebaseUser currentuser=auth.getCurrentUser();
        String id=currentuser.getUid();

        ArrayList<Integer> imgids=new ArrayList<>();


        list_users=findViewById(R.id.list_users);
        simpleAdapter adapter=new simpleAdapter(this,username,imgids);

        list_users.setAdapter(adapter);


       String name=currentuser.getDisplayName();
        TextView  txtview=findViewById(R.id.textView3);
        txtview.setText("Hello "+name);






        FirebaseDatabase.getInstance().getReference().child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String n=snapshot.child("name").getValue().toString();
                user_uids.add(snapshot.getKey());
                username.add(n);
                imgids.add(R.drawable.begin);
                adapter.notifyDataSetChanged();
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

        list_users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i=new Intent(BeginActivity.this,singlechatActivity.class);
                i.putExtra("receiver_uid",user_uids.get(position));
                i.putExtra("receiver_name",username.get(position));


                startActivity(i);

            }
        });



    }




    public class simpleAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        private Context mcontext;
        private TextView name;
        private ImageView img;

        private ArrayList<Integer> imgarray;
        private ArrayList<String> namearray;



        public simpleAdapter(Context c, ArrayList<String > t, ArrayList <Integer> i)
        {
            mcontext=c;
            imgarray=i;
            namearray=t;

            layoutInflater=LayoutInflater.from(mcontext);
        }


        @Override
        public int getCount() {
            return namearray.size();
        }

        @Override
        public Object getItem(int position) {
            return namearray.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView==null)
                convertView=layoutInflater.inflate(R.layout.beginactivitycardview,null);

            name=convertView.findViewById(R.id.name_CardView);

            img=convertView.findViewById(R.id.img_Cardview);

            name.setText(namearray.get(position));

            img.setImageResource(imgarray.get(position));


            return convertView;

        }
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        mgooglesigninclient.signOut();
        startActivity(new Intent(this,MainActivity.class));

    }



    }