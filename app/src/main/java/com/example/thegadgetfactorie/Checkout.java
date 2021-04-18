package com.example.thegadgetfactorie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Checkout extends AppCompatActivity {

    EditText addresLine1, AddressLine2, country, eircode, CardName, cardNumber, expiryDate, cvv;
    Button checkout;
    ArrayList<Product> list;
    String category, image, manufacturer, price, stock, title;
    String orderKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        int min = 50;
        int max = 100;
        int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
        orderKey = String.valueOf(random_int);

        addresLine1 = findViewById(R.id.addresLine1);
        AddressLine2 = findViewById(R.id.AddressLine2);
        country = findViewById(R.id.country);
        eircode = findViewById(R.id.eircode);
        CardName = findViewById(R.id.CardName);
        cardNumber = findViewById(R.id.cardNumber);
        expiryDate = findViewById(R.id.expiryDate);
        cvv = findViewById(R.id.cvv);
        checkout = findViewById(R.id.checkout);


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOrderHistory(v);
                getCart();
            }
        });



    }


    public void getCart() {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = rootRef.child("Cart").child(uid);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    category = dataSnapshot.child(key).child("category").getValue(String.class);
                    image = dataSnapshot.child(key).child("image").getValue(String.class);
                    manufacturer = dataSnapshot.child(key).child("manufacturer").getValue(String.class);
                    price = dataSnapshot.child(key).child("price").getValue(String.class);
                    stock = dataSnapshot.child(key).child("stock").getValue(String.class);
                    title = dataSnapshot.child(key).child("title").getValue(String.class);
                    adjustStock(title);
                    setDate(key, category, image, manufacturer, price, stock, title);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage(); //Don't ignore errors!
            }
        };
        uidRef.addListenerForSingleValueEvent(valueEventListener);

        uidRef.removeValue();
    }


    public void adjustStock(final String name) {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = rootRef.child("Products");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    String title = dataSnapshot.child(key).child("title").getValue(String.class);
                    if (name.equalsIgnoreCase(title)) {
                        String stock = dataSnapshot.child(key).child("stock").getValue(String.class);
                        int stockInt = Integer.parseInt(stock) - 1;
                        DatabaseReference uidRef = dataSnapshot.getRef();
                        uidRef.child(key).child("stock").setValue(String.valueOf(stockInt));
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

    public void setDate(final String key, final String category, final String image, final String manufacturer, final String price, final String stockLevel, final String title) {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = rootRef.child("OrderHistory").child(uid).child(orderKey);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                DatabaseReference uidRef = dataSnapshot.child(key).getRef();
                uidRef.child("category").setValue(category);
                uidRef.child("image").setValue(image);
                uidRef.child("manufacturer").setValue(manufacturer);
                uidRef.child("price").setValue(price);
                uidRef.child("stockLevel").setValue(stockLevel);
                uidRef.child("title").setValue(title);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage(); //Don't ignore errors!
            }
        };
        uidRef.addListenerForSingleValueEvent(valueEventListener);



    }


    public void setOrderHistory(View view) {


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = rootRef.child("OrderHistory").child(uid).child(orderKey);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                DatabaseReference uidRef = dataSnapshot.child("Shipping").getRef();
                uidRef.child("addresLine1").setValue(addresLine1.getText().toString());
                uidRef.child("AddressLine2").setValue(AddressLine2.getText().toString());
                uidRef.child("country").setValue(country.getText().toString());
                uidRef.child("eircode").setValue(eircode.getText().toString());
                uidRef = dataSnapshot.child("Payment").getRef();
                uidRef.child("CardName").setValue(CardName.getText().toString());
                uidRef.child("cardNumber").setValue(cardNumber.getText().toString());
                uidRef.child("expiryDate").setValue(expiryDate.getText().toString());
                uidRef.child("cvv").setValue(cvv.getText().toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage(); //Don't ignore errors!
            }
        };
        uidRef.addListenerForSingleValueEvent(valueEventListener);



    }
}