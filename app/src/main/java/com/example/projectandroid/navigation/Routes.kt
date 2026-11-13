package com.example.projectandroid.navigation

object Routes {

    const val HOME = "home"

    const val CATEGORIES =
        "categories/{section}"

    const val DETAILS =
        "details/{type}/{id}"

    const val FAVORITES =
        "favorites"

    fun categories(
        section: String
    ): String {
        return "categories/$section"
    }

    fun details(
        type: String,
        id: Int
    ): String {
        return "details/$type/$id"
    }
}