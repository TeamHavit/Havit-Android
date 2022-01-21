package org.sopt.havit

data class ContentsFinalData(
    var title : String,
    var description : String?,
    var image : String?,
    var url : String,
    var isNotified : Boolean,
    var notificationTime : String,
    var categoryIds : MutableList<Int>
)
