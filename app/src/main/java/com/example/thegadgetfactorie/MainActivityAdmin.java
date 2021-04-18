package com.example.thegadgetfactorie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivityAdmin extends AppCompatActivity {

    DatabaseReference ref;
    ArrayList<Product> list;
    RecyclerView recyclerView;
    SearchView searchView;
    FirebaseAuth fAuth;
    AdminAdapter adapterClass;
    CheckBox titleButton, manuButton, categoryButton, a_checkBox, d_checkBox, title_checkBox, manu_checkBox, price_checkBox;
    Button checkoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);



        fAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference().child("Items");
        recyclerView = findViewById(R.id.rvAD);
        searchView = findViewById(R.id.searchViewAD);


        //Checkout Button
        checkoutButton = findViewById(R.id.checkoutButtonAD);


        //checkboxes
        titleButton = findViewById(R.id.titleButtonAD);
        manuButton = findViewById(R.id.manuButtonAD);
        categoryButton = findViewById(R.id.categoryButtonAD);

        //override checkboxes

        titleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    manuButton.setChecked(false);
                    categoryButton.setChecked(false);
                }
            }
        });
        manuButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    categoryButton.setChecked(false);
                    titleButton.setChecked(false);
                }
            }
        });
        categoryButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    manuButton.setChecked(false);
                    titleButton.setChecked(false);
                }
            }
        });

        list = new ArrayList<>();
        adapterClass = new AdminAdapter(list, this);
        recyclerView.setAdapter(adapterClass);

        Button dialogButton = (Button) findViewById(R.id.s_buttonAD);
        dialogButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

       /*checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), ShoppingCart.class));
            }
        });*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ref != null) {

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        for (DataSnapshot ds : snapshot.getChildren()) {

                            Product userObj = ds.getValue(Product.class);
                            list.add(userObj);
                            adapterClass.notifyItemInserted(list.size() - 1);
                            recyclerView.setAdapter(adapterClass);

                        }


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivityAdmin.this, error.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }

        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);

                    return true;
                }
            });

        }

    }

    private void search(String str) {
        ArrayList<Product> mylist = new ArrayList<>();
        for (Product object : list) {

            if(titleButton.isChecked()) {
                if (object.getTitle().toLowerCase().contains(str.toLowerCase())) {
                    mylist.add(object);

                    adapterClass.notifyItemInserted(mylist.size() - 1);

                }
            }

            if(manuButton.isChecked()) {
                if (object.getManufacturer().toLowerCase().contains(str.toLowerCase())) {
                    mylist.add(object);

                    adapterClass.notifyItemInserted(mylist.size() - 1);

                }
            }

            if(categoryButton.isChecked()) {
                if (object.getCategory().toLowerCase().contains(str.toLowerCase())) {
                    mylist.add(object);

                    adapterClass.notifyItemInserted(mylist.size() - 1);

                }


            }
            //recyclerView.setAdapter(adapterClass);
            AdminAdapter adapterClass = new AdminAdapter(mylist, this);
            recyclerView.setAdapter(adapterClass);

        }

    }

    public void showDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        //checkboxes inside dialog (Order)
        a_checkBox = dialog.findViewById(R.id.a_checkBox);
        d_checkBox = dialog.findViewById(R.id.d_checkBox);

        //checkboxes inside dialog (Category)
        title_checkBox = dialog.findViewById(R.id.title_checkBox);
        manu_checkBox = dialog.findViewById(R.id.manu_checkBox);
        price_checkBox = dialog.findViewById(R.id.price_checkBox);


        checkboxesOrder();
        checkboxesCategory();


        Button dialogButton = (Button) dialog.findViewById(R.id.sortButton);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (a_checkBox.isChecked() && title_checkBox.isChecked()) {
                    ascendingTitle();

                }

                else if (d_checkBox.isChecked() && title_checkBox.isChecked()) {

                    descendingTitle();


                }
                else if (a_checkBox.isChecked() && manu_checkBox.isChecked()) {
                    ascendingManufacturing();
                }

                else if (d_checkBox.isChecked() && manu_checkBox.isChecked()) {
                    descendingManufacturing();
                }

                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void ascendingTitle(){

        Collections.sort(list, new Comparator<Product>() {
            @Override
            public int compare(Product lhs, Product rhs) {
                return lhs.getTitle().compareTo(rhs.getTitle());
            }
        });

    }

    public void ascendingManufacturing(){

        Collections.sort(list, new Comparator<Product>() {
            @Override
            public int compare(Product lhs, Product rhs) {
                return lhs.getManufacturer().compareTo(rhs.getManufacturer());
            }
        });

    }

    public void descendingTitle(){

        Collections.sort(list, new Comparator<Product>() {
            @Override
            public int compare(Product lhs, Product rhs) {
                return rhs.getTitle().compareTo(lhs.getTitle());
            }
        });

    }

    public void descendingManufacturing(){

        Collections.sort(list, new Comparator<Product>() {
            @Override
            public int compare(Product lhs, Product rhs) {
                return rhs.getManufacturer().compareTo(lhs.getManufacturer());
            }
        });

    }

    public void checkboxesOrder() {
        a_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    d_checkBox.setChecked(false);

                }
            }
        });

        d_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    a_checkBox.setChecked(false);

                }
            }
        });

    }

    public void checkboxesCategory() {


        title_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    manu_checkBox.setChecked(false);
                    price_checkBox.setChecked(false);

                }
            }
        });

        price_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    manu_checkBox.setChecked(false);
                    title_checkBox.setChecked(false);

                }
            }
        });
        manu_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    price_checkBox.setChecked(false);
                    title_checkBox.setChecked(false);

                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.item1:
                Toast.makeText(this, "Customer Order History", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivityAdmin.this, CustomerDetails.class);
                startActivity(intent);
                return true;
            case R.id.item2:
                Toast.makeText(this,  "Stock Database", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(MainActivityAdmin.this, MainActivityAdmin.class);
                startActivity(intent2);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


}