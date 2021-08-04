package com.genius.appdesign2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AccountActivity extends AppCompatActivity {

    private TextView tvUsename, tvEmail, tvWebsite;
    private Button button;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        getSupportActionBar().hide();

        tvEmail = findViewById(R.id.activity_account_tvEmail);
        tvUsename = findViewById(R.id.activity_account_tvUsername);
        tvWebsite = findViewById(R.id.activity_account_tvWebsite);
        button = findViewById(R.id.activity_account_bLogOut);


        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    firebaseAuth.signOut();
                    finish();
            }
        });
        tvWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://lyodata.web.app/"));
                startActivity(intent);
            }
        });

        if (firebaseAuth.getCurrentUser() != null){
            firestore.collection("users")
                    .whereEqualTo("email", firebaseAuth.getCurrentUser().getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.getData().get("email") != null && document.getData().get("username") != null) {
                                        tvEmail.setText("Email ID : " + document.getData().get("email").toString());
                                        tvUsename.setText("Username : " + document.getData().get("username").toString());

                                    }else {
                                        Log.e("Account", "Missing Parameters");
                                    }
                                }
                            }else {
                                Log.e("HomeActivity", "Error");
                            }
                        }
                    });
        }


    }
}