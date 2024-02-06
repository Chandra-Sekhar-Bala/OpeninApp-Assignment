package com.openinapp.task.ui.linkFrag

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.openinapp.task.databinding.ItemLinksBinding
import com.openinapp.task.helper.formatDate
import com.openinapp.task.model.Link
import com.squareup.picasso.Picasso

class LinkAdapter : ListAdapter<Link, LinkAdapter.ViewHolder>(diffUtil) {
    private lateinit var context: Context
    var onItemClick: ((String) -> Unit)? = null

    inner class ViewHolder(private val binding: ItemLinksBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Link) {
            Picasso.get().load(item.originalImage).into(binding.imgLogo)
            binding.txtLinkTitle.text = item.title
            binding.txtLinkClicks.text = item.totalClicks.toString()
            binding.txtLink.text = item.webLink
            binding.txtDate.text = item.createdAt.formatDate()

            binding.imgCopyBtn.setOnClickListener {
                copyToClipboard(item.webLink)
            }
        }

        private fun copyToClipboard(webLink: String) {
            val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Link", webLink)
            clipboardManager.setPrimaryClip(clipData)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val view = ItemLinksBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    private object diffUtil : DiffUtil.ItemCallback<Link>() {
        override fun areItemsTheSame(oldItem: Link, newItem: Link): Boolean {
            return oldItem.domainId == newItem.domainId
        }

        override fun areContentsTheSame(oldItem: Link, newItem: Link): Boolean {
            return oldItem == newItem
        }

    }
}