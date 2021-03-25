package com.embedded.model

import com.embedded.Constant

data class LoginReq(var secret:String=Constant.WEBSERVICE.secret, var apikey:String=Constant.WEBSERVICE.apikey)