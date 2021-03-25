package com.embedded
class Constant {
//signed SHA1-- 65:19:CE:13:AD:8D:8B:2D:70:34:50:93:4D:78:4C:5F:5F:E8:3E:77
    companion object{
        const val URL ="https://vps819243.ovh.net"
    }
    //https://vps819243.ovh.net/calendar/wp-json/simone/v1/agent/1
    interface WEBSERVICE{
          companion object{
              const val RESTSERVICEURL = "$URL/calendar/wp-json/simone/v1/"
              const val secret = "42f98713-66f2-4a03-a307-b978ae5c506e"
              const val apikey = "4e199d89-7737-46e0-bbf6-1f4cb9e8306c"
          }
    }

    interface SERVICE_API{
        companion object{
            const val DIRECT_LOGIN = "direct/login"
            const val USER_REG = "direct/user-reg"
            const val NEW_USER_TOKEN = "direct/preauth-token/{userId}"
            const val CLOSE_ACCOUNT = "direct/close-account/{userId}"
            const val CHANGE_EMAIL = "direct/change-email/{userId}"
            const val CHANGE_PHONE = "direct/change-mobile/{userId}"
            const val CHANGE_USER_HOST = "direct/change-host/{userId}"
            const val UPDATE_REFRESH_DATE = "direct/update-refresh/{userId}"
            const val CUSTOMER_REFRESH_TOKEN = "direct/refresh-token"
            const val AGENT = "agent/1"
        }
    }

    interface SERVICE_TYPE{
        companion object{
            const val GET_NEW_ORDER = "GetNewOrder"
        }
    }

    interface STATUS{
        companion object{
            const val FAIL = "fail"
            const val SUCCESS = "success"
        }
    }
    interface LOGIN_TYPE{
        companion object{
            const val DIRCT = 1
            const val EFX = 2
            const val USER = 3
        }
    }
    interface API_TYPE{
        companion object{
            const val REGISTER = 1
            const val NEW_USER_TOKEN = 2
            const val CLOSE_YOUR_ACCOUNT = 3
            const val CHANGE_EMAIL = 4
            const val CHANGE_PHONE = 5
            const val CHANGE_HOST = 6
            const val REFRESH_DATE = 7
            const val REFRESH_TOKEN = 8
        }
    }

    interface CALENDAR{
        companion object{
            const val CALENDAR_ID:Long = 3
        }
    }

}