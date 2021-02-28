package com.elevenetc.zipher.shared

import platform.Foundation.NSUUID

//import platform.Foundation.NSUUID
//import cocoapods.AFNetworking.AFHTTPRequestQueryStringDefaultStyle

actual fun randomUUID(): String {
    //return cocoapods.AFNetworking.AFHTTPSessionManager.manager().toString()
    return NSUUID().UUIDString()
}