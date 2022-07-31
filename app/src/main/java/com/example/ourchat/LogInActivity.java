package com.example.ourchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth fAuth;
    private FirebaseDatabase fDatabase;
    private DatabaseReference dReference;

    private EditText email, pass;
    private TextView signUpText;
    private Button logInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.label_log_in);

        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        dReference = fDatabase.getReference("users");

        email = findViewById(R.id.logInEmail);
        pass = findViewById(R.id.logInPass);
        logInButton = findViewById(R.id.logInButton);
        signUpText = findViewById(R.id.logInSignUpText);

        logInButton.setOnClickListener(this);
        signUpText.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.logInSignUpText){
            startActivity(new Intent(this,SignUpActivity.class));
        }

        if(v.getId()==R.id.logInButton){
            String stringEmail = email.getText().toString().trim();
            String stringPass = pass.getText().toString().trim();

            if(stringEmail.isEmpty()){
                email.setError("You can't leave blank");
                email.requestFocus();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(stringEmail).matches()){
                email.setError("Enter valid Email ID");
                email.requestFocus();
                return;
            }
            if(stringPass.isEmpty()){
                pass.setError("You can't leave blank");
                pass.requestFocus();
                return;
            }
            if(stringPass.length()<6){
                pass.setError("Enter at least 6 digit password");
                pass.setText("");
                pass.requestFocus();
                return;
            }

            fAuth.signInWithEmailAndPassword(stringEmail, stringPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        startActivity(new Intent(LogInActivity.this, MainActivity.class));
                        finish();
                        Toast.makeText(LogInActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LogInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialogExit = new AlertDialog.Builder(LogInActivity.this);

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
}