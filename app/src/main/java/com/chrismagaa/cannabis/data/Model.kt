package com.chrismagaa.cannabis

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml
import java.util.*

@Xml(name = "rss")
data class RSSResponse(
   @Element
    var channel: Channel? = null
)

@Xml(name = "channel")
data class Channel(
    @PropertyElement(name = "title")
    var title: String? = null,
    @Element(name = "item")
    var itemList: List<Item>? = null
)

@Xml(name = "item")
data class Item(
    @TextContent
    var titleBlog: String? = "",
    @PropertyElement(name = "title")
    var title: String = "",
    @PropertyElement(name = "pubDate")
    var date: Date,
    @PropertyElement(name = "description")
    var description: String = "",
    @PropertyElement(name = "content:encoded")
    var content: String? = "",
    @PropertyElement(name = "link")
    var link: String = "",
)