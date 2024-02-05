package com.openinapp.task.ui.linkFrag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.openinapp.task.R

class LinkFragment : Fragment() {

    private lateinit var viewModel: LinkViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_link, container, false)
    }
}