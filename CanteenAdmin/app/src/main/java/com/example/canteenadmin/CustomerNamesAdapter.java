package com.example.canteenadmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class CustomerNamesAdapter extends FirebaseRecyclerAdapter<CustomerNames, CustomerNamesAdapter.orderViewHolder> {

    private OnItemClickListener onItemClickListener;

    public CustomerNamesAdapter(@NonNull FirebaseRecyclerOptions<CustomerNames> options) {
        super(options);
    }

    @Override
    protected void
    onBindViewHolder(@NonNull orderViewHolder holder, int position, @NonNull CustomerNames model) { //Binds data to the views
        holder.customerName.setText(model.getCustomerName()); //puts meal name into the holder

    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    @NonNull
    @Override
    public CustomerNames getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public orderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflating the menu item (list_item_meal)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_name_item, parent, false);
        return new orderViewHolder(view);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getRef().removeValue();

    }


    class orderViewHolder extends RecyclerView.ViewHolder {
        private TextView customerName;

        public orderViewHolder (@NonNull View itemView) { //gets data from the menu item (list_item_meal)
            super(itemView);
            customerName = itemView.findViewById(R.id.customerNameNew);

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
        void onItemClick(CustomerNames order);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;

    }





}
