package com.example.thegadgetfactorie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    //variables
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    DatabaseReference ref;
    ArrayList<Product> list;
    RecyclerView recyclerView;
    SearchView searchView;
    AdapterClass adapterClass;
    CheckBox titleButton, manuButton, categoryButton, a_checkBox, d_checkBox, title_checkBox, manu_checkBox, price_checkBox;
    Button checkoutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        ref = FirebaseDatabase.getInstance().getReference().child("Products");
        recyclerView = findViewById(R.id.rv);
        searchView = findViewById(R.id.searchView);


        //Checkout Button
        checkoutButton = findViewById(R.id.checkoutButton);

        //checkboxes
        titleButton = findViewById(R.id.titleButton);
        manuButton = findViewById(R.id.manuButton);
        categoryButton = findViewById(R.id.categoryButton);

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
        adapterClass = new AdapterClass(list, this);
        recyclerView.setAdapter(adapterClass);

        Button dialogButton = (Button) findViewById(R.id.s_button);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ShoppingCart.class));
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
        }

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
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();

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
            AdapterClass adapterClass = new AdapterClass(mylist, this);
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



    //----------------------------------------------------------------------------------------------------------------------------------

    //main menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    //main menu options
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_logout_btn:
                logOut();
                return true;

            case R.id.action_settings_btn:
                ///startActivity(new Intent(getApplicationContext(), SetupActivity.class));
                return true;

            default:
                return false;

        }
    }

    /*
    //on start if the user is not logged in then user is bought to the login page
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
        }
    }
     */

    //logs out the user and send back to the login page
    private void logOut() {
        firebaseAuth.signOut();
        startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
    }

}
