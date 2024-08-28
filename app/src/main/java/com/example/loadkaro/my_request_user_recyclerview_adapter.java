package com.example.loadkaro;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class my_request_user_recyclerview_adapter extends FirebaseRecyclerAdapter<my_request_user_model_class,my_request_user_recyclerview_adapter.myviewholder> {

    public my_request_user_recyclerview_adapter(@NonNull FirebaseRecyclerOptions<my_request_user_model_class> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull my_request_user_model_class model) {

        holder.request_no.setText(model.getRequest_no());
        holder.address_from.setText(model.getAddress_from());
        holder.address_to.setText(model.getAddress_to());
        holder.distance.setText(model.getDistance());
        holder.material_name.setText(model.getMaterial_name());
        holder.material_quantity.setText(model.getMaterial_quantity());
        holder.loading_date.setText(model.getDate_of_loading());
        holder.unloading_date.setText(model.getDate_of_unloading());
        holder.price.setText(model.getPrice());



    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_request_user_row_design,parent,false);
        myviewholder viewholder=new myviewholder(view);
        return viewholder;
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        TextView request_no,address_from,address_to,loading_date,unloading_date,distance,material_name,material_quantity,price;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            request_no=itemView.findViewById(R.id.my_request_user_row_txt_request_no);
            address_to=itemView.findViewById(R.id.my_request_user_row_txt_address_to);
            address_from=itemView.findViewById(R.id.my_request_user_row_txt_address_from);
            loading_date=itemView.findViewById(R.id.my_request_user_row_txt_loading_date);
            unloading_date=itemView.findViewById(R.id.my_request_user_row_txt_unloading_date);
            distance=itemView.findViewById(R.id.my_request_user_row_txt_distance);
            material_name=itemView.findViewById(R.id.my_request_user_row_txt_material_name);
            material_quantity=itemView.findViewById(R.id.my_request_user_row_txt_material_quantity);
            price=itemView.findViewById(R.id.my_request_user_row_txt_price);
        }
    }
}
