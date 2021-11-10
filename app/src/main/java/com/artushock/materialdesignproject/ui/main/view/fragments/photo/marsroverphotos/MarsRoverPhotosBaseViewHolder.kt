package com.artushock.materialdesignproject.ui.main.view.fragments.photo.marsroverphotos

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.artushock.materialdesignproject.data.model.MarsRoverPhoto

abstract class MarsRoverPhotosBaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(data: MarsRoverPhoto)
} 