package com.example.itda

data class BoardDTO(var date:Long?=null, var email:String?=null, var like:Long?=null, var reply:Map<String,String>?=null, var text:String?=null, var title:String?=null)
