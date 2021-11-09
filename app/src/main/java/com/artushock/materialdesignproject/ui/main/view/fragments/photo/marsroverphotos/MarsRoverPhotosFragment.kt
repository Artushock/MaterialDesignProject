package com.artushock.materialdesignproject.ui.main.view.fragments.photo.marsroverphotos

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.artushock.materialdesignproject.R
import com.artushock.materialdesignproject.data.model.MarsRoverPhoto
import com.artushock.materialdesignproject.data.model.MarsRoverPhotosData
import com.artushock.materialdesignproject.databinding.FragmentMarsRoverPhotosBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MarsRoverPhotosFragment : Fragment() {

    private var _binding: FragmentMarsRoverPhotosBinding? = null
    private val binding get() = _binding!!

    private var date: String = "2019-04-12"

    private val viewModel: MarsRoverPhotosViewModel by lazy {
        ViewModelProvider(this).get(MarsRoverPhotosViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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
        binding.marsRoverToolbarTitle.text = "Curisoity: $date. $photosAmount photo(s)"
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
                binding.marsRoverProgressBar.visibility = View.VISIBLE
                binding.marsRoverChipsLayout.visibility = View.GONE
                binding.marsRoverRecyclerView.visibility = View.GONE
                binding.marsRoverAppbar.visibility = View.GONE
                binding.marsRoverTextView.visibility = View.GONE
            }
            is MarsRoverPhotosData.Success -> {
                binding.marsRoverProgressBar.visibility = View.GONE
                binding.marsRoverChipsLayout.visibility = View.VISIBLE
                binding.marsRoverRecyclerView.visibility = View.VISIBLE
                binding.marsRoverAppbar.visibility = View.VISIBLE
                binding.marsRoverTextView.visibility = View.GONE

                val photos = data.marsRoverPhotos.photos
                if (photos.isNotEmpty()) {
                    val dataForAdapter = ArrayList<MarsRoverPhoto>()
                    for (i in photos) {
                        dataForAdapter.add(i.mapToMarsRoverPhoto())
                    }
                    val recyclerView = binding.marsRoverRecyclerView
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter = MarsRoverPhotosAdapter(dataForAdapter)
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

    private fun showMessage(message: String) {
        binding.marsRoverProgressBar.visibility = View.GONE
        binding.marsRoverChipsLayout.visibility = View.VISIBLE
        binding.marsRoverRecyclerView.visibility = View.GONE
        binding.marsRoverAppbar.visibility = View.VISIBLE

        val marsRoverTextView = binding.marsRoverTextView
        marsRoverTextView.visibility = View.VISIBLE
        marsRoverTextView.text = message
    }

}