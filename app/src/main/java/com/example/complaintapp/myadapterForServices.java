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

class myadapterForServices extends FirebaseRecyclerAdapter<modelServices,myadapterForServices.myviewholder> {

    private RecyclerViewClickListener listener;
    public myadapterForServices(@NonNull FirebaseRecyclerOptions<modelServices> options, RecyclerViewClickListener listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, int position, @NonNull final modelServices modelServices) {
        holder.nameService.setText(modelServices.getName());
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_for_services,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CircleImageView img;
        TextView nameService;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img = (CircleImageView) itemView.findViewById(R.id.img1);
            nameService = (TextView) itemView.findViewById(R.id.nameService);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, nameService.getText().toString().trim());
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, String nameService);
    }
}

