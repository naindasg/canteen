package com.example.canteen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class BasketAdapter extends FirebaseRecyclerAdapter<Basket, BasketAdapter.basketViewHolder> {

    private BasketAdapter.OnItemClickListener onItemClickListener;

    public BasketAdapter(@NonNull FirebaseRecyclerOptions<Basket> options) {
        super(options);
    }

////
    @Override
    protected void
    onBindViewHolder(@NonNull BasketAdapter.basketViewHolder holder, int position, @NonNull Basket model) { //Binds data to the views
        holder.mealName.setText(model.getName()); //puts meal name into the holder
        holder.mealQuantity.setText(model.getQuantity()); //puts meal description into the holder

       // holder.mealID.setText(model.getMealID()); //puts meal ID into the holder
       // Glide.with(holder.mealImage.getContext()).load(model.getUrl()).into(holder.mealImage); //puts meal image into the holder
        holder.mealImage.setText(model.getUrl());


        float priceFloat = Float.parseFloat(model.getPrice());
        int quantityInt = Integer.parseInt(model.getQuantity());
        float subtotalFloat = priceFloat * quantityInt;



        holder.mealSubtotal.setText(String.format("%.2f", subtotalFloat)); //puts meal price into the holder


    }

    @NonNull
    @Override
    public Basket getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public BasketAdapter.basketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflating the menu item (list_item_meal)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_item_recycler, parent, false);
        return new basketViewHolder(view);
    }

    class basketViewHolder extends RecyclerView.ViewHolder {
        private TextView mealName, mealDescription, mealPrice, mealID, mealIngredients, mealQuantity, mealSubtotal;
        private TextView mealImage;

        public basketViewHolder(@NonNull View itemView) { //gets data from the menu item (list_item_meal)
            super(itemView);
            mealQuantity = itemView.findViewById(R.id.basket_meal_quantity_r);
            mealName = itemView.findViewById(R.id.basket_meal_name_r);
            mealSubtotal = itemView.findViewById(R.id.basket_meal_subtotal_r);
//            mealDescription = itemView.findViewById(R.id.basket_meal_description_r);
            mealImage = itemView.findViewById(R.id.basket_url);
//            mealID = itemView.findViewById(R.id.basket_meal_Id_r);

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

    public void setOnItemClickListener(BasketAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
