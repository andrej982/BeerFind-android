package com.beerfind

import java.io.Serializable

data class Pub constructor(
    val latitude: Double,
    val longitude: Double,
    val prices: Int,
    val name: String,
    val address: String
    ): Serializable

val brnoPubs = listOf(
    Pub(49.196813, 16.609236, 2, "Výčep Na Stojáka", "Běhounská 16"),
    Pub(49.192890, 16.606002, 1, "Pivovarský dům Poupě", "Dominikánská 342"),
    Pub(49.194351, 16.607244, 3, "Wild Thing Brno", "Zámečnická 8")
)

val bratislavaPubs = listOf(
    Pub(48.144352, 17.114187, 1, "U Zlatého Bažanta", "Dunajská 4"),
    Pub(48.143787, 17.106876, 2, "Barrock", "Sedlárska 1"),
    Pub(48.145681, 17.106431, 3, "Channels Club", "Župné námestie 2")
)

val vidinaPubs = listOf(
    Pub(48.355807, 19.657466, 1, "Konkurent", "9. mája 1")
)
