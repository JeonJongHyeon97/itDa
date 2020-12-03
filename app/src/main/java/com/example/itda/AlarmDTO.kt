package com.example.itda

data class AlarmDTO (
    var destinationEmail: String? = null,//var destinationUid: String? = null,
    var userEmail: String? = null,//var userId: String? = null
//    var uid: String? = null,
//    var kind: Int = 0, //0 : 좋아요, 1: 메세지, 2: 팔로우
    var message: String? = null,
    var boardName: String? = null,
    var postDate: Long? = null,
    var replyDate: Long? = null
)

