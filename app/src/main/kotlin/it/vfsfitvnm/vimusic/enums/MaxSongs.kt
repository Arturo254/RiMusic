package it.vfsfitvnm.vimusic.enums

enum class MaxSongs {
    `500`,
    `1000`,
    `2000`,
    `3000`,
    Unlimited;

    val number: Long
        get() = when (this) {
            `500` -> 500
            `1000` -> 1000
            `2000` -> 2000
            `3000` -> 3000
            Unlimited -> 1000000
        } * 1L
}