package com.openinapp.task.model

import com.google.gson.annotations.SerializedName

data class LinkResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("support_whatsapp_number")
    val supportWhatsappNumber: String,
    @SerializedName("extra_income")
    val extraIncome: Double,
    @SerializedName("total_links")
    val totalLinks: Int,
    @SerializedName("total_clicks")
    val totalClicks: Int,
    @SerializedName("today_clicks")
    val todayClicks: Int,
    @SerializedName("top_source")
    val topSource: String,
    @SerializedName("top_location")
    val topLocation: String,
    @SerializedName("startTime")
    val startTime: String,
    @SerializedName("links_created_today")
    val linksCreatedToday: Int,
    @SerializedName("applied_campaign")
    val appliedCampaign: Int,
    @SerializedName("data")
    val data: Data
)

data class Data(
    @SerializedName("recent_links")
    val recentLinks: List<RecentLink>,
    @SerializedName("top_links")
    val topLinks: List<TopLink>,
    @SerializedName("favourite_links")
    val favouriteLinks: List<Any>,
    @SerializedName("overall_url_chart")
    val overallUrlChart: Map<String, Int>
)

data class RecentLink(
    @SerializedName("url_id")
    val urlId: Int,
    @SerializedName("web_link")
    val webLink: String,
    @SerializedName("smart_link")
    val smartLink: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("total_clicks")
    val totalClicks: Int,
    @SerializedName("original_image")
    val originalImage: String,
    @SerializedName("thumbnail")
    val thumbnail: Any?,
    @SerializedName("times_ago")
    val timesAgo: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("domain_id")
    val domainId: String,
    @SerializedName("url_prefix")
    val urlPrefix: Any?,
    @SerializedName("url_suffix")
    val urlSuffix: String,
    @SerializedName("app")
    val app: String,
    @SerializedName("is_favourite")
    val isFavourite: Boolean
)

data class TopLink(
    @SerializedName("url_id")
    val urlId: Int,
    @SerializedName("web_link")
    val webLink: String,
    @SerializedName("smart_link")
    val smartLink: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("total_clicks")
    val totalClicks: Int,
    @SerializedName("original_image")
    val originalImage: String,
    @SerializedName("thumbnail")
    val thumbnail: Any?,
    @SerializedName("times_ago")
    val timesAgo: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("domain_id")
    val domainId: String,
    @SerializedName("url_prefix")
    val urlPrefix: String,
    @SerializedName("url_suffix")
    val urlSuffix: String,
    @SerializedName("app")
    val app: String,
    @SerializedName("is_favourite")
    val isFavourite: Boolean
)
