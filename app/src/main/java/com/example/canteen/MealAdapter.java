package com.example.canteen;

import android.content.Context;
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

import java.util.ArrayList;


public class MealAdapter extends FirebaseRecyclerAdapter<Meal, MealAdapter.mealViewHolder> {

    private OnItemClickListener onItemClickListener;


    public MealAdapter(@NonNull FirebaseRecyclerOptions<Meal> options) {
        super(options);
    }

    @Override
    protected void
    onBindViewHolder(@NonNull mealViewHolder holder, int position, @NonNull Meal model) { //Binds data to the views
        holder.mealName.setText(model.getName()); //puts meal name into the holder
        holder.mealDescription.setText(model.getDescription()); //puts meal description into the holder
        holder.mealPrice.setText(model.getPrice()); //puts meal price into the holder
        holder.mealID.setText(model.getMealId()); //puts meal ID into the holder
        Glide.with(holder.mealImage.getContext()).load(model.getUrl()).into(holder.mealImage); //puts meal image into the holder
    }

    @NonNull
    @Override
    public Meal getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public mealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflating the menu item (list_item_meal)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_item, parent, false);
        return new mealViewHolder(view);
    }

    class mealViewHolder extends RecyclerView.ViewHolder {
        private TextView mealName, mealDescription, mealPrice, mealID;
        private ImageView mealImage;

        public mealViewHolder(@NonNull View itemView) { //gets data from the menu item (list_item_meal)
            super(itemView);
            mealImage = itemView.findViewById(R.id.mealImage);
            mealName = itemView.findViewById(R.id.mealName);
            mealDescription = itemView.findViewById(R.id.mealDescription);
            mealPrice = itemView.findViewById(R.id.mealPrice);
            mealID = itemView.findViewById(R.id.mealID);

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
        void onItemClick(Meal meal);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }





}
