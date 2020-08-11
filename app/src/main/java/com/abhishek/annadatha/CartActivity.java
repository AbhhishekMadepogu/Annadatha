package com.abhishek.annadatha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.abhishek.annadatha.Model.Cart;
import com.abhishek.annadatha.Model.Products;
import com.abhishek.annadatha.Prevalent.Prevalent;
import com.abhishek.annadatha.ViewHolder.CartViewHolder;
import com.abhishek.annadatha.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Button btnCheckout;
    TextView txtTotal;
    String phone=Prevalent.CurrentonlineUser.getPhone();
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        databaseReference= FirebaseDatabase.getInstance().getReference().child("CARTW");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView=findViewById(R.id.rltcart);
        txtTotal=findViewById(R.id.txtTotal);
        FirebaseRecyclerOptions<Cart> options= new FirebaseRecyclerOptions.Builder<Cart>().setQuery(databaseReference.child(Prevalent.CurrentonlineUser.getPhone()),Cart.class).build();
        btnCheckout=findViewById(R.id.btnCheckout);
        onStart();
       // Toast.makeText(this, ""+databaseReference.child(Prevalent.CurrentonlineUser.getPhone()).getDatabase(), Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
        FirebaseRecyclerOptions<Cart> options= new FirebaseRecyclerOptions.Builder<Cart>().setQuery(databaseReference.child(Prevalent.CurrentonlineUser.getPhone()),Cart.class).build();
       FirebaseRecyclerAdapter<Cart,CartViewHolder> adapter=new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
           @Override
           protected void onBindViewHolder(@NonNull CartViewHolder holder, int i, @NonNull Cart cart) {
              // Toast.makeText(CartActivity.this, "Hello", Toast.LENGTH_SHORT).show();
               holder.txtPrice.setText(cart.getPrice());
               holder.txtQuanity.setText(cart.getQuantity());
              // holder.txtPrice.setText(cart.getPrice());
               holder.txtname.setText(cart.getPname());
           }

           @NonNull
           @Override
           public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               Toast.makeText(CartActivity.this, "Hello", Toast.LENGTH_SHORT).show();
               View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);
               CartViewHolder holder=new CartViewHolder(view);

               return holder;
           }
       };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}
