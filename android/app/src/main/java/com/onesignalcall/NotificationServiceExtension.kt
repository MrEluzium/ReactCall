package com.onesignalcall;

import android.content.ComponentName
import android.content.Intent

import android.util.Log;
import com.onesignal.OneSignal

import com.onesignal.notifications.IDisplayableMutableNotification;
import com.onesignal.notifications.INotificationReceivedEvent;
import com.onesignal.notifications.INotificationServiceExtension;

class NotificationServiceExtension : INotificationServiceExtension {
    override fun onNotificationReceived(event: INotificationReceivedEvent) {
        val notification: IDisplayableMutableNotification = event.notification
        OneSignal.Notifications.clearAllNotifications()
        event.preventDefault();
        Log.d("OneSignalCall", "CALL - onNotificationReceived")

        // To invoke app to the front on notification
        val intent = Intent(event.context, MainActivity::class.java).apply {
            component = ComponentName(event.context.packageName, MainActivity::class.java.name)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        event.context.applicationContext.startActivity(intent)
        Thread.sleep(3000)

        // val application = event.context.applicationContext as MainApplication
        // application.startVibration()
        event.notification.display()

        //If you need to perform an async action or stop the payload from being shown automatically,
        //use event.preventDefault(). Using event.notification.display() will show this message again.
        //event.preventDefault();
    }
}
