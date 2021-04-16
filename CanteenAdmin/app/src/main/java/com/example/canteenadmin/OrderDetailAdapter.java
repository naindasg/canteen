package com.example.canteenadmin;

import android.graphics.ImageFormat;
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

public class OrderDetailAdapter extends FirebaseRecyclerAdapter<OrderDetail, OrderDetailAdapter.orderDetailViewHolder> {

    private OnItemClickListener onItemClickListener;

    public OrderDetailAdapter(@NonNull FirebaseRecyclerOptions<OrderDetail> options) {
        super(options);
    }

    @Override
    protected void
    onBindViewHolder(@NonNull orderDetailViewHolder holder, int position, @NonNull OrderDetail model) { //Binds data to the views
        holder.foodName.setText(model.getName()); //puts meal name into the holder
        holder.foodQuantity.setText(model.getQuantity()); //puts meal description into the holder
        holder.customerName.setText(model.getCustomerName());
        Glide.with(holder.mealImage.getContext()).load(model.getUrl()).into(holder.mealImage);
    }

    @NonNull
    @Override
    public OrderDetail getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public orderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflating the menu item (list_item_meal)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_item2, parent, false);
        return new orderDetailViewHolder(view);
    }


    class orderDetailViewHolder extends RecyclerView.ViewHolder {
        private TextView foodName, foodQuantity, customerName;
        private ImageView mealImage;

        public orderDetailViewHolder(@NonNull View itemView) { //gets data from the menu item (list_item_meal)
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            foodQuantity = itemView.findViewById(R.id.foodQuantity);
            customerName = itemView.findViewById(R.id.customerName);
            mealImage = itemView.findViewById(R.id.mealImage);

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
        void onItemClick(OrderDetail orderDetail);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
