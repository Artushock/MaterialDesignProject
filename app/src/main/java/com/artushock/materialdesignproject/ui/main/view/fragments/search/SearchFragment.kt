package com.artushock.materialdesignproject.ui.main.view.fragments.search

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.text.style.BulletSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.artushock.materialdesignproject.databinding.FragmentSearchStartBinding
import java.util.*

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchStartBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = SearchFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTextInputLayout()
        initTextView()
    }


    @RequiresApi(Build.VERSION_CODES.P)
    private fun initTextView() {
        with(binding.fragmentSearchTextView) {
            val spannable = getSpannable()
            this.text = spannable
            this.setOnClickListener {
                (it as TextView).text = getSpannable()
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun getSpannable(): Spannable {
        val spannable = SpannableString("My text \nbullet one\nbullet two\nbullet three")

        val matches = Regex("bullet\\s").findAll(spannable)
        matches.forEach { f ->
            val idxFirst = f.range.first
            val idxLast = f.range.last

            spannable.setSpan(
                BulletSpan(50, Color.RED, (10..30).random()),
                idxFirst,
                idxLast,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        val mat = Regex("[a-zA-Z]").findAll(spannable)
        mat.forEach { f ->
            val idxFirst = f.range.first
            val idxLast = f.range.last + 1

            var red = (30..255).random()
            var green = (30..255).random()
            var blue = (30..255).random()
            var color = Color.argb(255, red, green, blue)

            spannable.setSpan(
                ForegroundColorSpan(color),
                idxFirst, idxLast,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spannable.setSpan(
                AbsoluteSizeSpan((50..150).random()),
                idxFirst, idxLast,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            if(Random().nextBoolean()){
                red = (30..255).random()
                green = (30..255).random()
                blue = (30..255).random()
                color = Color.argb(255, red, green, blue)
                spannable.setSpan(
                    BackgroundColorSpan(color),
                    idxFirst, idxLast,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        return spannable
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
