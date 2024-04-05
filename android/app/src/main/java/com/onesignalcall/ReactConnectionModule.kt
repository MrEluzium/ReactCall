package com.onesignalcall
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class ReactConnectionModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun getName() = "ReactConnectionModule"

    @ReactMethod
    fun callStartEvent() {

    }

    @ReactMethod
    fun callStopEvent() {
        val application = reactApplicationContext.applicationContext as MainApplication
        application.cancelCall()
    }
}