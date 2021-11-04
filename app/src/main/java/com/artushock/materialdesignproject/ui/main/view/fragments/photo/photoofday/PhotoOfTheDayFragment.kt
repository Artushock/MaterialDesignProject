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
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.*
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
        savedInstanceState: Bundle?,
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
        val chipGroup = binding.photoOfTheDayChipGroup
        image.setOnClickListener {
            isImageExpanded = !isImageExpanded
            TransitionManager.beginDelayedTransition(
                binding.photoOfTheDayContainer, TransitionSet()
                    .addTransition(Fade())
                    .addTransition(ChangeBounds())
                    .addTransition(ChangeImageTransform())
            )

            val params: ViewGroup.LayoutParams = image.layoutParams
            params.height = if (isImageExpanded)
                ViewGroup.LayoutParams.MATCH_PARENT
            else
                ViewGroup.LayoutParams.WRAP_CONTENT
            image.layoutParams = params

            if (isImageExpanded) {
                image.scaleType = ImageView.ScaleType.CENTER_CROP
                chipGroup.visibility = View.GONE
            } else {
                image.scaleType = ImageView.ScaleType.FIT_CENTER
                chipGroup.visibility = View.VISIBLE
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initChips() {
        binding.todayPhotoChip.setOnCheckedChangeListener { chip: CompoundButton?, _ ->
            animateChip(chip)
        }
        binding.yesterdayPhotoChip.setOnCheckedChangeListener { chip: CompoundButton?, _ ->
            animateChip(chip)
        }
        binding.dayBeforeYesterdayPhotoChip.setOnCheckedChangeListener { chip: CompoundButton?, _ ->
            animateChip(chip)
        }

        with(binding) {
            todayPhotoChip.setOnClickListener {
                viewModel.getCurrentDayData().observe(viewLifecycleOwner, {
                    todayPhotoChip.isChecked = true
                    renderData(it)
                })
            }
            yesterdayPhotoChip.setOnClickListener {
                viewModel.getYesterdayData().observe(viewLifecycleOwner, {
                    yesterdayPhotoChip.isChecked = true
                    renderData(it)
                })
            }
            dayBeforeYesterdayPhotoChip.setOnClickListener {
                viewModel.getDayBeforeYesterdayData()
                    .observe(viewLifecycleOwner, {
                        dayBeforeYesterdayPhotoChip.isChecked = true
                        renderData(it)
                    })
            }
        }
    }

    private fun animateChip(chip: CompoundButton?) {
        val chipAnimationScale = 1.2f
        chip?.let {
            if (chip.isChecked) {
                chip.animate()
                    .scaleX(chipAnimationScale)
                    .scaleY(chipAnimationScale)
                    .setInterpolator(BounceInterpolator())
                    .setDuration(1000)
            } else {
                chip.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setInterpolator(BounceInterpolator())
                    .setDuration(1000)
            }
        }
    }

    private fun setBottomSheetBehavior(container: LinearLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(container)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        animateFabRotation(0f)
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        animateFabRotation(90f)
                    }
                    else -> {
                        //do nothing
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //do nothing
            }

        })

        initFabInfo()
    }

    private fun initFabInfo() {
        binding.fabInfo.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                animateFabRotation(0f)
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                animateFabRotation(90f)
            }
        }
    }

    private fun animateFabRotation(angle: Float) {
        val fabRotationObjectAnimator = ObjectAnimator.ofFloat(
            binding.fabInfo,
            "rotation",
            angle
        )
        fabRotationObjectAnimator.duration = 1000
        fabRotationObjectAnimator.interpolator = BounceInterpolator()
        fabRotationObjectAnimator.start()
        isInfoExpanded = !isInfoExpanded
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