package com.example.thegadgetfactorie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.MyViewHolder>{

    ArrayList<Product> list;
    Context context;


    public AdminAdapter(ArrayList<Product> list,Context context ) {

        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_product_holder, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {





        holder.titleView.setText(list.get(i).getTitle());
        holder.manuView.setText(list.get(i).getManufacturer());
        holder.categoryView.setText(list.get(i).getCategory());
        holder.priceView.setText(list.get(i).getPrice());
        holder.stockView.setText(list.get(i).getStockLevel());

        Glide.with(context).load(list.get(i).getImage()).into(holder.image);
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = holder.titleView.getText().toString();
                String manufacturer =  holder.manuView.getText().toString();
                String categoryView =  holder.manuView.getText().toString();
                String price =  holder.priceView.getText().toString();
                String stock =  holder.stockView.getText().toString();
                int position = i;
                addToCart(view, title, manufacturer, categoryView, price,  stock,list.get(i).getImage(), position);
                Toast.makeText(context, "Data Updated", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder  {
        EditText titleView, manuView, categoryView, priceView, stockView;
        ImageView image;
        Button btn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.titleET);
            manuView = itemView.findViewById(R.id.manuET);
            image = itemView.findViewById(R.id.imageViewOrder);
            categoryView = itemView.findViewById(R.id.categoryET);
            priceView = itemView.findViewById(R.id.priceET);
            stockView = itemView.findViewById(R.id.stockET);
            btn = itemView.findViewById(R.id.updateData);

        }

    }

    public void addToCart(View view, final String name, final String manufacturer, final String category, final String price, final String stock, final String image, int position) {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = rootRef.child("Items");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    String title = dataSnapshot.child(key).child("title").getValue(String.class);
                    if (name.equalsIgnoreCase(title)) {
                        DatabaseReference uidRef = dataSnapshot.getRef();
                        uidRef.child(key).child("title").setValue(name);
                        uidRef.child(key).child("manufacturer").setValue(manufacturer);
                        uidRef.child(key).child("category").setValue(category);
                        uidRef.child(key).child("price").setValue(price);
                        uidRef.child(key).child("image").setValue(image);
                        uidRef.child(key).child("stock").setValue(stock);
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

