package com.example.canteen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class BasketAdapter extends FirebaseRecyclerAdapter<Basket, BasketAdapter.basketViewHolder> {

    private OnItemClickListener onItemClickListener;

    public BasketAdapter(@NonNull FirebaseRecyclerOptions<Basket> options) {
        super(options);
    }

    @Override
    protected void
    onBindViewHolder(@NonNull basketViewHolder holder, int position, @NonNull Basket model) { //Binds data to the views
        holder.mealName.setText(model.getName());
        holder.mealQuantity.setText(model.getQuantity());
        holder.mealImage.setText(model.getUrl());

        //Calculate subtotal and put it into the holder
        float price = Float.parseFloat(model.getPrice());
        int quantity = Integer.parseInt(model.getQuantity());
        float totalPrice = price * quantity;
        holder.mealSubtotal.setText(String.format("%.2f", totalPrice));

    }

    @NonNull
    @Override
    public Basket getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public basketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflating the menu item (basket_item_recycler)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_item, parent, false);
        return new basketViewHolder(view);
    }

    class basketViewHolder extends RecyclerView.ViewHolder {
        private TextView mealName, mealDescription, mealPrice, mealID, mealIngredients, mealQuantity, mealSubtotal;
        private TextView mealImage;

        public basketViewHolder(@NonNull View itemView) { //gets data from the menu item (meal_item)
            super(itemView);
            mealQuantity = itemView.findViewById(R.id.basket_meal_quantity_r);
            mealName = itemView.findViewById(R.id.basket_meal_name_r);
            mealSubtotal = itemView.findViewById(R.id.basket_meal_subtotal_r);
            mealImage = itemView.findViewById(R.id.basket_url);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Basket basketRecycler);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
