package com.example.loadkaro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class request_all_trip_driver_adapter extends FirebaseRecyclerAdapter<all_trip_request_model_class,request_all_trip_driver_adapter.myviewholder> {
Activity mActivity;
    public request_all_trip_driver_adapter(@NonNull FirebaseRecyclerOptions<all_trip_request_model_class> options, Activity mActivity) {
        super(options);
        this.mActivity=mActivity;
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull all_trip_request_model_class model) {

        Picasso.get().load(model.getUser_url_pic()).placeholder(R.drawable.user_icon).into(holder.user_profile_dp);
        holder.username.setText(model.getUser_name());
        holder.address_from.setText(model.getAddress_from());
        holder.address_to.setText(model.getAddress_to());
        holder.loading_date.setText(model.getDate_of_loading());
        holder.unloading_date.setText(model.getDate_of_unloading());
        holder.distance.setText(model.getDistance());
        holder.material_name.setText(model.getMaterial_name());
        holder.material_quantity.setText(model.getMaterial_quantity());
        holder.price.setText(model.getPrice());


        holder.btn_request_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //complete btn request trip operation.


                ProgressDialog dialog=new ProgressDialog(holder.address_from.getContext());
                dialog.setTitle("Booking Trip.");
                dialog.setMessage("Please wait....");
                dialog.show();



                String noti_title="Request Accepted";
                String noti_body="your trip from "+model.getAddress_from()+" to "+model.getAddress_to()+" is been accepted.";

                Toast.makeText(mActivity, ""+model.getUser_token_id(), Toast.LENGTH_SHORT).show();

                FcmNotificationsSender notificationsSender=new FcmNotificationsSender(model.getUser_token_id(),noti_title,noti_body,holder.address_from.getContext(),mActivity);
                notificationsSender.SendNotifications();


                FirebaseUser currentuser= FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Driver").child(currentuser.getUid());
                reference.child("Profile").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            String driver_name=snapshot.child("name").getValue().toString();
                            String driver_pic_url=snapshot.child("profile_dp_url").getValue().toString();
                            String driver_contact=snapshot.child("contact").getValue().toString();

                            accepted_request_user_model_class accepted_model=new accepted_request_user_model_class(driver_name,driver_pic_url, currentuser.getUid(),model.getRequest_no(),model.getAddress_from(),model.getAddress_to(),driver_contact,model.getMaterial_name());

                            DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("User").child(model.getUser_id());
                            reference1.child("Accepted Request").child(currentuser.getUid()+model.getRequest_no()).setValue(accepted_model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful())
                                    {
                                        String request_status="accept";
                                        all_trip_request_model_class booked_model=new all_trip_request_model_class(model.getUser_token_id(),model.getRequest_no(), model.getAddress_from(), model.getAddress_to(),model.getDate_of_loading(),model.getDate_of_unloading(),model.getDistance(),model.getMaterial_name(),model.getMaterial_quantity(),model.getPrice(),model.getUser_contact(),model.getUser_name(),model.getUser_url_pic(),model.getUser_id(),request_status);

                                        reference.child("Booked request").child(model.getUser_id()+model.getRequest_no()).setValue(booked_model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful())
                                                {
                                                    Toast.makeText(holder.address_from.getContext(), "Trip booked contact customer", Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    Toast.makeText(holder.address_from.getContext(), "unable to book trip", Toast.LENGTH_SHORT).show();

                                                }
                                                dialog.dismiss();

                                            }
                                        });
                                    }
                                    else {
                                        Toast.makeText(holder.address_from.getContext(), "unable to book trip", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }

                                }
                            });

                        }
                        else {
                            Toast.makeText(holder.address_from.getContext(), "unable to book trip", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        dialog.dismiss();
                    }
                });
            }
        });




    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.request_all_trip_driver_row_design,parent,false);
        myviewholder viewholder=new myviewholder(view);
        return viewholder;
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        ShapeableImageView user_profile_dp;
        TextView username,address_from,address_to,loading_date,unloading_date,distance,material_name,material_quantity,price;
        AppCompatButton btn_request_trip;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            user_profile_dp=itemView.findViewById(R.id.request_all_trip_driver_row_profile_dp);
            username=itemView.findViewById(R.id.request_all_trip_driver_row_username);
            address_from=itemView.findViewById(R.id.request_all_trip_driver_row_txt_address_from);
            address_to=itemView.findViewById(R.id.request_all_trip_driver_row_txt_address_to);
            loading_date=itemView.findViewById(R.id.request_all_trip_driver_row_txt_loading_date);
            unloading_date=itemView.findViewById(R.id.request_all_trip_driver_row_txt_unloading_date);
            distance=itemView.findViewById(R.id.request_all_trip_driver_row_txt_distance);
            material_name=itemView.findViewById(R.id.request_all_trip_driver_row_txt_material_name);
            material_quantity=itemView.findViewById(R.id.request_all_trip_driver_row_txt_material_quantity);
            price=itemView.findViewById(R.id.request_all_trip_driver_row_txt_price);
            btn_request_trip=itemView.findViewById(R.id.request_all_trip_driver_row_btn_request_trip);
        }
    }
}
