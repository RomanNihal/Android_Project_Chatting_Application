package com.example.ourchat;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth fAuth;
    private FirebaseDatabase fDatabase;
    private DatabaseReference dReference;
    FirebaseStorage fStorage;
    StorageReference sReference;

    private EditText name,email,pass,confirmPass;
    private TextView signInText;
    private ImageView imageView;
    private ProgressBar progressBar;
    private Button imageButton,createButton;

    ActivityResultLauncher<String> activityResultLauncher;
    String pictureLink ="https://www.pngitem.com/pimgs/m/22-220721_circled-user-male-type-user-colorful-icon-png.png?fbclid=IwAR3VIKtfsa7rYCRsLWYKwJ6WS0Ga54kQtLxQAowOiK55zPgD8h7QeMXwr-0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.label_sign_up);

        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        dReference = fDatabase.getReference("users");
        fStorage = FirebaseStorage.getInstance();
        sReference = fStorage.getReference("images");

        name = findViewById(R.id.signUpName);
        email = findViewById(R.id.signUpEmail);
        pass = findViewById(R.id.signUpPass);
        confirmPass = findViewById(R.id.signUpConfirmPass);
        imageView = findViewById(R.id.signUpImage);
        imageButton = findViewById(R.id.signUpImageButton);
        createButton = findViewById(R.id.signUpCreateButton);
        signInText = findViewById(R.id.signUpSignInText);
        progressBar = findViewById(R.id.signUpProgressBar);

        signInText.setOnClickListener(this);
        createButton.setOnClickListener(this);

        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                progressBar.setVisibility(View.VISIBLE);
                createButton.setEnabled(false);
                sReference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    sReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            pictureLink = uri.toString();
                            progressBar.setVisibility(View.INVISIBLE);
                            createButton.setEnabled(true);
                            imageView.setImageURI(result);
                        }
                    });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "Couldn't Upload !!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        activityResultLauncher.launch("image/*");
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.signUpCreateButton){
            String stringName = name.getText().toString();
            String stringEmail = email.getText().toString().trim();
            String stringPass = pass.getText().toString().trim();
            String stringConfirmPass = confirmPass.getText().toString().trim();

            if(stringName.isEmpty()){
                name.setError("You can't leave blank");
                name.requestFocus();
                return;
            }
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
            if(stringConfirmPass.isEmpty()){
                confirmPass.setError("You can't leave blank");
                confirmPass.requestFocus();
                return;
            }
            if(!stringConfirmPass.equals(stringPass)){
                confirmPass.setText("");
                confirmPass.setError("Didn't match");
                confirmPass.requestFocus();
                return;
            }

            String trimEmail;
            trimEmail = stringEmail.replace("@","");
            trimEmail = trimEmail.replace(".","");

            dReference.child(trimEmail).setValue(new ModelOfUsers(stringName, stringEmail, pictureLink));

            fAuth.createUserWithEmailAndPassword(stringEmail, stringPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        fAuth.signOut();
                        Toast.makeText(SignUpActivity.this, "Successfully signed up", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUpActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if(v.getId()==R.id.signUpSignInText){
            startActivity(new Intent(this,LogInActivity.class));
        }
    }
}