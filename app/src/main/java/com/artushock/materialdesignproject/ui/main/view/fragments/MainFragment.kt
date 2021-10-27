package com.artushock.materialdesignproject.ui.main.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.artushock.materialdesignproject.R
import com.artushock.materialdesignproject.data.model.PictureOfTheDayData
import com.artushock.materialdesignproject.databinding.MainFragmentBinding
import com.artushock.materialdesignproject.ui.main.view.activities.MainActivity
import com.artushock.materialdesignproject.ui.main.view.fragments.photo.PhotoFragment
import com.artushock.materialdesignproject.ui.main.viewmodel.MainViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainFragment : Fragment() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        private var isMain = true

        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomSheetBehavior(binding.includedBottomSheet.bottomSheetContainer)

        initTextInputLayout()
        setBottomAppBar()
        initViewModel()
        initChips()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initChips() {
        with(binding) {
            chipToday.setOnClickListener {
                viewModel.getCurrentDayData().observe(viewLifecycleOwner, { renderData(it) })
            }
            chipYesterday.setOnClickListener {
                viewModel.getYesterdayData().observe(viewLifecycleOwner, { renderData(it) })
            }
            chipTheDayBeforeYesterday.setOnClickListener {
                viewModel.getDayBeforeYesterdayData()
                    .observe(viewLifecycleOwner, { renderData(it) })
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_app_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, PhotoFragment())
                    .addToBackStack(null)
                    .commit()
            }
            R.id.app_bar_settings -> {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, SettingsFragment())?.addToBackStack(null)?.commit()
            }
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomAppBar() {
        val context = activity as MainActivity
        context.setSupportActionBar(binding.bottomAppBar)
        setHasOptionsMenu(true)

        with(binding) {
            fab.setOnClickListener {
                if (isMain) {
                    isMain = false
                    bottomAppBar.navigationIcon = null
                    bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                    fab.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_baseline_arrow_back_24
                        )
                    )
                    bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
                } else {
                    isMain = true
                    bottomAppBar.navigationIcon =
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_outline_insert_emoticon_24
                        )
                    bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                    fab.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_baseline_arrow_forward_24
                        )
                    )
                    bottomAppBar.replaceMenu(R.menu.menu_bottom_app_bar)
                }
            }
        }
    }

    private fun initTextInputLayout() {
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        binding.includedBottomSheet.bottomSheetDescriptionHeader.text =
            getString(R.string._info_waited)

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        Toast.makeText(context, "STATE_COLLAPSED", Toast.LENGTH_SHORT).show()
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        Toast.makeText(context, "STATE_DRAGGING", Toast.LENGTH_SHORT).show()
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        Toast.makeText(context, "STATE_EXPANDED", Toast.LENGTH_SHORT).show()
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        Toast.makeText(context, "STATE_HALF_EXPANDED", Toast.LENGTH_SHORT).show()
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        Toast.makeText(context, "STATE_HIDDEN", Toast.LENGTH_SHORT).show()
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                        Toast.makeText(context, "STATE_SETTLING", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("123123123", "slideOffset $slideOffset")
            }
        })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initViewModel() {
        viewModel.getCurrentDayData().observe(viewLifecycleOwner, { renderData(it) })
    }

    private fun renderData(data: PictureOfTheDayData?) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                binding.imageView.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE

                val serverResponseData = data.serverResponseData
                setPhotoDescriptionIntoBottomSheet(
                    serverResponseData.title,
                    serverResponseData.explanation
                )

                val url = serverResponseData.url
                if (serverResponseData.media_type == "video") {
                    handleVideoUrl(url)
                } else {
                    handleImageUrl(url)
                }

            }
            is PictureOfTheDayData.Loading -> {
                binding.imageView.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            }
            is PictureOfTheDayData.Error -> {
                throw data.error
            }
        }
    }

    private fun handleImageUrl(url: String?) {
        binding.videoGottenLayout.visibility = View.GONE
        binding.imageView.visibility = View.VISIBLE
        if (url.isNullOrEmpty()) {
            Toast.makeText(context, "No APOD url!", Toast.LENGTH_SHORT).show()
        } else {
            binding.imageView.load(url) {
                lifecycle(this@MainFragment)
            }
        }
    }

    private fun handleVideoUrl(url: String?) {
        binding.videoGottenLayout.visibility = View.VISIBLE
        binding.imageView.visibility = View.GONE
        binding.videoButton.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }

    private fun setPhotoDescriptionIntoBottomSheet(title: String?, description: String?) {
        val t = if (title.isNullOrEmpty()) "No title" else title
        val d = if (description.isNullOrEmpty()) "No description" else description

        with(binding.includedBottomSheet) {
            bottomSheetDescriptionHeader.text = t
            bottomSheetDescription.text = d
        }
    }
}
