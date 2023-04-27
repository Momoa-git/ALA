package com.example.ala.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ala.R;
import com.example.ala.model.object.Product;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    Context context;
    ArrayList<Product> list;
    ArrayList<Product> list2;
    int count = 0;
    private OnDetailListener onDetailListener;




    public ProductAdapter(Context context, ArrayList<Product> list, ArrayList<Product> list2, OnDetailListener onDetailListener) {
        this.context = context;
        this.list = list;
        this.list2 = list2;
        this.onDetailListener = onDetailListener;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_products, parent,false);
        return  new MyViewHolder(view, onDetailListener);

    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = list.get(position);
        holder.name.setText(product.getName());
        holder.price.setText(product.getPrice());
        holder.bar_code.setText(product.getBar_code());
        holder.place.setText(product.getLine() + "/" + product.getPlace());




        for (Product list2 : list2) {
            if (list2.getBar_code().equals(product.getBar_code())) {
                count++;
            }
        }
        holder.piece.setText(count + " ks");

        count = 0;

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name, price,piece, bar_code, place;
        OnDetailListener onDetailListener;

        public MyViewHolder(@NonNull View itemView, OnDetailListener onDetailListener) {
            super(itemView);

            name = itemView.findViewById(R.id.txt_name);
            price = itemView.findViewById(R.id.txt_price);
            piece = itemView.findViewById(R.id.txt_piece);
            bar_code = itemView.findViewById(R.id.txt_bar_code);
            place = itemView.findViewById(R.id.txt_place);
            this.onDetailListener = onDetailListener;


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onDetailListener.onDetailClick(getAdapterPosition());
        }
    }

     public interface OnDetailListener{
        void onDetailClick(int position);
     }

}
