package com.openinapp.task.ui.linkFrag

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.openinapp.task.databinding.FragmentLinkBinding
import com.openinapp.task.helper.CONSTANTS
import com.openinapp.task.helper.logthis
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLEncoder

@AndroidEntryPoint
class LinkFragment : Fragment() {

    private val binding: FragmentLinkBinding by lazy { FragmentLinkBinding.inflate(layoutInflater) }
    private val viewModel: LinkViewModel by viewModels()
    private val shf by lazy {
        requireContext().getSharedPreferences(CONSTANTS.WP, Context.MODE_PRIVATE)
    }

    private val adapter by lazy { LinkAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rcyView.adapter = adapter

        viewModel.getDataFromInternet()
        setupOnClickListener()
        binding.txtGreet.text = viewModel.greetMessage

        viewModel.response.observe(viewLifecycleOwner) { data ->

            adapter.submitList(data.data.topLinks)

            logthis("Data on UI : $data")
            binding.layoutStats.apply {

                txtTdyClicks.text = data.todayClicks.toString()
                if (data.topLocation.isNotEmpty())
                    txtLocation.text = data.topLocation
                if (data.topSource.isNotEmpty())
                    txtSources.text = data.topSource
            }
            if (shf?.getString(CONSTANTS.WP, "")!!.isEmpty()) {
                shf.edit().apply {
                    putString(CONSTANTS.WP_NO, data.supportWhatsappNumber)
                    apply()
                }
            }
        }
    }

    private fun setupOnClickListener() {

        binding.txtTalkOnWp.setOnClickListener {
            talkOnWhatsapp()
        }

        binding.txtFaq.setOnClickListener {
            takeOnWeb()
        }
    }

    private fun takeOnWeb() {
        val faqUrl = "https://openinapp.com/faq"
        val uri = Uri.parse(faqUrl)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun talkOnWhatsapp() {
        val whatsappNumber = shf.getString(CONSTANTS.WP_NO, "")
        val message = "Hello!" // Replace with your desired message

        val uri = Uri.parse(
            "https://api.whatsapp.com/send?phone=$whatsappNumber&text=${
                URLEncoder.encode(message, "UTF-8")
            }"
        )
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)

    }
}