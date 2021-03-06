@file:Suppress("unused", "SpellCheckingInspection")

package com.hyber.android.hybersdkandroid.core

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.hyber.android.hybersdkandroid.add.GetInfo
import com.hyber.android.hybersdkandroid.add.PushKInternal
import java.net.HttpURLConnection
import java.net.URL


open class PushKPublicParams {

    /*
*To get a Bitmap image from the URL received
* */
    private fun getBitmapFromUrl(imageUrl: String): Bitmap? {
        var ansBitmap: Bitmap? = null
        val threadNetBitmap = Thread(Runnable {
            try {
                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                ansBitmap = BitmapFactory.decodeStream(input)
            } catch (e: Exception) {
                e.printStackTrace()
                ansBitmap = null
            }})
        threadNetBitmap.start()
        threadNetBitmap.join()
        return ansBitmap
    }

    open fun notificationBuilder(
        context: Context,
        notificationTextMess: String,
        image: String
    ): NotificationCompat.Builder {
        val imageBitmap = getBitmapFromUrl(image)
        val intent = Intent(context, context::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        if (imageBitmap != null) {
        return NotificationCompat.Builder(context, "push.push.k.sdk")
            .setContentText(notificationTextMess)
            .setAutoCancel(true)
            //.setSmallIcon(R.drawable.googleg_standard_color_18)
            .setPriority(PushKInternal.notificationPriorityOld(PushSdkParameters.push_notification_display_priority))
            .setSound(defaultSoundUri)
            //.setVibrate(longArrayOf(1000))
            .setContentIntent(pendingIntent)
            .setLargeIcon(imageBitmap)}
        else {
            return NotificationCompat.Builder(context, "push.push.k.sdk")
                .setContentText(notificationTextMess)
                .setAutoCancel(true)
                //.setSmallIcon(R.drawable.googleg_standard_color_18)
                .setPriority(PushKInternal.notificationPriorityOld(PushSdkParameters.push_notification_display_priority))
                .setSound(defaultSoundUri)
                //.setVibrate(longArrayOf(1000))
                .setContentIntent(pendingIntent)
        }
    }

    open fun notificationBuilder(
        context: Context,
        notificationTextMess: String
    ): NotificationCompat.Builder {
        val intent = Intent(context, context::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        return NotificationCompat.Builder(context, "push.push.k.sdk")
            .setContentText(notificationTextMess)
            .setAutoCancel(true)
            //.setSmallIcon(R.drawable.googleg_standard_color_18)
            .setPriority(PushKInternal.notificationPriorityOld(PushSdkParameters.push_notification_display_priority))
            .setSound(defaultSoundUri)
            //.setVibrate(longArrayOf(1000))
            .setContentIntent(pendingIntent)
    }
}


//URLs DATA for Push platform for different branches
object PushSdkParametersPublic {
    val branchMasterValue: UrlsPlatformList = UrlsPlatformList(
        fun_pushsdk_url_device_update = "https://push.hyber.im/api/2.3/device/update",
        fun_pushsdk_url_registration = "https://push.hyber.im/api/2.3/device/registration",
        fun_pushsdk_url_revoke = "https://push.hyber.im/api/2.3/device/revoke",
        fun_pushsdk_url_get_device_all = "https://push.hyber.im/api/2.3/device/all",
        fun_pushsdk_url_message_callback = "https://push.hyber.im/api/2.3/message/callback",
        fun_pushsdk_url_message_dr = "https://push.hyber.im/api/2.3/message/dr",
        fun_pushsdk_url_mess_queue = "https://push.hyber.im/api/2.3/message/queue",
        pushsdk_url_message_history = "https://push.hyber.im/api/2.3/message/history?startDate="
    )
    val branchTestValue: UrlsPlatformList = UrlsPlatformList(
        fun_pushsdk_url_device_update = "https://test-push.hyber.im/api/2.3/device/update",
        fun_pushsdk_url_registration = "https://test-push.hyber.im/api/2.3/device/registration",
        fun_pushsdk_url_revoke = "https://test-push.hyber.im/api/2.3/device/revoke",
        fun_pushsdk_url_get_device_all = "https://test-push.hyber.im/api/2.3/device/all",
        fun_pushsdk_url_message_callback = "https://test-push.hyber.im/api/2.3/message/callback",
        fun_pushsdk_url_message_dr = "https://test-push.hyber.im/api/2.3/message/dr",
        fun_pushsdk_url_mess_queue = "https://test-push.hyber.im/api/2.3/message/queue",
        pushsdk_url_message_history = "https://test-push.hyber.im/api/2.3/message/history?startDate="
    )
    const val TAG_LOGGING = "PushPushSDK"
    const val pushsdk_log_level_error = "error"
    const val pushsdk_log_level_debug = "debug"

}

object PushSdkParameters {
    private var infoLocalDeviceHardware: GetInfo = GetInfo()
    var sdkVersion: String = "1.0.0.42"
    var push_k_osType: String = "android"
    var push_k_deviceName: String = infoLocalDeviceHardware.getDeviceName().toString()

    var push_notification_display_priority: Int = 2
        set(value) {
            if (value > 0) field = value
        }
    //platform url branches. It can be rewrite by Push SDK initiation
    var branch_current_active: UrlsPlatformList = PushSdkParametersPublic.branchMasterValue

}

interface PushKAp
enum class PushKApC : PushKAp {
    BODY
}

internal data class PushKDataApi(
    val code: Int,
    val body: String,
    val time: Int
)


data class HyberFunAnswerRegister(
    val code: Int = 0,
    val result: String = "",
    val description: String = "",
    val deviceId: String = "",
    val token: String = "",
    val userId: String = "",
    val userPhone: String = "",
    val createdAt: String = ""
)

internal data class PushKDataApi2(
    val code: Int,
    val body: HyberFunAnswerRegister,
    val time: Int
)


data class HyberFunAnswerGeneral(
    val code: Int,
    val result: String,
    val description: String,
    val body: String
)

data class UrlsPlatformList(
    val fun_pushsdk_url_device_update: String,
    val fun_pushsdk_url_registration: String,
    val fun_pushsdk_url_revoke: String,
    val fun_pushsdk_url_get_device_all: String,
    val fun_pushsdk_url_message_callback: String,
    val fun_pushsdk_url_message_dr: String,
    val fun_pushsdk_url_mess_queue: String,
    val pushsdk_url_message_history: String
)

data class PushOperativeData(

    //is procedure for register new device completed or not
    // (true - devise exist on server. )
    // false - it s new device and we need to complete push_register_new()
    var registrationStatus: Boolean = false,

    var push_k_user_Password: String = "",
    var push_k_user_msisdn: String = "",
    var push_k_registration_token: String = "",
    var push_k_user_id: String = "",
    var push_k_registration_createdAt: String = "",
    var firebase_registration_token: String = "",
    var push_k_registration_time: String = "",

    //uuid generates only one time
    var push_k_uuid: String = "",

    //its deviceId which we receive from server with answer for push_register_new()
    var deviceId: String = ""

)