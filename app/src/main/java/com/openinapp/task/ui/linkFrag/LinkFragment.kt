package com.openinapp.task.ui.linkFrag

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.openinapp.task.R
import com.openinapp.task.databinding.FragmentLinkBinding
import com.openinapp.task.helper.CONSTANTS
import com.openinapp.task.helper.logThis
import com.openinapp.task.model.Link
import com.openinapp.task.model.Screen
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
    private val chipList =
        listOf(
            R.id.chip_top,
            R.id.chip_recent,
        )
    private val rcyData = mutableListOf<List<Link>>()
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

            rcyData.add(data.data.topLinks)
            rcyData.add(data.data.recentLinks)
            // Setting the TopLink by default
            setRcyScreen(Screen.TOP)

            logThis("Data on UI : $data")
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
        binding.chipGroup.setOnCheckedStateChangeListener { _, chipIds ->
            setChip(chipIds)
        }
    }

    private fun setChip(chipIds: List<Int>) {
        val selectedChipId = chipIds[0]
        val otherChipId = if (selectedChipId == chipList[0]) chipList[1] else chipList[0]

        view?.findViewById<Chip>(selectedChipId)?.apply {
            setChipBackgroundColorResource(R.color.chip_active)
            setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
        setRcyScreen(if (selectedChipId == chipList[0]) Screen.TOP else Screen.RECENT)
        view?.findViewById<Chip>(otherChipId)?.apply {
            setChipBackgroundColorResource(R.color.white)
            setTextColor(ContextCompat.getColor(requireContext(), R.color.chip_inactive))
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

    private fun setRcyScreen(pos: Screen) {
        adapter.submitList(
            when (pos) {
                Screen.TOP -> rcyData[0]
                Screen.RECENT -> rcyData[1]
            }
        )
    }
}