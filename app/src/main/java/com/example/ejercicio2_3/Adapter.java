package com.example.ejercicio2_3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ejercicio2_3.Modelo.Photograh;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>{
    ArrayList<Photograh> photograhsList;

    public Adapter(ArrayList<Photograh> photograhsList){
        this.photograhsList = photograhsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType){
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.list_layout,
                parent,
                false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position){
        holder.setData(photograhsList.get(position));
    }

    @Override
    public int getItemCount(){
        return photograhsList.size();
    }

    private static Bitmap getPhotograhsImage(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName;
        ImageView imageViewIcon;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);
        }

        void setData(Photograh Fotografias){
            textViewName.setText(Fotografias.getDescripcion());
            imageViewIcon.setImageBitmap(getPhotograhsImage(Fotografias.getImagen()));
        }
    }
}

