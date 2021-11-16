package com.artushock.materialdesignproject.ui.main.view.fragments.photo.marsroverphotos

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.blue
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.artushock.materialdesignproject.R
import com.artushock.materialdesignproject.data.model.MarsRoverPhoto
import com.squareup.picasso.Picasso

class MarsRoverPhotosAdapter(
    private val data: MutableList<Pair<MarsRoverPhoto, Boolean>>,
    private val dragListener: OnStartDragListener,
) : RecyclerView.Adapter<MarsRoverPhotosBaseViewHolder>(), ItemTouchHelperAdapter {

    init {
        Log.d(TAG, "init: ${data.size}")
    }

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

    override fun onBindViewHolder(
        holder: MarsRoverPhotosBaseViewHolder,
        position: Int,
        payloads: MutableList<Any>,
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val combinedChange =
                createCombinedPayload(payloads as List<Change<Pair<MarsRoverPhoto, Boolean>>>)
            val oldData = combinedChange.oldData
            val newData = combinedChange.newData

            if (newData.first.date != oldData.first.date) {
                holder.itemView.findViewById<TextView>(R.id.mars_rover_photos_text_view)
                    .text =
                    with(newData.first) { "$roverName: $date, ID: $id\n $cameraName: $cameraFullName" }
            }

            if (newData.first.url != oldData.first.url) {
                val imageView =
                    holder.itemView.findViewById<ImageView>(R.id.mars_rover_photos_image_view)
                Picasso.get()
                    .load(newData.first.url)
                    .into(imageView)
            }

            if (newData.second != oldData.second) {
                val buttonLine =
                    holder.itemView.findViewById<LinearLayout>(R.id.mars_item_image_button_line)
                buttonLine.visibility = if (newData.second) View.VISIBLE else View.GONE
            }
        }
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

    inner class MarsRoverPhotosViewHolder(view: View) : MarsRoverPhotosBaseViewHolder(view),
        ItemTouchHelperViewHolder {

        private var isItemFavorite = false

        @SuppressLint("ClickableViewAccessibility")
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
            hamburgerImageButton.performClick()

            hamburgerImageButton.setOnTouchListener { _, motionEvent ->
                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        dragListener.onStartDrag(this@MarsRoverPhotosViewHolder)
                        true
                    }
                    else -> false
                }
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(Color.WHITE)
        }

        private fun toggleButtonLine() {
            data[layoutPosition] = data[layoutPosition].let {
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
            Log.d(TAG,
                "deleteItem() data.size: ${data.size} data.hashCode(): ${data.hashCode()}").blue
            data.removeAt(layoutPosition)
            Log.d(TAG,
                "MarsRoverPhotosAdapter.deleteItem() : $layoutPosition data.size: ${data.size}")
            notifyItemRemoved(layoutPosition)
            Log.d(TAG,
                "MarsRoverPhotosAdapter.deleteItem() : notifyItemRemoved($layoutPosition) data.size: ${data.size}")
        }
    }

    inner class MarsRoverPhotosViewHolderHeader(view: View) :
        MarsRoverPhotosBaseViewHolder(view), ItemTouchHelperViewHolder {
        override fun bind(data: Pair<MarsRoverPhoto, Boolean>) {
            val textView: TextView =
                itemView.findViewById(R.id.mars_rover_photos_item_header_text)

            textView.text = data.first.url
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(0)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        data.removeAt(fromPosition).apply {
            data.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        Log.d(TAG,
            "onItemDismiss(position = $position) data.size: ${data.size}  data.hashCode(): ${data.hashCode()}")
        data.removeAt(position)
        Log.d(TAG,
            "MarsRoverPhotosAdapter.onItemDismiss(position = $position) data.size: ${data.size}")
        notifyItemRemoved(position)
        Log.d(TAG,
            "MarsRoverPhotosAdapter.onItemDismiss(position = $position) : notifyItemRemoved($position) data.size: ${data.size}")
    }

    fun setItems(newItems: List<Pair<MarsRoverPhoto, Boolean>>){
        val result = DiffUtil.calculateDiff(DiffUtilsCallBack(data, newItems))
        result.dispatchUpdatesTo(this)
        data.clear()
        data.addAll(newItems)
    }

    inner class DiffUtilsCallBack(
        private val oldItems: List<Pair<MarsRoverPhoto, Boolean>>,
        private val newItems: List<Pair<MarsRoverPhoto, Boolean>>,
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].first.id == newItems[oldItemPosition].first.id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition].first
            val newItem = newItems[newItemPosition].first

            return oldItem.url == newItem.url && oldItem.date == newItem.date
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return Change(oldItem, newItem)
        }
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_PHOTO_CONTAINER = 1

        private const val TAG = "123123123345-"
    }

    interface OnStartDragListener {
        fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
    }
}

