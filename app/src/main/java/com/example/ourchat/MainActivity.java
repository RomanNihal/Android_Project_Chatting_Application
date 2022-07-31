package com.example.ourchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private FirebaseDatabase fDatabase;
    private DatabaseReference dReference;

    List<ModelOfMessage> list;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    Connector connector;

    private EditText message;
    private Button logOut,sendMessage;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.label_homepage);

        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        dReference = fDatabase.getReference();

        logOut = findViewById(R.id.mainLogOutButton);
        message = findViewById(R.id.mainMessage);
        sendMessage = findViewById(R.id.mainSendButton);
        progressBar = findViewById(R.id.mainProgressBar);
        list = new ArrayList<>();

        String mail = fAuth.getCurrentUser().getEmail();

        String trimEmail;
        trimEmail = mail.replace("@","");
        trimEmail = trimEmail.replace(".","");
        String finalTrimEmail = trimEmail;

        setRecycle();
        setData();

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = message.getText().toString();
                if(text.isEmpty()){
                    message.setError("Please write anything");
                    message.requestFocus();
                    return;
                }

                dReference.child("users").child(finalTrimEmail).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ModelOfUsers model = snapshot.getValue(ModelOfUsers.class);

                        String name, pictureLink;
                        name = model.getName();
                        pictureLink = model.getPictureLink();
                        String key = dReference.push().getKey();

                        dReference.child("chats").child(key).setValue(new ModelOfMessage(mail, name, pictureLink, text, key));
                        message.setText("");
                        connector.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                startActivity(new Intent(getApplicationContext(),LogInActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialogExit = new AlertDialog.Builder(MainActivity.this);

        dialogExit.setTitle("EXIT!!");
        dialogExit.setMessage("Are you sure?");

        dialogExit.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                finish();
            }
        });

        dialogExit.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogExit.show();
    }

    private void setRecycle() {
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView = findViewById(R.id.mainRecyclerView);
        recyclerView.setLayoutManager(layoutManager);
        connector = new Connector(list, this);
        recyclerView.setAdapter(connector);
        connector.notifyDataSetChanged();
    }

    private void setData(){
        dReference.child("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                progressBar.setVisibility(View.VISIBLE);
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    ModelOfMessage message=snapshot1.getValue(ModelOfMessage.class);
                    list.add(message);
                }
                recyclerView.scrollToPosition(list.size()-1);
                connector.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}