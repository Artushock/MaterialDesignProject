package com.artushock.materialdesignproject.ui.main.view.fragments.photo.photoofday

import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import coil.load
import com.artushock.materialdesignproject.data.model.PictureOfTheDayData
import com.artushock.materialdesignproject.databinding.FragmentPhotoOfTheDayBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class PhotoOfTheDayFragment : Fragment() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var isInfoExpanded = false
    private var isImageExpanded = false

    private var _binding: FragmentPhotoOfTheDayBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoOfTheDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetLayout: LinearLayout = binding.bottomSheetInfo.infoBottomSheetContainer
        setBottomSheetBehavior(bottomSheetLayout)
        initViewModel()
        initChips()
        setImageClickAnimation()
    }

    private fun setImageClickAnimation() {
        val image = binding.photoImageView
        image.setOnClickListener {
            isImageExpanded = !isImageExpanded
            TransitionManager.beginDelayedTransition(
                binding.photoOfTheDayContainer, TransitionSet()
                    .addTransition(ChangeBounds())
                    .addTransition(ChangeImageTransform())
            )

            image.scaleType =
                if (isImageExpanded)
                    ImageView.ScaleType.CENTER_CROP
                else
                    ImageView.ScaleType.FIT_CENTER

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initChips() {
        with(binding) {
            todayPhotoChip.setOnClickListener {
                viewModel.getCurrentDayData().observe(viewLifecycleOwner, {
                    todayPhotoChip.isChecked = true
                    renderData(it)
                })
            }
            yesterdayPhotoChip.setOnClickListener {
                yesterdayPhotoChip.isChecked = true
                viewModel.getYesterdayData().observe(viewLifecycleOwner, { renderData(it) })
            }
            dayBeforeYesterdayPhotoChip.setOnClickListener {
                dayBeforeYesterdayPhotoChip.isChecked = true
                viewModel.getDayBeforeYesterdayData()
                    .observe(viewLifecycleOwner, { renderData(it) })
            }
        }
    }

    private fun setBottomSheetBehavior(container: LinearLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(container)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        initFabInfo()
    }

    private fun initFabInfo() {
        binding.fabInfo.setOnClickListener {
            if (isInfoExpanded) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                val fabRotationObjectAnimator =
                    ObjectAnimator.ofFloat(binding.fabInfo, "rotation", 0f)
                fabRotationObjectAnimator.duration = 1000
                fabRotationObjectAnimator.interpolator = BounceInterpolator()
                fabRotationObjectAnimator.start()
                isInfoExpanded = !isInfoExpanded
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                val fabRotationObjectAnimator =
                    ObjectAnimator.ofFloat(binding.fabInfo, "rotation", 90f)
                fabRotationObjectAnimator.duration = 1000
                fabRotationObjectAnimator.interpolator = BounceInterpolator()
                fabRotationObjectAnimator.start()
                isInfoExpanded = !isInfoExpanded
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initViewModel() {
        viewModel.getCurrentDayData().observe(viewLifecycleOwner, {
            binding.todayPhotoChip.isChecked = true
            renderData(it)
        })
    }

    private fun renderData(data: PictureOfTheDayData?) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                binding.photoImageView.visibility = View.VISIBLE
                binding.photoProgressBar.visibility = View.GONE

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
                binding.photoImageView.visibility = View.GONE
                binding.photoProgressBar.visibility = View.VISIBLE
            }
            is PictureOfTheDayData.Error -> {
                throw data.error
            }
        }
    }

    private fun handleImageUrl(url: String?) {
        binding.videoGottenLayout.visibility = View.GONE
        binding.photoImageView.visibility = View.VISIBLE
        if (url.isNullOrEmpty()) {
            Toast.makeText(context, "NASA sent nothing!", Toast.LENGTH_SHORT).show()
        } else {
            binding.photoImageView.load(url) {
                lifecycle(this@PhotoOfTheDayFragment)
            }
        }
    }

    private fun handleVideoUrl(url: String?) {
        binding.videoGottenLayout.visibility = View.VISIBLE
        binding.photoImageView.visibility = View.GONE
        binding.videoButton.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }

    private fun setPhotoDescriptionIntoBottomSheet(title: String?, description: String?) {
        Log.d("123123123", "$title and $description")
        val t = if (title.isNullOrEmpty()) "No title" else title
        val d = if (description.isNullOrEmpty()) "No description" else description

        with(binding.bottomSheetInfo) {
            infoTitle.text = t
            infoContent.text = d
        }
    }
}