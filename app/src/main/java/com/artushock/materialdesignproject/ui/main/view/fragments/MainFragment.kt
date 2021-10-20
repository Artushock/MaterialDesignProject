package com.artushock.materialdesignproject.ui.main.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.artushock.materialdesignproject.R
import com.artushock.materialdesignproject.data.model.PictureOfTheDayData
import com.artushock.materialdesignproject.databinding.MainFragmentBinding
import com.artushock.materialdesignproject.ui.main.viewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainFragment : Fragment() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomSheetBehavior(binding.includedBottomSheet.bottomSheetContainer)
        initViewModel()
        initTextInputLayout()
    }

    private fun initTextInputLayout() {
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout){
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun initViewModel() {
        viewModel.getData().observe(viewLifecycleOwner, { renderData(it) })
    }

    private fun renderData(data: PictureOfTheDayData?) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
//                binding.title.text = serverResponseData.title
//                binding.description.text = serverResponseData.explanation
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    //Todo
                } else {
                    binding.imageView.load(url) {
                        lifecycle(this@MainFragment)
                    }
                }
            }
        }
    }
}
