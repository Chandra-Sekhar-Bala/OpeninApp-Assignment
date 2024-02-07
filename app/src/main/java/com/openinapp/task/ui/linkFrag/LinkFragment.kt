package com.openinapp.task.ui.linkFrag

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.chip.Chip
import com.openinapp.task.R
import com.openinapp.task.databinding.FragmentLinkBinding
import com.openinapp.task.helper.CONSTANTS
import com.openinapp.task.helper.LOAD
import com.openinapp.task.helper.SCREEN
import com.openinapp.task.helper.logThis
import com.openinapp.task.model.Link
import com.openinapp.task.model.LinkResponse
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

        viewModel.getDataFromInternet()
        binding.rcyView.adapter = adapter

        setupOnClickListener()
        observeData()
        setupLineChart()
    }

    private fun observeData() {
        binding.txtGreet.text = viewModel.getGreeting()
        viewModel.lineChartData.observe(viewLifecycleOwner) {
            showLineChart(it)
        }

        viewModel.dateTime.observe(viewLifecycleOwner) {
            binding.txtGraphDate.text = it
        }
        viewModel.response.observe(viewLifecycleOwner) {
            showDataOnScreen(it)
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            showShimmer(it)
        }

    }

    private fun showShimmer(it: LOAD) {
        when (it) {
            LOAD.LOADING -> {
                binding.layoutShimmer.root.visibility = View.VISIBLE
                binding.layoutMain.visibility = View.GONE
            }

            LOAD.FINISH -> {
                binding.layoutShimmer.root.visibility = View.GONE
                binding.layoutMain.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Sowing data on recyclerview and saving the whatsapp number if not saved earlier!
     * */
    private fun showDataOnScreen(data: LinkResponse) {

        rcyData.add(data.data.topLinks)
        rcyData.add(data.data.recentLinks)
        // Setting the TopLink by default
        setRcyScreen(SCREEN.TOP)

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

    // Showing the Graph data on screen!
    private fun showLineChart(entries: List<Entry>?) {

        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.fade_blue)

        val dataset = LineDataSet(entries, "").apply {
            color = ContextCompat.getColor(requireContext(), R.color.light_blue)
            mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            fillColor = ContextCompat.getColor(requireContext(), R.color.line_Chart_bg_color)
            setDrawFilled(true)
            fillDrawable = drawable
        }

        val dataValue = LineData(dataset).apply {
        }

        binding.chartLineChart.apply {
            data = dataValue
            animateY(4000)
            invalidate()
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
        setRcyScreen(if (selectedChipId == chipList[0]) SCREEN.TOP else SCREEN.RECENT)
        view?.findViewById<Chip>(otherChipId)?.apply {
            setChipBackgroundColorResource(R.color.white)
            setTextColor(ContextCompat.getColor(requireContext(), R.color.chip_inactive))
        }
    }

    private fun setupLineChart() {
        val customTypeface = ResourcesCompat.getFont(requireContext(), R.font.figtree_regular)
        val text_Color = ContextCompat.getColor(requireContext(), R.color.chip_inactive)
        val gridBgColor = ContextCompat.getColor(
            requireContext(),
            android.R.color.transparent
        )
        binding.chartLineChart.apply {
            xAxis.apply {
                gridColor = gridBgColor
                textColor = text_Color
                textSize = 6f
                typeface = customTypeface
                position = XAxis.XAxisPosition.BOTTOM
                setDrawAxisLine(false)
                setDrawLabels(true)
                setLabelCount(12, false)
                setDrawGridLines(false)
                valueFormatter = MonthValueFormatter()
            }
            description.isEnabled = false
            legend.isEnabled = false
            isClickable = false
            axisRight.isEnabled = false
            axisLeft.setDrawGridLines(false)
            axisLeft.setDrawAxisLine(false)
            setPinchZoom(false)
            isDoubleTapToZoomEnabled = false
        }
    }


    private inner class MonthValueFormatter : ValueFormatter() {
        private val monthLabels = arrayOf(
            "", "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index in 1..12) monthLabels[index] else ""
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

    private fun setRcyScreen(pos: SCREEN) {
        adapter.submitList(
            when (pos) {
                SCREEN.TOP -> rcyData[0]
                SCREEN.RECENT -> rcyData[1]
            }
        )
    }
}