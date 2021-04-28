package com.example.canteen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;
import java.util.List;


public class MealAdapter extends FirebaseRecyclerAdapter<Meal, MealAdapter.mealViewHolder> implements Filterable {

    private static final int LAYOUT_ONE = 0;
    private static final int LAYOUT_TWO = 1;
    private OnItemClickListener onItemClickListener;

    List<String> mealListAll;

    public MealAdapter(@NonNull FirebaseRecyclerOptions<Meal> options) {
        super(options);
    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);

        if (position == 0) {
            return LAYOUT_ONE;
        } else {
            return LAYOUT_TWO;
        }
    }

    @Override
    protected void
    onBindViewHolder(@NonNull mealViewHolder holder, int position, @NonNull Meal model) { //Gets data from firebase
        holder.mealName.setText(model.getName());
        holder.mealDescription.setText(model.getDescription());
        holder.mealIngredients.setText(model.getIngredients());
        holder.mealPrice.setText(model.getPrice());
        holder.mealID.setText(model.getMealId());
        Glide.with(holder.mealImage.getContext()).load(model.getUrl()).into(holder.mealImage);
        holder.mealCategory.setText(model.getCategory());

    }

    @NonNull
    @Override
    public Meal getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public mealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//        if (viewType == LAYOUT_ONE) {
//            //Inflating the menu item (meal_item.xml)
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drink_item, parent, false);
//            return new mealViewHolder(view);
//        } else {

            //Inflating the menu item (meal_item.xml)
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_item, parent, false);
            return new mealViewHolder(view);
     //   }
    }

    @Override
    public Filter getFilter() {
        return null;
    }


    class mealViewHolder extends RecyclerView.ViewHolder {
        private TextView mealID, mealName, mealDescription, mealIngredients, mealPrice, mealCategory;
        private ImageView mealImage;

        public mealViewHolder(@NonNull View itemView) { //gets data from the menu item (list_item_meal)
            super(itemView);
            mealID = itemView.findViewById(R.id.mealID);
            mealName = itemView.findViewById(R.id.mealName);
            mealDescription = itemView.findViewById(R.id.mealDescription);
            mealIngredients = itemView.findViewById(R.id.mealIngredients);
            mealPrice = itemView.findViewById(R.id.mealPrice);
            mealImage = itemView.findViewById(R.id.mealImage);
            mealCategory = itemView.findViewById(R.id.mealCategory);

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
