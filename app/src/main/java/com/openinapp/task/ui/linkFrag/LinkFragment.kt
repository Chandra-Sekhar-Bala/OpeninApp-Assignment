package com.openinapp.task.ui.linkFrag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.openinapp.task.databinding.FragmentLinkBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LinkFragment : Fragment() {

    private val binding: FragmentLinkBinding by lazy { FragmentLinkBinding.inflate(layoutInflater) }
    private val viewModel: LinkViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDataFromInternet()
        setupOnClickListener()
    }

    private fun setupOnClickListener() {

    }


}