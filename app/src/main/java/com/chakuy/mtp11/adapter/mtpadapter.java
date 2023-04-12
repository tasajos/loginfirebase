package com.chakuy.mtp11.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chakuy.mtp11.R;
import com.chakuy.mtp11.models.mtpcontroller;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class mtpadapter extends FirestoreRecyclerAdapter<mtpcontroller,mtpadapter.ViewHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public mtpadapter(@NonNull FirestoreRecyclerOptions<mtpcontroller> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int position, @NonNull mtpcontroller mtpcontroller) {
        viewHolder.name.setText(mtpcontroller.getNombre());
        viewHolder.lastname.setText(mtpcontroller.getApellido());
        viewHolder.type.setText(mtpcontroller.getTipo());
        viewHolder.detail.setText(mtpcontroller.getDetalle());
        viewHolder.order.setText(mtpcontroller.getPedido());
        viewHolder.quantity.setText(mtpcontroller.getCantidad());
        viewHolder.tecolor.setText(mtpcontroller.getColor());
        viewHolder.teprecio.setText(mtpcontroller.getPrecio());
        viewHolder.ubication.setText(mtpcontroller.getUbicacion());
        viewHolder.tefecha.setText(mtpcontroller.getfecha());



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewmtp_single, parent,false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, lastname, type, detail, order, quantity, tecolor, teprecio, ubication, tefecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nombre);
            lastname = itemView.findViewById(R.id.apellido);
            type = itemView.findViewById(R.id.tipo);
            detail = itemView.findViewById(R.id.detalle);
            order = itemView.findViewById(R.id.pedido);
            quantity = itemView.findViewById(R.id.cantidad);
            tecolor = itemView.findViewById(R.id.color);
            teprecio = itemView.findViewById(R.id.precio);
            ubication = itemView.findViewById(R.id.ubicacion);
            tefecha = itemView.findViewById(R.id.fecha);


        }
    }
}
