package com.artushock.materialdesignproject.ui.main.view.fragments.search

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.artushock.materialdesignproject.databinding.FragmentSearchBinding
import com.artushock.materialdesignproject.databinding.FragmentSearchStartBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchStartBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = SearchFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTextInputLayout()

        initTextView()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initTextView() {
        val textHtml = "<h1>Header</h1><br><p>Some paragraph</p><p>Some list</p><ul><li>First element</li><li>Second element</li></ul>"
        with(binding.fragmentSearchTextView){
            this.text = Html.fromHtml(textHtml, Html.FROM_HTML_SEPARATOR_LINE_BREAK_DIV)
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
}
