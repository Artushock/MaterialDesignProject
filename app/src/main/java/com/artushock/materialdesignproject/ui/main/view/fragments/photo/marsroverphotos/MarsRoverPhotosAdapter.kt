package com.artushock.materialdesignproject.ui.main.view.fragments.photo.marsroverphotos

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.artushock.materialdesignproject.R
import com.artushock.materialdesignproject.data.model.MarsRoverPhoto
import com.squareup.picasso.Picasso

class MarsRoverPhotosAdapter(
    private val listener: OnListItemClickListener,
    private val data: MutableList<Pair<MarsRoverPhoto, Boolean>>,
) :
    RecyclerView.Adapter<MarsRoverPhotosBaseViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MarsRoverPhotosBaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_PHOTO_CONTAINER -> {
                MarsRoverPhotosViewHolder(inflater.inflate(R.layout.mars_rover_photos_item,
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
        return when (position) {
            0 -> TYPE_HEADER
            else -> TYPE_PHOTO_CONTAINER
        }
    }

    inner class MarsRoverPhotosViewHolder(view: View) : MarsRoverPhotosBaseViewHolder(view) {

        private var isItemFavorite = false

        override fun bind(data: Pair<MarsRoverPhoto, Boolean>) {
            val imageView: ImageView = itemView.findViewById(R.id.mars_rover_photos_image_view)
            Picasso.get()
                .load(data.first.url)
                .into(imageView)

            val buttonLine: LinearLayout = itemView.findViewById(R.id.mars_item_image_button_line)
            buttonLine.visibility = if (data.second) View.VISIBLE else View.GONE

            imageView.setOnClickListener {
                toggleButtonLine()
            }

            val textView: TextView = itemView.findViewById(R.id.mars_rover_photos_text_view)
            textView.text =
                with(data.first) { "$roverName: $date, ID: $id\n $cameraName: $cameraFullName" }

            val dropUpImageButton: ImageButton =
                itemView.findViewById(R.id.mars_item_image_button_drop_up)
            dropUpImageButton.setOnClickListener { moveUp() }

            val dropDownImageButton: ImageButton =
                itemView.findViewById(R.id.mars_item_image_button_drop_down)
            dropDownImageButton.setOnClickListener { moveDown() }

            val deleteImageButton: ImageButton =
                itemView.findViewById(R.id.mars_item_image_button_delete)
            deleteImageButton.setOnClickListener { deleteItem() }

            val favoriteImageButton: ImageButton =
                itemView.findViewById(R.id.mars_item_image_button_favorite)
            favoriteImageButton.setOnClickListener { setItemFavorite(it as ImageButton) }

            val hamburgerImageButton: ImageButton =
                itemView.findViewById(R.id.mars_item_image_button_hamburger)

        }

        private fun toggleButtonLine() {
            data[layoutPosition] = data[layoutPosition].let { it: Pair<MarsRoverPhoto, Boolean> ->
                it.first to !it.second
            }
            notifyItemChanged(layoutPosition)
        }

        private fun moveUp() {
            layoutPosition.takeIf { it > 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition - 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition - 1)
            }
        }

        private fun moveDown() {
            layoutPosition.takeIf { it < data.size - 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition + 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition + 1)
            }
        }

        private fun setItemFavorite(it: ImageButton) {
            if (isItemFavorite) {
                isItemFavorite = !isItemFavorite
                it.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setInterpolator(BounceInterpolator())
                    .duration = 800
                it.colorFilter = null
            } else {
                isItemFavorite = !isItemFavorite
                it.animate()
                    .scaleX(1.5f)
                    .scaleY(1.5f)
                    .setInterpolator(BounceInterpolator())
                    .duration = 800
                it.setColorFilter(R.color.red, PorterDuff.Mode.MULTIPLY)
            }
        }

        private fun deleteItem() {
            data.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }
    }

    inner class MarsRoverPhotosViewHolderHeader(view: View) :
        MarsRoverPhotosBaseViewHolder(view) {
        override fun bind(data: Pair<MarsRoverPhoto, Boolean>) {
            val textView: TextView =
                itemView.findViewById(R.id.mars_rover_photos_item_header_text)
            textView.text = data.first.url
        }
    }

    interface OnListItemClickListener {
        fun onItemClick(data: MarsRoverPhoto)
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_PHOTO_CONTAINER = 1
    }
}