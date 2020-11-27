package com.example.itda

data class BoardDTO(var boardName:String?=null, var date:Long?=null, var email:String?=null, var like:Long?=null, var replies:MutableList<String?>?=null, var text:String?=null, var title:String?=null)
