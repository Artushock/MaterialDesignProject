package com.artushock.materialdesignproject.ui.main.view.fragments.photo.marsroverphotos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.artushock.materialdesignproject.R
import com.artushock.materialdesignproject.data.model.MarsRoverPhoto
import com.squareup.picasso.Picasso

class MarsRoverPhotosAdapter(private val data: List<MarsRoverPhoto>) :
    RecyclerView.Adapter<MarsRoverPhotosAdapter.MarsRoverPhotosViewHolder>() {


    class MarsRoverPhotosViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImageView: ImageView = view.findViewById(R.id.mars_rover_photos_image_view)
        val itemTextView: TextView = view.findViewById(R.id.mars_rover_photos_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarsRoverPhotosViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.mars_rover_photos_item, parent, false)

        return MarsRoverPhotosViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarsRoverPhotosViewHolder, position: Int) {
        //holder.itemImageView.load(data[position].url)

        Picasso.get()
            .load(data[position].url)
            .into(holder.itemImageView)
        holder.itemTextView.text = with(data[position]){
            "$roverName: $date, ID: $id\n $cameraName: $cameraFullName"
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}