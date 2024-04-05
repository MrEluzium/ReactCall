package com.onesignalcall

import VibrationManager
import android.app.Application
import android.content.res.Configuration
import android.util.Log
import com.facebook.react.PackageList
import com.facebook.react.ReactApplication
import com.facebook.react.ReactHost
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.config.ReactFeatureFlags
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.load
import com.facebook.react.defaults.DefaultReactHost.getDefaultReactHost
import com.facebook.react.defaults.DefaultReactNativeHost
import com.facebook.react.flipper.ReactNativeFlipper
import com.facebook.soloader.SoLoader
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import com.onesignal.notifications.INotificationClickEvent
import com.onesignal.notifications.INotificationClickListener
import com.onesignal.notifications.INotificationLifecycleListener
import com.onesignal.notifications.INotificationWillDisplayEvent
import expo.modules.ApplicationLifecycleDispatcher
import expo.modules.ReactNativeHostWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


const val ONESIGNAL_APP_ID = "588d3c9a-33b8-413b-9040-9e4a441d9292"

class MainApplication : Application(), ReactApplication {
  private var vibrationManager: VibrationManager? = null;
  override val reactNativeHost: ReactNativeHost = ReactNativeHostWrapper(
        this,
        object : DefaultReactNativeHost(this) {
          override fun getPackages(): List<ReactPackage> {
            // Packages that cannot be autolinked yet can be added manually here, for example:
            // packages.add(new MyReactNativePackage());
            return PackageList(this).packages.apply {
              add(MyAppPackage())
            }
          }

          override fun getJSMainModuleName(): String = ".expo/.virtual-metro-entry"

          override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG

          override val isNewArchEnabled: Boolean = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
          override val isHermesEnabled: Boolean = BuildConfig.IS_HERMES_ENABLED
      }
  )

  override val reactHost: ReactHost
    get() = getDefaultReactHost(this.applicationContext, reactNativeHost)

  private val lifecycleListener = object : INotificationLifecycleListener {
    override fun onWillDisplay(event: INotificationWillDisplayEvent) {
      Log.d("OneSignalCall", "CALL - onWillDisplay")
      startVibration()
    }
  }


  private val clickListener = object : INotificationClickListener {
    override fun onClick(event: INotificationClickEvent) {
      Log.d("OneSignalCall", "CALL - onClick")
      vibrationManager?.stopVibration()
    }
  }

  override fun onCreate() {
    super.onCreate()
    SoLoader.init(this, false)
    if (!BuildConfig.REACT_NATIVE_UNSTABLE_USE_RUNTIME_SCHEDULER_ALWAYS) {
      ReactFeatureFlags.unstable_useRuntimeSchedulerAlways = false
    }
    if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
      // If you opted-in for the New Architecture, we load the native entry point for this app.
      load()
    }
    if (BuildConfig.DEBUG) {
      ReactNativeFlipper.initializeFlipper(this, reactNativeHost.reactInstanceManager)
    }
    ApplicationLifecycleDispatcher.onApplicationCreate(this)

    OneSignal.Debug.logLevel = LogLevel.NONE
    OneSignal.initWithContext(this, ONESIGNAL_APP_ID)
    CoroutineScope(Dispatchers.IO).launch {
      OneSignal.Notifications.requestPermission(true)
    }

    OneSignal.Notifications.addForegroundLifecycleListener(lifecycleListener)
    OneSignal.Notifications.addClickListener(clickListener)

    OneSignal.Notifications.clearAllNotifications()

    vibrationManager = VibrationManager(this);
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    ApplicationLifecycleDispatcher.onConfigurationChanged(this, newConfig)
  }

  fun cancelCall() {
    OneSignal.Notifications.clearAllNotifications()
    vibrationManager?.stopVibration()
  }

  fun startVibration() {
    Log.d("OneSignalCall", "CALL - startVibration")
    val pattern = longArrayOf(0, 400, 400, 400)
    if(vibrationManager == null) {
      vibrationManager = VibrationManager(this);
    }
    vibrationManager?.startVibration(pattern, 0)
  }
}
