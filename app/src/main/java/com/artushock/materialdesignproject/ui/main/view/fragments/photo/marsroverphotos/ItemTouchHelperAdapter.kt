package com.artushock.materialdesignproject.ui.main.view.fragments.photo.marsroverphotos

interface ItemTouchHelperAdapter {

    fun onItemMove(fromPosition: Int, toPosition: Int)

    fun onItemDismiss(position: Int)
}