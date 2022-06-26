package com.example.project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    ArrayList<Info> infoArrayList=new ArrayList<>();
    LayoutInflater layoutInflater;
    Context context;
    Database my_db=new Database(context);
    String address_type_temp,id,name_1,address_1,address_type_1,latitude_1,longitude_1;

    public CustomAdapter(ArrayList<Info> infoArrayList, Context context) {
        this.infoArrayList = infoArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater=LayoutInflater.from(context);
        View v=layoutInflater.inflate(R.layout.row_list,parent,false);
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Info info=infoArrayList.get(position);
        holder.textView4.setText(infoArrayList.get(position).getName()+"-"+infoArrayList.get(position).getAddress());
        holder.bind(info);
        final Database my_db=new Database(context);
        final Cursor c=my_db.getAllData();


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean expanded=info.isExpanded();
                info.setExpanded(!expanded);
                notifyItemChanged(position);
            }
        });
        holder.b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder=new AlertDialog.Builder(context);
                LayoutInflater layoutInflater1=LayoutInflater.from(context);
                View mView=layoutInflater1.inflate(R.layout.dialog_2,null);
                final EditText edit_name=mView.findViewById(R.id.edit_name);
                final EditText edit_address=mView.findViewById(R.id.edit_address);
                final Button edit_save=mView.findViewById(R.id.edit_save);
                Button edit_cancel=mView.findViewById(R.id.edit_cancel);
                Spinner edit_spinner=mView.findViewById(R.id.edit_spinner);
                edit_name.setText(infoArrayList.get(position).getName());
                edit_address.setText(infoArrayList.get(position).getAddress());
                ArrayAdapter adapter = (ArrayAdapter) ArrayAdapter.createFromResource(context, R.array.Types, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                edit_spinner.setAdapter(adapter);
                id=infoArrayList.get(position).getId();
                mBuilder.setView(mView);
                final AlertDialog dialog=mBuilder.create();
                dialog.show();
                edit_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String[] address_types =context.getResources().getStringArray(R.array.Types);
                        address_type_temp=address_types[i];

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                edit_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isupdated=my_db.update_data(id,edit_name.getText().toString(), edit_address.getText().toString(), address_type_temp);
                        infoArrayList.clear();
                        if (c.getCount() > 0) {
                            if (c.moveToFirst()) {
                                do {
                                    id = c.getString(0);
                                    name_1 = c.getString(1);
                                    address_1 = c.getString(2);
                                    address_type_1 = c.getString(3);
                                    latitude_1=c.getString(4);
                                    longitude_1=c.getString(5);
                                    Info info = new Info(id, name_1, address_1, address_type_1,latitude_1,longitude_1);
                                    infoArrayList.add(info);

                                } while (c.moveToNext());
                            }
                        } else {

                        }
                        swap(infoArrayList);
                        if (isupdated) {
                            Toast.makeText(context, "Data Updated", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(context, "Data Not Updated", Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                });
                edit_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });
        holder.b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double d=Double.parseDouble(infoArrayList.get(position).getLatitude());
                double a=Double.parseDouble(infoArrayList.get(position).getLongitude());
                MainActivity.drawer.closeDrawers();
                LatLng pp=new LatLng(d,a);
                MarkerOptions markerOptions=new MarkerOptions();
                markerOptions.position(pp);
                MainActivity.mMap.clear();

                MainActivity.mMap.addMarker(markerOptions);
                MainActivity.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pp,6));
            }
        });
        holder.constraintLayout.setTag(holder);

    }

    @Override
    public int getItemCount() {
        return infoArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView,textView2,textView3,textView4;
        ConstraintLayout constraintLayout;
        View subitem;
        Button b1,b2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView4=itemView.findViewById(R.id.textView4);
            textView4.setTypeface(null, Typeface.BOLD);
            constraintLayout=itemView.findViewById(R.id.constraintLayout);
            subitem=itemView.findViewById(R.id.cnstrlyt);
            b1=itemView.findViewById(R.id.button5);
            b2=itemView.findViewById(R.id.button6);
        }
        private void bind(Info info){
            boolean expanded=info.isExpanded();
            subitem.setVisibility(expanded ? View.VISIBLE : View.GONE);

        }
    }
    public void swap(ArrayList<Info> info){

        infoArrayList = info;
        notifyDataSetChanged();

    }
    public void swap_item(){

    }
}