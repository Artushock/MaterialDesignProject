package com.artushock.materialdesignproject.ui.main.view.fragments.photo.marsroverphotos

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artushock.materialdesignproject.R
import com.artushock.materialdesignproject.data.model.MarsRoverPhoto
import com.artushock.materialdesignproject.data.model.MarsRoverPhotosDTO
import com.artushock.materialdesignproject.data.model.MarsRoverPhotosData
import com.artushock.materialdesignproject.databinding.FragmentMarsRoverPhotosBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MarsRoverPhotosFragment : Fragment() {

    private var _binding: FragmentMarsRoverPhotosBinding? = null
    private val binding get() = _binding!!

    private var date: String = "2019-04-12"

    private val adapter by lazy {
        MarsRoverPhotosAdapter(
            mutableListOf(),
            object : MarsRoverPhotosAdapter.OnStartDragListener {
                override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                    itemTouchHelper.startDrag(viewHolder)
                }
            }
        )
    }

    private val viewModel: MarsRoverPhotosViewModel by lazy {
        ViewModelProvider(this)[MarsRoverPhotosViewModel::class.java]
    }

    private val recyclerView by lazy {
        binding.marsRoverRecyclerView
    }

    lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMarsRoverPhotosBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        date = LocalDate.now().format(DateTimeFormatter.ISO_DATE)

        initToolbar()

        initChips()

        initViewModel(date)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun initToolbar() {
        val toolbar = binding.marsRoverToolbar
        toolbar.inflateMenu(R.menu.mars_rover_photos_menu)
        toolbar.setOnMenuItemClickListener { itemMenu ->
            when (itemMenu.itemId) {
                R.id.mars_rover_menu_rover_curiosity -> {
                    Toast.makeText(requireContext(), "Curiosity", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.mars_rover_menu_rover_opportunity -> {
                    Toast.makeText(requireContext(), "Opportunity", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.mars_rover_menu_rover_spirit -> {
                    Toast.makeText(requireContext(), "Spirit", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun setToolbarTitle(photosAmount: Int) {
        "Curiosity: $date. $photosAmount photo(s)".also { binding.marsRoverToolbarTitle.text = it }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initChips() {
        binding.marsRoverPreviousDayChip.setOnClickListener {
            date = LocalDate.parse(date).minusDays(1).format(DateTimeFormatter.ISO_DATE)
            initViewModel(date)
        }

        binding.marsRoverNextDayChip.setOnClickListener {
            date = LocalDate.parse(date).plusDays(1).format(DateTimeFormatter.ISO_DATE)
            initViewModel(date)
        }
    }

    private fun initViewModel(date: String) {
        viewModel.getCuriosityPhotosByDate(date)
            .observe(viewLifecycleOwner, { renderData(it) })
    }

    private fun renderData(data: MarsRoverPhotosData) {
        when (data) {
            is MarsRoverPhotosData.Loading -> {
                setLayoutElementsVisibility(
                    progressBarVisibility = true,
                    chipsVisibility = false,
                    recyclerViewVisibility = false,
                    appBarVisibility = false,
                    textViewVisibility = false
                )
            }
            is MarsRoverPhotosData.Success -> {
                setLayoutElementsVisibility(
                    progressBarVisibility = false,
                    chipsVisibility = true,
                    recyclerViewVisibility = true,
                    appBarVisibility = true,
                    textViewVisibility = false
                )

                val photos = data.marsRoverPhotos.photos
                if (photos.isNotEmpty()) {
                    val newData = mapDataForAdapter(photos)
                    adapter.setItems(newData)
                    setToolbarTitle(photos.size)
                } else {
                    showMessage("There aren't photos this day!")
                }
            }
            is MarsRoverPhotosData.Error -> {
                showMessage(data.error.message.toString())
            }
        }
    }

    private fun setLayoutElementsVisibility(
        progressBarVisibility: Boolean,
        chipsVisibility: Boolean,
        recyclerViewVisibility: Boolean,
        appBarVisibility: Boolean,
        textViewVisibility: Boolean,
    ) {
        with(binding) {
            marsRoverProgressBar.visibility = if (progressBarVisibility) View.VISIBLE else View.GONE
            marsRoverChipsLayout.visibility = if (chipsVisibility) View.VISIBLE else View.GONE
            marsRoverRecyclerView.visibility =
                if (recyclerViewVisibility) View.VISIBLE else View.GONE
            marsRoverAppbar.visibility = if (appBarVisibility) View.VISIBLE else View.GONE
            marsRoverTextView.visibility = if (textViewVisibility) View.VISIBLE else View.GONE
        }
    }

    private fun mapDataForAdapter(photos: MutableList<MarsRoverPhotosDTO.Photo>): List<Pair<MarsRoverPhoto, Boolean>> {
        val data = mutableListOf<Pair<MarsRoverPhoto, Boolean>>()
        for (i in photos) {
            data.add(Pair(i.mapToMarsRoverPhoto(), false))
        }

        val roverInfo = with(photos[0].rover) {
            "Rover: $name (id: $id)\nLaunch date: $launch_date\nLanding date: $landing_date\nStatus: $status"
        }
        data.add(0, Pair(MarsRoverPhoto(0, roverInfo), false))

        return data
    }

    private fun showMessage(message: String) {
        setLayoutElementsVisibility(
            progressBarVisibility = false,
            chipsVisibility = true,
            recyclerViewVisibility = false,
            appBarVisibility = false,
            textViewVisibility = true
        )
        val marsRoverTextView = binding.marsRoverTextView
        marsRoverTextView.visibility = View.VISIBLE
        marsRoverTextView.text = message
    }
}