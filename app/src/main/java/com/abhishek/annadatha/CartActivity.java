package com.abhishek.annadatha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    String totalAmt;
    int onetypePrice=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        databaseReference= FirebaseDatabase.getInstance().getReference().child("CARTW");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView=findViewById(R.id.rltcart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        txtTotal=findViewById(R.id.txtTotal);
        //FirebaseRecyclerOptions<Cart> options= new FirebaseRecyclerOptions.Builder<Cart>().setQuery(databaseReference.child(Prevalent.CurrentonlineUser.getPhone()),Cart.class).build();
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
           protected void onBindViewHolder(@NonNull CartViewHolder holder, int i, @NonNull final Cart cart) {
              // Toast.makeText(CartActivity.this, "Hello", Toast.LENGTH_SHORT).show();
               holder.txtPrice.setText(cart.getPrice());
               holder.txtQuanity.setText(cart.getQuantity());
              // holder.txtPrice.setText(cart.getPrice());
               holder.txtname.setText(cart.getPname());
               //onetypePrice=cart.getPrice()* (cart.getQuantity());
               totalAmt=totalAmt+onetypePrice;
               holder.itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       CharSequence options[]=new CharSequence[]{

                               "Edit",
                               "Remove"

                       };
                       AlertDialog.Builder builder=new AlertDialog.Builder(CartActivity.this);
                       builder.setTitle("Cart Options");
                       builder.setItems(options, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               if(which==0){
                                   //changeProductState(pid);
                                   Intent i=new Intent(CartActivity.this,ProductDetailsActivity.class);
                                   i.putExtra("pid",cart.getPid());
                                   startActivity(i);
                               }
                               if(which==1){
                                   databaseReference.child(Prevalent.CurrentonlineUser.getPhone()).child(cart.getPid()).removeValue().
                                           addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {
                                                   if(task.isSuccessful()){
                                                       Toast.makeText(CartActivity.this, "Product Removed Successfully", Toast.LENGTH_SHORT).show();
                                                   }
                                                   
                                               }
                                           });
                                   
                               }

                           }
                       });
                       builder.show();
                   }
               });
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
        txtTotal.setText(totalAmt);
    }

}
