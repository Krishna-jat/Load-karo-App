package com.example.loadkaro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class vehicle_Firebase_Recyclerview_adapter_class extends FirebaseRecyclerAdapter<driver_vehicle_model_class,vehicle_Firebase_Recyclerview_adapter_class.myviewholder> {

    public vehicle_Firebase_Recyclerview_adapter_class(@NonNull FirebaseRecyclerOptions<driver_vehicle_model_class> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull driver_vehicle_model_class model) {

        holder.vehicle_type.setText(model.getVehicle_type());
        holder.vehicle_capacity.setText(model.getVehicle_capacity());
        holder.vehicle_number.setText(model.getVehicle_number());

        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(holder.vehicle_capacity.getContext())
                        .setTitle("Delete Vehicle!")
                        .setMessage("Are you sure to delete vehicle details?")
                        .setIcon(R.drawable.delete_icon)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase.getInstance().getReference("Driver").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Vehicles")
                                        .child(getRef(holder.getAbsoluteAdapterPosition()).getKey()).removeValue();
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                    dialog.show();

                return true;
            }
        });




    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_recyclerview_vehicles_row,parent,false);
        myviewholder myviewholder=new myviewholder(view);
        return myviewholder;
    }

    public class myviewholder extends RecyclerView.ViewHolder {

TextView vehicle_type,vehicle_capacity,vehicle_number;
LinearLayout linearLayout;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            vehicle_type=itemView.findViewById(R.id.recyclerview_txt_row_vehicle_type);
            vehicle_capacity=itemView.findViewById(R.id.recyclerview_txt_row_vehicle_capacity);
            vehicle_number=itemView.findViewById(R.id.recyclerview_txt_row_vehicle_number);
            linearLayout=itemView.findViewById(R.id.recyclerview_linear_layout);
        }
    }
}
