package com.example.loadkaro;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class accepted_request_user_recyclerview_adapter_class extends FirebaseRecyclerAdapter<accepted_request_user_model_class,accepted_request_user_recyclerview_adapter_class.myviewholder> {

    public accepted_request_user_recyclerview_adapter_class(@NonNull FirebaseRecyclerOptions<accepted_request_user_model_class> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull accepted_request_user_model_class model) {

        Picasso.get().load(model.getDriver_pic_url()).placeholder(R.drawable.user_icon).into(holder.profile_dp);

        holder.txt_request_no.setText(model.getRequest_no());
        holder.txt_username.setText(model.getDriver_name());
        holder.txt_address_from.setText(model.getAddress_from());
        holder.txt_address_to.setText(model.getAddress_to());
        holder.txt_material_name.setText(model.getMaterial_name());

        holder.btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(holder.txt_username.getContext())
                        .withPermission(Manifest.permission.CALL_PHONE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent icall=new Intent(Intent.ACTION_CALL);
                                icall.setData(Uri.parse("tel:"+model.getDriver_contact().trim()));
                                holder.txt_username.getContext().startActivity(icall);

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                Toast.makeText(holder.txt_username.getContext(), "call permission denied", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });




    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.accepted_request_user_row_design,parent,false);
        myviewholder viewholder=new myviewholder(view);
        return viewholder;
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        ShapeableImageView profile_dp,img_decline_request;
        TextView txt_username,txt_request_no,txt_address_from,txt_address_to,txt_material_name;
        MaterialButton btn_connect,btn_loading_otp;
        public myviewholder(@NonNull View itemView) {
            super(itemView);

            profile_dp=itemView.findViewById(R.id.accept_request_user_row_profile_dp);
            img_decline_request=itemView.findViewById(R.id.accept_request_user_row_decline_request);
            txt_request_no=itemView.findViewById(R.id.accept_request_user_row_txt_request_no);
            txt_username=itemView.findViewById(R.id.accept_request_user_row_username);
            txt_address_from=itemView.findViewById(R.id.accept_request_user_row_txt_address_from);
            txt_address_to=itemView.findViewById(R.id.accept_request_user_row_txt_address_to);
            txt_material_name=itemView.findViewById(R.id.accept_request_user_row_txt_material_name);
            btn_connect=itemView.findViewById(R.id.accept_request_user_row_btn_connect);
            btn_loading_otp=itemView.findViewById(R.id.accept_request_user_row_btn_otp);
        }

    }
}
