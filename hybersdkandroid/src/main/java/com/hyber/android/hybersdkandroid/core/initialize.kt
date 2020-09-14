package com.hyber.android.hybersdkandroid.core

import android.content.Context
import com.google.firebase.iid.FirebaseInstanceId
import com.hyber.android.hybersdkandroid.logger.HyberLoggerSdk
import java.util.*


//function for initialization different parameters
internal class Initialization(val context: Context) {
    private val sharedPreference: SharedPreference = SharedPreference(context)

    fun hSdkUpdateFirebaseAuto(hyberInternalParamsObj: PushSdkParameters): PushSdkParameters {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            val token = instanceIdResult.token
            if (token != "") {
                sharedPreference.save("firebase_registration_token", token)
                hyberInternalParamsObj.firebase_registration_token = token
                HyberLoggerSdk.debug("Initialization.hSdkUpdateFirebaseAuto.Firebase token: $token")
            } else {
                val firebaseRegistrationToken: String =
                    sharedPreference.getValueString("firebase_registration_token")!!.toString()
                hyberInternalParamsObj.firebase_registration_token = firebaseRegistrationToken
                HyberLoggerSdk.debug("Initialization.hSdkUpdateFirebaseAuto.Firebase token empty loaded: $firebaseRegistrationToken")
            }
        }
        return hyberInternalParamsObj
    }

    fun hSdkUpdateFirebaseManual(x_token: String, hyberInternalParamsObj: PushSdkParameters): PushSdkParameters {
        sharedPreference.save("firebase_registration_token", x_token)
        hyberInternalParamsObj.firebase_registration_token = x_token
        HyberLoggerSdk.debug("Initialization.hSdkUpdateFirebaseManual.Firebase token: $x_token")
        return hyberInternalParamsObj
    }

    private fun paramsLoader(hyberInternalParamsObj: PushSdkParameters): PushSdkParameters {
        HyberLoggerSdk.debug("Initialization.paramsLoader started")

        val hyberUuid: String = sharedPreference.getValueString("hyber_uuid")!!.toString()
        hyberInternalParamsObj.hyber_uuid = hyberUuid

        val devId: String = sharedPreference.getValueString("deviceId")!!.toString()
        hyberInternalParamsObj.deviceId = devId

        val hyberUserMsisdn: String =
            sharedPreference.getValueString("hyber_user_msisdn")!!.toString()
        hyberInternalParamsObj.hyber_user_msisdn = hyberUserMsisdn

        val hyberUserPassword: String =
            sharedPreference.getValueString("hyber_user_Password")!!.toString()
        hyberInternalParamsObj.hyber_user_Password = hyberUserPassword

        val hyberDeviceType: String =
            sharedPreference.getValueString("hyber_deviceType")!!.toString()
        hyberInternalParamsObj.hyber_deviceType = hyberDeviceType

        val hyberDeviceName: String =
            sharedPreference.getValueString("hyber_deviceName")!!.toString()
        hyberInternalParamsObj.hyber_deviceName = hyberDeviceName

        val hyberOsType: String = sharedPreference.getValueString("hyber_osType")!!.toString()
        hyberInternalParamsObj.hyber_osType = hyberOsType

        val hyberRegistrationToken: String =
            sharedPreference.getValueString("hyber_registration_token")!!.toString()
        hyberInternalParamsObj.hyber_registration_token = hyberRegistrationToken

        val hyberUserId: String =
            sharedPreference.getValueString("hyber_user_id")!!.toString()
        hyberInternalParamsObj.hyber_user_id = hyberUserId

        val hyberRegistrationCreatedAt: String =
            sharedPreference.getValueString("hyber_registration_createdAt")!!.toString()
        hyberInternalParamsObj.hyber_registration_createdAt = hyberRegistrationCreatedAt
        HyberLoggerSdk.debug("Initialization.paramsLoader finished: hyberUuid=$hyberUuid, devId=$devId, hyberUserMsisdn=$hyberUserMsisdn, hyberUserPassword=$hyberUserPassword, hyberDeviceType=$hyberDeviceType, hyberDeviceName=$hyberDeviceName, hyberOsType=$hyberOsType, hyberRegistrationToken=$hyberRegistrationToken, hyberUserId=$hyberUserId, hyberRegistrationCreatedAt=$hyberRegistrationCreatedAt")
        return hyberInternalParamsObj
    }

    fun hSdkInit(
        hOsType1: String,
        hDeviceType1: String,
        hDeviceName1: String,
        hUrlsInfo: UrlsPlatformList
    ): PushSdkParameters {
        var hyberInternalParamsObj = PushSdkParameters

        HyberLoggerSdk.debug("Initialization.hSdkInit  started")
        val registrationStatus: Boolean = sharedPreference.getValueBool("registrationstatus", false)
        hyberInternalParamsObj.registrationStatus = registrationStatus
        hyberInternalParamsObj.branch_current_active = hUrlsInfo
        hyberInternalParamsObj.hyber_osType = hOsType1
        hyberInternalParamsObj.hyber_deviceType = hDeviceType1
        hyberInternalParamsObj.hyber_deviceName = hDeviceName1

        HyberLoggerSdk.debug("Initialization.hSdkInit  registrationstatus: $registrationStatus")
        HyberLoggerSdk.debug("Initialization.hSdkInit  hUrlsInfo=$hUrlsInfo, hOsType1=$hOsType1, hDeviceType1=$hDeviceType1, hDeviceName1=$hDeviceName1")

        hyberInternalParamsObj = hSdkUpdateFirebaseAuto(hyberInternalParamsObj)

        if (!registrationStatus) {
            val hyberUuid = UUID.randomUUID().toString()
            sharedPreference.save("hyber_uuid", hyberUuid)
            hyberInternalParamsObj.hyber_uuid = hyberUuid
            sharedPreference.save("hyber_osType", hOsType1)
            sharedPreference.save("hyber_deviceType", hDeviceType1)
            sharedPreference.save("hyber_deviceName", hDeviceName1)
        } else {
            hyberInternalParamsObj = paramsLoader(hyberInternalParamsObj)
        }
        return hyberInternalParamsObj
    }

    fun hSdkInit2(): PushSdkParameters {
        var hyberInternalParamsObj = PushSdkParameters
        HyberLoggerSdk.debug("Initialization.hSdkInit2  started")
        val registrationStatus: Boolean = sharedPreference.getValueBool("registrationstatus", false)
        hyberInternalParamsObj.registrationStatus = registrationStatus
        HyberLoggerSdk.debug("Initialization.hSdkInit2  getValueBool: $registrationStatus")

        hyberInternalParamsObj = hSdkUpdateFirebaseAuto(hyberInternalParamsObj)

        if (!registrationStatus) {
            val hyberUuid = UUID.randomUUID().toString()
            sharedPreference.save("hyber_uuid", hyberUuid)
            hyberInternalParamsObj.hyber_uuid = hyberUuid
        } else {
            hyberInternalParamsObj = paramsLoader(hyberInternalParamsObj)
        }
        return hyberInternalParamsObj
    }

    fun clearData(hyberInternalParamsObj: PushSdkParameters): PushSdkParameters {
        sharedPreference.clearSharedPreference()
        hyberInternalParamsObj.registrationStatus = false
        HyberLoggerSdk.debug("Initialization.clearData  processed")
        return hyberInternalParamsObj
    }
}
