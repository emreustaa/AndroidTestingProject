package com.example.artbooktesting.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.example.artbooktesting.R
import com.example.artbooktesting.adapter.ImageRecyclerAdapter
import com.example.artbooktesting.databinding.FragmentImageApiBinding
import com.example.artbooktesting.util.Status
import com.example.artbooktesting.viewmodel.ArtViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImageApiFragment @Inject constructor(private val imageRecyclerAdapter: ImageRecyclerAdapter) :
    Fragment(R.layout.fragment_image_api) {

    lateinit var viewModel: ArtViewModel

    private var fragmentBinding: FragmentImageApiBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentImageApiBinding.bind(view)
        fragmentBinding = binding

        var job: Job? = null


        binding.etSearchText.addTextChangedListener {
            job?.cancel()
            job = lifecycleScope.launch {
                delay(1000)
                it?.let {
                    if (it.toString().isNotEmpty()) {
                        viewModel.searchForImage(it.toString())
                    }
                }
            }
        }
        viewModel = ViewModelProvider(requireActivity()).get(ArtViewModel::class.java)

        binding.rvImageView.adapter = imageRecyclerAdapter
        binding.rvImageView.layoutManager = GridLayoutManager(requireContext(), 3)

        imageRecyclerAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            viewModel.setSelectedImage(it)
        }
        subscribeToObservers()


    }

    private fun subscribeToObservers() {
        viewModel.imageList.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    val urls = it.data?.hits?.map { imageResult ->
                        imageResult.previewURL
                    }
                    imageRecyclerAdapter.images = urls ?: listOf()

                    fragmentBinding?.pbImageArt?.visibility = View.GONE

                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message ?: "Error", Toast.LENGTH_LONG)
                        .show()
                    fragmentBinding?.pbImageArt?.visibility = View.GONE
                }

                Status.LOADING -> {
                    fragmentBinding?.pbImageArt?.visibility = View.VISIBLE
                }
            }
        })
    }

}