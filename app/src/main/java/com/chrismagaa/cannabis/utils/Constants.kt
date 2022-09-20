package com.chrismagaa.cannabis.utils

enum class RSSFeeds(val url: String, val title: String) {
    CANNABIS_NET("https://cannabis.net/rss/blog", "Cannabis.net"),
    CBD_ORIGIN("https://cbdorigin.com/feed/", "CDB Origin"),
    CANADELICS("https://cannadelics.com/feed/","Cannadelics"),
    CANNABIS_SEEDS_STORE("https://www.cannabis-seeds-store.co.uk/Cannabis-Seeds-News/feed", "Cannabis Seeds Store"),
    //Aquí puedes agregar más fuentes confiables de RSS
}

enum class ModeTheme(val value: Int) {
    LIGHT(0),
    DARK(1)
}