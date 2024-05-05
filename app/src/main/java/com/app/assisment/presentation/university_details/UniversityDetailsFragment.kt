package com.app.assisment.presentation.university_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.assisment.R
import com.app.assisment.data.remote.model.UniversityResponseItem
import com.app.assisment.databinding.FragmentUniversityDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UniversityDetailsFragment : Fragment() {

    private lateinit var binding: FragmentUniversityDetailsBinding
    private val args: UniversityDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUniversityDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindData(args.model)

        binding.refreshImg.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.set("key", true)
            findNavController().popBackStack()
        }
    }

    private fun bindData(universityResponseItem: UniversityResponseItem?) {
        if (universityResponseItem != null) {
            binding.model = universityResponseItem
            if (universityResponseItem.state != null) {
                binding.universityState.text = universityResponseItem.state
            } else {
                binding.universityState.text = "N/A"
            }
        } else {
            Toast.makeText(context, getString(R.string.general_error_msg), Toast.LENGTH_SHORT)
                .show()
        }
    }
}