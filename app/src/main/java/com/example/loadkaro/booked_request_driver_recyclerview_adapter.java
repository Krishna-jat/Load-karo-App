package com.example.loadkaro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class booked_request_driver_recyclerview_adapter extends FirebaseRecyclerAdapter<all_trip_request_model_class,booked_request_driver_recyclerview_adapter.myviewholder> {

    public booked_request_driver_recyclerview_adapter(@NonNull FirebaseRecyclerOptions<all_trip_request_model_class> options) {
        super(options);
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



        //btn for calling user
        holder.btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(holder.address_from.getContext())
                        .withPermission(Manifest.permission.CALL_PHONE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent icall=new Intent(Intent.ACTION_CALL);
                                icall.setData(Uri.parse("tel:"+model.getUser_contact().trim()));
                               holder.address_from.getContext().startActivity(icall);

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                Toast.makeText(holder.address_from.getContext(), "call permission denied", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        //btn to make trip live trip
        holder.btn_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser current_user= FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Driver").child(current_user.getUid());
                HashMap<String,Object> map=new HashMap<>();
                map.put("address_from",model.getAddress_from());
                map.put("address_to",model.getAddress_to());
                map.put("date_of_unloading",model.getDate_of_unloading());
                reference.child("Live Trip").setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                   if(task.isSuccessful())
                   {
                       Toast.makeText(holder.address_from.getContext(), "Trip is made live", Toast.LENGTH_SHORT).show();
                   }
                   else
                   {
                       Toast.makeText(holder.address_from.getContext(), "Unable to make trip live", Toast.LENGTH_SHORT).show();

                   }
                    }
                });
            }
        });

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.booked_request_driver_row_design,parent,false);
        myviewholder viewholder=new myviewholder(view);
        return viewholder;
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        ShapeableImageView user_profile_dp;
        TextView username,address_from,address_to,loading_date,unloading_date,distance,material_name,material_quantity,price;

        MaterialButton btn_connect,btn_live;
        public myviewholder(@NonNull View itemView) {
            super(itemView);

            user_profile_dp=itemView.findViewById(R.id.booked_request_driver_row_profile_dp);
            username=itemView.findViewById(R.id.booked_request_driver_row_username);
            address_from=itemView.findViewById(R.id.booked_request_driver_row_txt_address_from);
            address_to=itemView.findViewById(R.id.booked_request_driver_row_txt_address_to);
            loading_date=itemView.findViewById(R.id.booked_request_driver_row_txt_loading_date);
            unloading_date=itemView.findViewById(R.id.booked_request_driver_row_txt_unloading_date);
            distance=itemView.findViewById(R.id.booked_request_driver_row_txt_distance);
            material_name=itemView.findViewById(R.id.booked_request_driver_row_txt_material_name);
            material_quantity=itemView.findViewById(R.id.booked_request_driver_row_txt_material_quantity);
            price=itemView.findViewById(R.id.booked_request_driver_row_txt_price);
            btn_connect=itemView.findViewById(R.id.booked_request_btn_connect);
            btn_live=itemView.findViewById(R.id.booked_request_btn_live);
        }
    }
}
