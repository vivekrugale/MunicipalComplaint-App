package com.example.complaintapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

class myadapterForComplaints extends FirebaseRecyclerAdapter<modelComplaints,myadapterForComplaints.myviewholder> {

    private RecyclerViewClickListener listener;
    public myadapterForComplaints(@NonNull FirebaseRecyclerOptions<modelComplaints> options, RecyclerViewClickListener listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, int position, @NonNull final modelComplaints modelComplaints) {
        holder.citizen.setText(modelComplaints.getUser());
        holder.complaint.setText(modelComplaints.getComplaint());
        holder.area.setText(modelComplaints.getArea());
        holder.field.setText(modelComplaints.getField());
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_for_complaints,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView citizen,complaint,area,field;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            citizen = (TextView) itemView.findViewById(R.id.citizenText);
            complaint = (TextView) itemView.findViewById(R.id.complaintNameText);
            area = (TextView) itemView.findViewById(R.id.areaNameText);
            field = (TextView) itemView.findViewById(R.id.textViewFieldName);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, citizen.getText().toString().trim());
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, String citizen);
    }
}

