package com.example.thegadgetfactorie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Purchases extends AppCompatActivity {

    FirebaseAuth fAuth;
    DatabaseReference reference;

    TextView purchasesText, shippingText, cardText;
    String purchases, shipping, card, mainKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases);

        purchasesText = findViewById(R.id.purchasesText);
        shippingText = findViewById(R.id.shippingText);
        cardText = findViewById(R.id.cardText);
        String username = getIntent().getExtras().getString("username");
        //getKey(username);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = rootRef.child("OrderHistory").child("WfCoAYOyuSdncyQpp6Yd1gQjov32").child("56").child("Item53");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String nameText = dataSnapshot.child("title").getValue(String.class);
                String lnameText = dataSnapshot.child("price").getValue(String.class);


                purchasesText.setText(nameText);
                shippingText.setText(lnameText);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage(); //Don't ignore errors!
            }
        };
        uidRef.addListenerForSingleValueEvent(valueEventListener);
    }

    public void getKey(final String name) {


        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = rootRef.child("Users");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    String title = dataSnapshot.child(key).child("username").getValue(String.class);
                    if (name.equalsIgnoreCase(title)) {
                        mainKey = key;
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage(); //Don't ignore errors!
            }
        };
        uidRef.addListenerForSingleValueEvent(valueEventListener);
    }
}