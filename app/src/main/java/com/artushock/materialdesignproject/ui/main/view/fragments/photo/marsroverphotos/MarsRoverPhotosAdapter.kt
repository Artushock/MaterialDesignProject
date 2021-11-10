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

class MarsRoverPhotosAdapter(
    private val listener: OnListItemClickListener,
    private val data: List<MarsRoverPhoto>,
) :
    RecyclerView.Adapter<MarsRoverPhotosBaseViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarsRoverPhotosBaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_WITH_BUTTONS -> {
                MarsRoverPhotosViewHolder(inflater.inflate(R.layout.mars_rover_photos_item,
                    parent,
                    false) as View)
            }
            TYPE_WITHOUT_BUTTONS -> {
                MarsRoverPhotosViewHolderWithoutButtons(inflater.inflate(R.layout.mars_rover_photos_item_without_buttons,
                    parent,
                    false) as View)
            }
            else -> {
                MarsRoverPhotosViewHolderHeader(inflater.inflate(R.layout.mars_rover_photos_item_header,
                    parent,
                    false) as View)
            }
        }
    }

    override fun onBindViewHolder(holder: MarsRoverPhotosBaseViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> TYPE_HEADER
            data[position].cameraName == "FHAZ" -> TYPE_WITH_BUTTONS
            else -> TYPE_WITHOUT_BUTTONS
        }
    }

    inner class MarsRoverPhotosViewHolder(view: View) : MarsRoverPhotosBaseViewHolder(view) {
        override fun bind(data: MarsRoverPhoto) {
            val imageView: ImageView = itemView.findViewById(R.id.mars_rover_photos_image_view)
            Picasso.get()
                .load(data.url)
                .into(imageView)
            imageView.setOnClickListener {
                listener.onItemClick(data)
            }

            val textView: TextView = itemView.findViewById(R.id.mars_rover_photos_text_view)
            textView.text =
                with(data) { "$roverName: $date, ID: $id\n $cameraName: $cameraFullName" }
        }
    }

    inner class MarsRoverPhotosViewHolderWithoutButtons(view: View) :
        MarsRoverPhotosBaseViewHolder(view) {
        override fun bind(data: MarsRoverPhoto) {
            val imageView: ImageView =
                itemView.findViewById(R.id.mars_rover_photos_without_buttons_image_view)
            Picasso.get()
                .load(data.url)
                .into(imageView)

            imageView.setOnClickListener {
                listener.onItemClick(data)
            }

            val textView: TextView =
                itemView.findViewById(R.id.mars_rover_photos_without_buttons_text_view)
            textView.text =
                with(data) { "$roverName: $date, ID: $id\n $cameraName: $cameraFullName" }
        }
    }

    inner class MarsRoverPhotosViewHolderHeader(view: View) :
        MarsRoverPhotosBaseViewHolder(view) {
        override fun bind(data: MarsRoverPhoto) {
            val textView: TextView =
                itemView.findViewById(R.id.mars_rover_photos_item_header_text)
            textView.text = data.url
        }
    }

    interface OnListItemClickListener {
        fun onItemClick(data: MarsRoverPhoto)
    }

    companion object {
        private const val TYPE_WITH_BUTTONS = 0
        private const val TYPE_WITHOUT_BUTTONS = 1
        private const val TYPE_HEADER = 2
    }
}