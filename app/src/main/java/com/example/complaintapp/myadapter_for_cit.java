package com.example.complaintapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class myadapter_for_cit extends FirebaseRecyclerAdapter<model_cit_recview,myadapter_for_cit.myviewholder> {
    Context context;

    public myadapter_for_cit(@NonNull FirebaseRecyclerOptions<model_cit_recview> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder myviewholder, final int position, @NonNull final model_cit_recview model_cit_recview) {
        myviewholder.complaintname.setText(model_cit_recview.getComplaint());
        myviewholder.area.setText(model_cit_recview.getArea());

        context = myviewholder.itemView.getContext();
        SharedPreferences prefs = context.getSharedPreferences("Snapshot", MODE_PRIVATE);
        final String docName = prefs.getString("docNode", "");
        final String patName = prefs.getString("patNode", "");

        SharedPreferences sp = context.getSharedPreferences("CITDATA", MODE_PRIVATE);
        final String authName = sp.getString("authNode", "");
        final String ServiceName = sp.getString("ServiceName", "");
        final String citNode = sp.getString("citNode", "");
        final String area = sp.getString("area", "");

        final String ComplaintNode = ServiceName + area;

        myviewholder.editcom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(myviewholder.complaintname.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialogcontent_for_cit))
                        .setExpanded(true, 1100)
                        .create();

                View myview = dialogPlus.getHolderView();
                final EditText complaint = myview.findViewById(R.id.editcomplaintcit);
                final EditText area = myview.findViewById(R.id.editareacit);
                Button submit = myview.findViewById(R.id.btnUpdateComplaint);

                complaint.setText(model_cit_recview.getComplaint());
                area.setText(model_cit_recview.getArea());

                dialogPlus.show();
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("complaint", complaint.getText().toString());
                        map.put("area", area.getText().toString());
                        map.put("user", citNode);
                        map.put("field", ServiceName);

                        FirebaseDatabase.getInstance().getReference().child("Authority").child(authName).child("Complaints")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialogPlus.dismiss();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialogPlus.dismiss();

                                    }
                                });

                        FirebaseDatabase.getInstance().getReference().child("Authority").child(authName).child("Citizens").child(citNode).child("Complaints").child(ServiceName)
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialogPlus.dismiss();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialogPlus.dismiss();

                                    }
                                });
                    }
                });


            }
        });

        myviewholder.deletcom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(myviewholder.complaintname.getContext());
                builder.setTitle("Delete this complaint?");
                builder.setMessage("");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("Authority").child(authName).child("Complaints")
                                .child(getRef(position).getKey()).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("Authority").child(authName).child("Citizens").child(citNode).child("Complaints").child(ServiceName)
                                .removeValue();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_for_cit, parent, false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder {
        ImageView editcom, deletcom;
        TextView complaintname, area;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            complaintname = (TextView) itemView.findViewById(R.id.complaintname);
            area = (TextView) itemView.findViewById(R.id.area);
            editcom = (ImageView) itemView.findViewById(R.id.editComplaint);
            deletcom = (ImageView) itemView.findViewById(R.id.deleteComplaint);
        }

    }
}
