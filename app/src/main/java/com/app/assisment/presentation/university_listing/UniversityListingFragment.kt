package com.app.assisment.presentation.university_listing

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.assisment.R
import com.app.assisment.common.DataStatus
import com.app.assisment.data.remote.model.UniversityResponseItem
import com.app.assisment.databinding.FragmentUniversityListingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UniversityListingFragment : Fragment(), UniversityListingAdapter.UniversityItemListener {

    private lateinit var binding: FragmentUniversityListingBinding
    private val viewModel: UniversityListingViewModel by viewModels()
    private lateinit var adapter: UniversityListingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentUniversityListingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onClickedItem(item: UniversityResponseItem) {
        /*** Navigate to details screen by passing model ***/
        val action =
            UniversityListingFragmentDirections.actionListingFragmentToDetailsFragment(
                item
            )
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObserver()
        observeNavigationCallBack()
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.list.collect {
                    when (it) {
                        is DataStatus.Success -> {
                            binding.progressBar.visibility = View.GONE
                            it.data?.let { list -> renderList(list) }
                            binding.rvListing.visibility = View.VISIBLE
                        }

                        is DataStatus.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.rvListing.visibility = View.GONE
                        }

                        is DataStatus.Error -> {
                            if (it.data!!.isNotEmpty()) {
                                /*** Local data fetched ***/
                                binding.progressBar.visibility = View.GONE
                                renderList(it.data)
                                binding.rvListing.visibility = View.VISIBLE
                            } else {
                                /*** Show connectivity error ***/
                                binding.progressBar.visibility = View.GONE
                                binding.errorTxt.visibility = View.VISIBLE
                                binding.errorTxt.text = getString(R.string.error_unknown)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun renderList(list: List<UniversityResponseItem>) {
        /*** Add data in adapter ***/
        adapter.clear()
        adapter.addData(list)
    }

    private fun setupUI() {
        binding.rvListing.apply {
            layoutManager = LinearLayoutManager(
                context
            )
            setHasFixedSize(true)
        }
        adapter = UniversityListingAdapter(this, arrayListOf())
        binding.rvListing.adapter = adapter
    }

    private fun refreshApi() {
        /*** Refresh listing api ***/
        viewModel.getUniversityListing()
    }

    private fun observeNavigationCallBack() {
        /*** Triggers when press refresh on details screen ***/
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("key")
            ?.observe(viewLifecycleOwner) {
                findNavController().currentBackStackEntry?.savedStateHandle?.remove<Boolean>("key")
                refreshApi()
            }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }
}