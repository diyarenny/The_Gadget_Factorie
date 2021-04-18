package com.example.thegadgetfactorie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ShoppingCart extends AppCompatActivity {
    DatabaseReference ref;
    ArrayList<Product> list;
    RecyclerView recyclerView;
    SearchView searchView;
    FirebaseAuth fAuth;
    ShoppingCartAdapter adapterClass;
    TextView totalValue;
    double total  = 0;
    EditText promoCode;
    Button apply, clear;
    String promocodes[] = {"Gadget10%OFF", "Gadget20%OFF", "Gadget30%OFF", "Gadget40%OFF", "Gadget50%OFF", "Gadget60%OFF", "Gadget70%OFF", "Gadget80%OFF", "Gadget90%OFF"};
    Button proceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        fAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference().child("Cart").child(fAuth.getCurrentUser().getUid());
        recyclerView = findViewById(R.id.rv);
        searchView = findViewById(R.id.searchView);

        // Display total value
        totalValue = findViewById(R.id.totalValue);

        //Promo code
        promoCode = findViewById(R.id.promoCode);
        apply = findViewById(R.id.apply);
        clear = findViewById(R.id.clearPromoCode);

        //Proceed
        proceed = findViewById(R.id.proceed);

        list = new ArrayList<>();
        adapterClass = new ShoppingCartAdapter(list, this);
        recyclerView.setAdapter(adapterClass);


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Checkout.class));
            }
        });

    }

    @Override
    protected void onStart() {

        super.onStart();
        if(ref != null)
        {

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {

                        for(DataSnapshot ds : snapshot.getChildren())
                        {

                            Product userObj = ds.getValue(Product.class);
                            list.add(userObj);
                            adapterClass.notifyItemInserted(list.size()-1);
                            recyclerView.setAdapter(adapterClass);

                        }

                        for (int i = 0; i <= list.size()-1; i++) {
                            total = total + Double.parseDouble(list.get(i).getPrice());
                            totalValue.setText(String.valueOf(total));

                        }

                        clear.setVisibility(View.GONE);
                        apply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (promoCode.getText().toString().equalsIgnoreCase(promocodes[0])) {
                                    double total1 = total;
                                    total1 = total1 - (total1 * 0.10);
                                    totalValue.setText(String.valueOf(total1));
                                    promoCode.setText("");
                                    promoCode.setEnabled(false);
                                    clear.setVisibility(View.VISIBLE);

                                }

                                if (promoCode.getText().toString().equalsIgnoreCase(promocodes[1])) {
                                    double total2 = total;
                                    total2 = total2 - (total2 * 0.20);
                                    totalValue.setText(String.valueOf(total2));
                                    promoCode.setText("");
                                    promoCode.setEnabled(false);
                                    clear.setVisibility(View.VISIBLE);

                                }
                                if (promoCode.getText().toString().equalsIgnoreCase(promocodes[2])) {
                                    double total3 = total;
                                    total3 = total3 - (total3 * 0.30);
                                    totalValue.setText(String.valueOf(total3));
                                    promoCode.setText("");
                                    promoCode.setEnabled(false);
                                    clear.setVisibility(View.VISIBLE);

                                }
                                if (promoCode.getText().toString().equalsIgnoreCase(promocodes[3])) {
                                    double total4 = total;
                                    total4 = total4 - (total4 * 0.40);
                                    totalValue.setText(String.valueOf(total4));
                                    promoCode.setText("");
                                    promoCode.setEnabled(false);
                                    clear.setVisibility(View.VISIBLE);

                                }
                                if (promoCode.getText().toString().equalsIgnoreCase(promocodes[4])) {
                                    double total5 = total;
                                    total5 = total5 - (total5 * 0.50);
                                    totalValue.setText(String.valueOf(total5));
                                    promoCode.setText("");
                                    promoCode.setEnabled(false);
                                    clear.setVisibility(View.VISIBLE);

                                }
                                if (promoCode.getText().toString().equalsIgnoreCase(promocodes[5])) {
                                    double total6 = total;
                                    total6 = total6 - (total6 * 0.60);
                                    totalValue.setText(String.valueOf(total6));
                                    promoCode.setText("");
                                    promoCode.setEnabled(false);
                                    clear.setVisibility(View.VISIBLE);

                                }
                                if (promoCode.getText().toString().equalsIgnoreCase(promocodes[6])) {
                                    double total7 = total;
                                    total7 = total7 - (total7 * 0.70);
                                    totalValue.setText(String.valueOf(total7));
                                    promoCode.setText("");
                                    promoCode.setEnabled(false);
                                    clear.setVisibility(View.VISIBLE);

                                }
                                if (promoCode.getText().toString().equalsIgnoreCase(promocodes[7])) {
                                    double total8 = total;
                                    total8 = total8 - (total8 * 0.80);
                                    totalValue.setText(String.valueOf(total8));
                                    promoCode.setText("");
                                    promoCode.setEnabled(false);
                                    clear.setVisibility(View.VISIBLE);

                                }
                                if (promoCode.getText().toString().equalsIgnoreCase(promocodes[8])) {
                                    double total9 = total;
                                    total9 = total9 - (total9 * 0.90);
                                    totalValue.setText(String.valueOf(total9));
                                    promoCode.setText("");
                                    promoCode.setEnabled(false);
                                    clear.setVisibility(View.VISIBLE);

                                }

                                Toast.makeText(ShoppingCart.this, "You Can Only Use One Promocode", Toast.LENGTH_SHORT).show();

                            }
                        });

                        clear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                totalValue.setText(String.valueOf(total));
                                promoCode.setEnabled(true);
                                clear.setVisibility(View.GONE);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ShoppingCart.this, error.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }
    }
}