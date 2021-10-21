package com.artushock.materialdesignproject.ui.main.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.artushock.materialdesignproject.databinding.FragmentChipsBinding
import com.google.android.material.chip.Chip

class ChipsFragment : Fragment() {

    private var _binding: FragmentChipsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChipsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setChipsGroupListener()

        binding.chipClose.setOnCloseIconClickListener{
            Toast.makeText(context, "Close is Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setChipsGroupListener() {
        binding.chipGroup.setOnCheckedChangeListener { group, position ->
            group.findViewById<Chip>(position)?.let {
                Toast.makeText(context, "Chosen: ${it.text}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}