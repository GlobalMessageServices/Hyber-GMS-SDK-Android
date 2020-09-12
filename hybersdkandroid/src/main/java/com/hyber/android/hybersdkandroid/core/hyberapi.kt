package com.hyber.android.hybersdkandroid.core

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.hyber.android.hybersdkandroid.add.Answer
import com.hyber.android.hybersdkandroid.add.GetInfo
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.URL
import java.nio.charset.Charset
import java.security.MessageDigest
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSocketFactory

//class for communication with hyber rest server (REST API)
internal class HyberApi {

    //class init for creation answers
    private var answerForm: Answer = Answer()
    private var osVersionClass: GetInfo = GetInfo()

    //parameters for procedures
    private val osVersion: String = osVersionClass.getAndroidVersion()

    //function for create special token for another procedures
    private fun hash(sss: String): String {
        try {
            val bytes = sss.toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            val resp: String = digest.fold("", { str, it -> str + "%02x".format(it) })
            Log.d(TAG, "Result: OK, Function: hash, Class: HyberApi, input: $sss, output: $resp")
            return resp
        } catch (e: Exception) {
            Log.d(
                TAG,
                "Result: FAILED, Function: hash, Class: HyberApi, input: $sss, output: failed"
            )
            return "failed"
        }
    }

    //POST procedure for new registration
    fun hDeviceRegister(
        xPlatformClientAPIKey: String,
        X_Hyber_Session_Id: String,
        X_Hyber_App_Fingerprint: String,
        device_Name: String,
        device_Type: String,
        os_Type: String,
        sdk_Version: String,
        user_Pass: String,
        user_Phone: String,
        context: Context
    ): HyberDataApi2 {
        var functionNetAnswer = HyberFunAnswerRegister()
        var functionCodeAnswer = 0

        val threadNetF1 = Thread(Runnable {
            try {
                Log.d(
                    TAG,
                    "Result: Start step1, Function: hyber_device_register, Class: HyberApi, xPlatformClientAPIKey: $xPlatformClientAPIKey, X_Hyber_Session_Id: $X_Hyber_Session_Id, X_Hyber_App_Fingerprint: $X_Hyber_App_Fingerprint, device_Name: $device_Name, device_Type: $device_Type, os_Type: $os_Type, sdk_Version: $sdk_Version, user_Pass: $user_Pass, user_Phone: $user_Phone"
                )
                val message =
                    "{\"userPhone\":\"$user_Phone\",\"userPass\":\"$user_Pass\",\"osType\":\"$os_Type\",\"osVersion\":\"$osVersion\",\"deviceType\":\"$device_Type\",\"deviceName\":\"$device_Name\",\"sdkVersion\":\"$sdk_Version\"}"
                //val message = "{\"userPhone\":\"$user_Phone\",\"userPass\":\"$user_Pass\",\"osType\":\"$os_Type\",\"osVersion\":\"$os_version\",\"deviceType\":\"$device_Type\",\"deviceName\":\"$device_Name\",\"sdkVersion\":\"$sdk_Version\"}"

                Log.d(
                    TAG,
                    "Result: Start step2, Function: hyber_device_register, Class: HyberApi, message: $message"
                )

                val currentTimestamp = System.currentTimeMillis()
                val postData: ByteArray = message.toByteArray(Charset.forName("UTF-8"))
                val mURL = URL(PushSdkParameters.branch_current_active.fun_hyber_url_registration)
                val urlc = mURL.openConnection() as HttpsURLConnection
                urlc.doOutput = true
                urlc.setRequestProperty("Content-Language", "en-US")
                urlc.setRequestProperty("X-Hyber-Client-API-Key", xPlatformClientAPIKey)
                urlc.setRequestProperty("Content-Type", "application/json")
                urlc.setRequestProperty("X-Hyber-Session-Id", X_Hyber_Session_Id)
                urlc.setRequestProperty("X-Hyber-App-Fingerprint", X_Hyber_App_Fingerprint)
                urlc.sslSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory

                with(urlc) {
                    // optional default is GET
                    requestMethod = "POST"
                    doOutput = true
                    //doInput = true
                    //useCaches = false
                    //setRequestProperty("","")
                    Log.d(TAG, "URL : $url")

                    Log.d(TAG, "start DataOutputStream")
                    val wr = DataOutputStream(outputStream)
                    Log.d(TAG, "start write")

                    wr.write(postData)

                    Log.d(TAG, "end write")
                    wr.flush()
                    Log.d(
                        TAG,
                        "Result: Finished step3, Function: hyber_device_register, Class: HyberApi, Response Code : $responseCode"
                    )
                    functionCodeAnswer = responseCode
                    if (responseCode == 200) {

                        BufferedReader(InputStreamReader(inputStream)).use {
                            val response = StringBuffer()

                            var inputLine = it.readLine()
                            while (inputLine != null) {
                                response.append(inputLine)
                                inputLine = it.readLine()
                            }
                            it.close()
                            Log.d(
                                TAG,
                                "Result: Finished step4, Function: hyber_device_register, Class: HyberApi, Response : $response"
                            )
                            functionNetAnswer = answerForm.registerProcedureAnswer2(
                                responseCode.toString(),
                                response.toString(),
                                context
                            )
                        }
                    } else {
                        functionNetAnswer = answerForm.registerProcedureAnswer2(
                            responseCode.toString(),
                            "unknown",
                            context
                        )

                    }
                }
            } catch (e: Exception) {

                Log.d(
                    TAG,
                    "Result: Failed step5, Function: hyber_device_register, Class: HyberApi, exception: ${e.stackTrace}"
                )
                functionNetAnswer = answerForm.registerProcedureAnswer2(
                    "705",
                    "unknown",
                    context
                )
            }
        })

        threadNetF1.start()
        threadNetF1.join()

        return HyberDataApi2(functionNetAnswer.code, functionNetAnswer, 0)
    }

    //POST
    fun hDeviceRevoke(
        dev_list: String,
        X_Hyber_Session_Id: String,
        X_Hyber_Auth_Token: String
    ): HyberDataApi {

        var functionNetAnswer2 = String()

        val threadNetF2 = Thread(Runnable {

            try {

                Log.d(
                    TAG,
                    "Result: Start step1, Function: hyber_device_revoke, Class: HyberApi, dev_list: $dev_list, X_Hyber_Session_Id: $X_Hyber_Session_Id, X_Hyber_Auth_Token: $X_Hyber_Auth_Token"
                )

                val message2 = "{\"devices\":$dev_list}"

                Log.d(
                    TAG,
                    "Result: Start step2, Function: hyber_device_revoke, Class: HyberApi, message2: $message2"
                )

                val currentTimestamp2 = System.currentTimeMillis() // We want timestamp in seconds
                //val date = Date(currentTimestamp * 1000) // Timestamp must be in ms to be converted to Date

                println(currentTimestamp2)
                //println(date)

                val authToken = hash("$X_Hyber_Auth_Token:$currentTimestamp2")


                val postData2: ByteArray = message2.toByteArray(Charset.forName("UTF-8"))

                val mURL2 = URL(PushSdkParameters.branch_current_active.fun_hyber_url_revoke)

                val urlc2 = mURL2.openConnection() as HttpsURLConnection
                urlc2.doOutput = true
                urlc2.setRequestProperty("Content-Language", "en-US")
                //urlc.setRequestProperty("X-Hyber-Client-API-Key", "test");
                urlc2.setRequestProperty("Content-Type", "application/json")
                urlc2.setRequestProperty("X-Hyber-Session-Id", X_Hyber_Session_Id)
                //urlc.setRequestProperty("X-Hyber-App-Fingerprint", "1");
                urlc2.setRequestProperty("X-Hyber-Timestamp", currentTimestamp2.toString())
                urlc2.setRequestProperty("X-Hyber-Auth-Token", authToken)

                urlc2.sslSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory


                with(urlc2) {
                    // optional default is GET
                    requestMethod = "POST"
                    doOutput = true
                    //doInput = true
                    //useCaches = false
                    //setRequestProperty("","")

                    //val wr = OutputStreamWriter(getOutputStream());

                    val wr = DataOutputStream(outputStream)

                    println("URL3 : $url")
                    wr.write(postData2)

                    println("URL2 : $url")

                    wr.flush()

                    Log.d(TAG, "URL : $url")

                    Log.d(TAG, "Response Code : $responseCode")

                    try {
                        BufferedReader(InputStreamReader(inputStream)).use {
                            val response = StringBuffer()

                            var inputLine = it.readLine()
                            while (inputLine != null) {
                                response.append(inputLine)
                                inputLine = it.readLine()
                            }
                            it.close()
                            println("Response : $response")
                        }
                    } catch (e: Exception) {
                        println("Failed")
                    }

                    functionNetAnswer2 = responseCode.toString()
                }
            } catch (e: Exception) {
                functionNetAnswer2 = "500"
            }
        })

        threadNetF2.start()
        threadNetF2.join()

        return HyberDataApi(functionNetAnswer2.toInt(), "{}", 0)
    }

    //GET
    fun hGetMessageHistory(
        X_Hyber_Session_Id: String,
        X_Hyber_Auth_Token: String,
        period_in_seconds: Int
    ): HyberFunAnswerGeneral {

        var functionNetAnswer3 = String()
        var functionCodeAnswer3 = 0

        val thread_net_f3 = Thread(Runnable {
            try {

                val currentTimestamp2 =
                    System.currentTimeMillis() - period_in_seconds // We want timestamp in seconds
                //val date = Date(currentTimestamp * 1000) // Timestamp must be in ms to be converted to Date

                println(currentTimestamp2)
                //println(date)

                val authToken = hash("$X_Hyber_Auth_Token:$currentTimestamp2")

                Log.d(
                    TAG,
                    "\nSent 'GET' request to hyber_get_device_all with : X_Hyber_Session_Id : $X_Hyber_Session_Id; X_Hyber_Auth_Token : $X_Hyber_Auth_Token; period_in_seconds : $period_in_seconds"
                )


                val mURL2 =
                    URL(PushSdkParameters.branch_current_active.hyber_url_message_history + currentTimestamp2.toString())

                //val urlc2 = mURL2.openConnection() as HttpsURLConnection

                with(mURL2.openConnection() as HttpsURLConnection) {
                    requestMethod = "GET"  // optional default is GET

                    //doOutput = true
                    setRequestProperty("Content-Language", "en-US")
                    //urlc.setRequestProperty("X-Hyber-Client-API-Key", "test");
                    setRequestProperty("Content-Type", "application/json")
                    setRequestProperty("X-Hyber-Session-Id", X_Hyber_Session_Id)
                    //urlc.setRequestProperty("X-Hyber-App-Fingerprint", "1");
                    setRequestProperty("X-Hyber-Timestamp", currentTimestamp2.toString())
                    setRequestProperty("X-Hyber-Auth-Token", authToken)

                    sslSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory

                    requestMethod = "GET"

                    Log.d(TAG, "\nSent 'GET' request to URL : $url; Response Code : $responseCode")
                    functionCodeAnswer3 = responseCode

                    inputStream.bufferedReader().use {

                        //println(it.readLine())
                        functionNetAnswer3 = it.readLine().toString()

                    }
                }
            } catch (e: Exception) {

                Log.d(
                    TAG,
                    "Result: Failed step5, Function: hyber_device_register, Class: HyberApi, exception: ${e.stackTrace}"
                )
                functionCodeAnswer3 = 700
                functionNetAnswer3 = "Failed"

                //final_answer = HyberFunAnswerGeneral(700, "Failed", "Rest Api thread exception", "{}")
            }

        })

        thread_net_f3.start()
        thread_net_f3.join()
        return HyberFunAnswerGeneral(functionCodeAnswer3, "OK", "Processed", functionNetAnswer3)

    }


    //GET
    fun hGetDeviceAll(X_Hyber_Session_Id: String, X_Hyber_Auth_Token: String): HyberDataApi {

        try {

            var functionNetAnswer4 = String()
            var functionCodeAnswer4 = 0

            val thread_net_f4 = Thread(Runnable {


                try {

                    val currentTimestamp2 =
                        System.currentTimeMillis() // We want timestamp in seconds
                    //val date = Date(currentTimestamp * 1000) // Timestamp must be in ms to be converted to Date

                    println(currentTimestamp2)
                    //println(date)

                    val authToken = hash("$X_Hyber_Auth_Token:$currentTimestamp2")

                    Log.d(
                        TAG,
                        "Result: Start step1, Function: hyber_get_device_all, Class: HyberApi, X_Hyber_Session_Id: $X_Hyber_Session_Id, X_Hyber_Auth_Token: $X_Hyber_Auth_Token, currentTimestamp2: $currentTimestamp2, auth_token: $authToken"
                    )

                    val mURL2 =
                        URL(PushSdkParameters.branch_current_active.fun_hyber_url_get_device_all)

                    //val urlc2 = mURL2.openConnection() as HttpsURLConnection


                    with(mURL2.openConnection() as HttpsURLConnection) {
                        requestMethod = "GET"  // optional default is GET
                        //doOutput = true
                        setRequestProperty("Content-Language", "en-US")
                        //urlc.setRequestProperty("X-Hyber-Client-API-Key", "test");
                        setRequestProperty("Content-Type", "application/json")
                        setRequestProperty("X-Hyber-Session-Id", X_Hyber_Session_Id)
                        //urlc.setRequestProperty("X-Hyber-App-Fingerprint", "1");
                        setRequestProperty("X-Hyber-Timestamp", currentTimestamp2.toString())
                        setRequestProperty("X-Hyber-Auth-Token", authToken)

                        sslSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory

                        //requestMethod = "GET"

                        Log.d(
                            TAG,
                            "\nSent 'GET' request to URL : $url; Response Code : $responseCode"
                        )
                        functionCodeAnswer4 = responseCode

                        //if (responseCode==401) { init_hyber.clearData() }

                        inputStream.bufferedReader().use {

                            //println(it.readLine())
                            functionNetAnswer4 = it.readLine().toString()

                            Log.d(
                                TAG,
                                "Result: Finish step2, Function: hyber_get_device_all, Class: HyberApi, function_net_answer4: $functionNetAnswer4"
                            )
                        }
                    }
                } catch (e: Exception) {
                    Log.d(
                        TAG,
                        "Result: Failed step3, Function: hyber_get_device_all, Class: HyberApi, exception: $e"
                    )
                    functionNetAnswer4 = "Failed"
                }
            })

            thread_net_f4.start()
            thread_net_f4.join()
            return HyberDataApi(functionCodeAnswer4, functionNetAnswer4, 0)

        } catch (e: Exception) {
            return HyberDataApi(700, "Failed", 0)
        }

    }

    //POST
    fun hDeviceUpdate(
        X_Hyber_Auth_Token: String,
        X_Hyber_Session_Id: String,
        device_Name: String,
        device_Type: String,
        os_Type: String,
        sdk_Version: String,
        fcm_Token: String
    ): HyberDataApi {

        var functionNetAnswer5 = String()
        var functionCodeAnswer5 = 0

        val threadNetF5 = Thread(Runnable {

            try {
                val message =
                    "{\"fcmToken\": \"$fcm_Token\",\"osType\": \"$os_Type\",\"osVersion\": \"$osVersion\",\"deviceType\": \"$device_Type\",\"deviceName\": \"$device_Name\",\"sdkVersion\": \"$sdk_Version\" }"
                println(message)


                val currentTimestamp = System.currentTimeMillis()

                val currentTimestamp2 = System.currentTimeMillis() // We want timestamp in seconds
                //val date = Date(currentTimestamp * 1000) // Timestamp must be in ms to be converted to Date

                println(currentTimestamp2)
                println(X_Hyber_Session_Id)
                //println(date)

                val auth_token = hash("$X_Hyber_Auth_Token:$currentTimestamp2")

                println(auth_token)

                val postData: ByteArray = message.toByteArray(Charset.forName("UTF-8"))

                val mURL = URL(PushSdkParameters.branch_current_active.fun_hyber_url_device_update)

                val urlc = mURL.openConnection() as HttpsURLConnection
                urlc.doOutput = true
                urlc.setRequestProperty("Content-Language", "en-US")
                //urlc.setRequestProperty("X-Hyber-Client-API-Key", X_Hyber_Client_API_Key);
                urlc.setRequestProperty("Content-Type", "application/json")
                urlc.setRequestProperty("X-Hyber-Session-Id", X_Hyber_Session_Id)
                //urlc.setRequestProperty("X-Hyber-App-Fingerprint", X_Hyber_App_Fingerprint);
                urlc.setRequestProperty("X-Hyber-Auth-Token", auth_token)
                urlc.setRequestProperty("X-Hyber-Timestamp", currentTimestamp2.toString())
                urlc.sslSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory

                with(urlc) {
                    // optional default is GET
                    requestMethod = "POST"
                    doOutput = true
                    //doInput = true
                    //useCaches = false
                    //setRequestProperty("","")


                    println("URL : $url")

                    //val wr = OutputStreamWriter(getOutputStream());

                    val wr = DataOutputStream(outputStream)

                    //println("URL3 : $url")
                    wr.write(postData)

                    //println("URL2 : $url")

                    wr.flush()

                    //println("URL : $url")
                    //println("Response Code : $responseCode")
                    functionCodeAnswer5 = responseCode

                    //if (responseCode==401) { init_hyber.clearData() }

                    BufferedReader(InputStreamReader(inputStream)).use {
                        val response = StringBuffer()

                        var inputLine = it.readLine()
                        while (inputLine != null) {
                            response.append(inputLine)
                            inputLine = it.readLine()
                        }
                        it.close()
                        //println("Response : $response")
                        functionNetAnswer5 = response.toString()
                    }
                }

            } catch (e: Exception) {

                Log.d(
                    TAG,
                    "Result: Failed step5, Function: hyber_device_register, Class: HyberApi, exception: ${e.stackTrace}"
                )
                functionNetAnswer5 = "Failed"
            }


        })

        threadNetF5.start()
        threadNetF5.join()

        return HyberDataApi(functionCodeAnswer5, functionNetAnswer5, 0)


    }

    //POST
    fun hMessageCallback(
        message_id: String,
        hyber_answer: String,
        X_Hyber_Session_Id: String,
        X_Hyber_Auth_Token: String
    ): HyberDataApi {


        var functionNetAnswer6 = String()
        var functionCodeAnswer6 = 0

        val threadNetF6 = Thread(Runnable {

            try {

                //val stringBuilder = StringBuilder("{\"name\":\"Cedric Beust\", \"age\":23}")
                //val message = "{\"name\":\"Cedric Beust\", \"age\":23}"
                val message2 = "{\"messageId\": \"$message_id\", \"answer\": \"$hyber_answer\"}"

                Log.d(TAG, "Body message to hyber : $message2")

                val currentTimestamp2 = System.currentTimeMillis() // We want timestamp in seconds
                //val date = Date(currentTimestamp * 1000) // Timestamp must be in ms to be converted to Date

                println(currentTimestamp2)
                //println(date)

                val authToken = hash("$X_Hyber_Auth_Token:$currentTimestamp2")


                val postData2: ByteArray = message2.toByteArray(Charset.forName("UTF-8"))

                //println(stringBuilder)

                val mURL2 =
                    URL(PushSdkParameters.branch_current_active.fun_hyber_url_message_callback)

                val urlc2 = mURL2.openConnection() as HttpsURLConnection
                urlc2.doOutput = true
                urlc2.setRequestProperty("Content-Language", "en-US")
                //urlc.setRequestProperty("X-Hyber-Client-API-Key", "test");
                urlc2.setRequestProperty("Content-Type", "application/json")
                urlc2.setRequestProperty("X-Hyber-Session-Id", X_Hyber_Session_Id)
                //urlc.setRequestProperty("X-Hyber-App-Fingerprint", "1");
                urlc2.setRequestProperty("X-Hyber-Timestamp", currentTimestamp2.toString())
                urlc2.setRequestProperty("X-Hyber-Auth-Token", authToken)

                urlc2.sslSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory


                with(urlc2) {
                    // optional default is GET
                    requestMethod = "POST"
                    doOutput = true
                    //doInput = true
                    //useCaches = false
                    //setRequestProperty("","")

                    //val wr = OutputStreamWriter(getOutputStream());

                    val wr = DataOutputStream(outputStream)

                    println("URL3 : $url")
                    wr.write(postData2)
                    println("URL2 : $url")
                    wr.flush()
                    Log.d(TAG, "URL : $url")
                    Log.d(TAG, "Response Code : $responseCode")
                    functionCodeAnswer6 = responseCode
                    // if (responseCode==401) { init_hyber.clearData() }

                    BufferedReader(InputStreamReader(inputStream)).use {
                        val response = StringBuffer()

                        var inputLine = it.readLine()
                        while (inputLine != null) {
                            response.append(inputLine)
                            inputLine = it.readLine()
                        }
                        it.close()
                        println("Response : $response")
                        //function_net_answer6 = responseCode.toString() + " " + "Response : $response"
                        functionNetAnswer6 = response.toString()
                    }
                }

            } catch (e: Exception) {

                Log.d(
                    TAG,
                    "Result: Failed step5, Function: hyber_device_register, Class: HyberApi, exception: ${e.stackTrace}"
                )

            }
        })

        threadNetF6.start()
        threadNetF6.join()
        return HyberDataApi(functionCodeAnswer6, functionNetAnswer6, 0)


    }

    //POST
    fun hMessageDr(
        message_id: String,
        X_Hyber_Session_Id: String,
        X_Hyber_Auth_Token: String
    ): HyberDataApi {

        if (X_Hyber_Session_Id != "" && X_Hyber_Auth_Token != "" && message_id != "") {

            var functionNetAnswer7 = String()

            val threadNetF7 = Thread(Runnable {

                try {
                    //val message2 = StringBuilder("{\"messageId\": \"$message_id\"}")
                    val message2 = "{\"messageId\": \"$message_id\"}"

                    Log.d(TAG, "Body message to hyber : $message2")

                    val currentTimestamp2 =
                        System.currentTimeMillis() // We want timestamp in seconds
                    //val date = Date(currentTimestamp * 1000) // Timestamp must be in ms to be converted to Date

                    Log.d(TAG, "Timestamp : $currentTimestamp2")

                    val auth_token = hash("$X_Hyber_Auth_Token:$currentTimestamp2")


                    val postData2: ByteArray = message2.toByteArray(Charset.forName("UTF-8"))

                    val mURL2 = URL(PushSdkParameters.branch_current_active.fun_hyber_url_message_dr)

                    val urlc2 = mURL2.openConnection() as HttpsURLConnection
                    urlc2.doOutput = true
                    urlc2.setRequestProperty("Content-Language", "en-US")
                    //urlc.setRequestProperty("X-Hyber-Client-API-Key", "test");
                    urlc2.setRequestProperty("Content-Type", "application/json")
                    urlc2.setRequestProperty("X-Hyber-Session-Id", X_Hyber_Session_Id)
                    //urlc.setRequestProperty("X-Hyber-App-Fingerprint", "1");
                    urlc2.setRequestProperty("X-Hyber-Timestamp", currentTimestamp2.toString())
                    urlc2.setRequestProperty("X-Hyber-Auth-Token", auth_token)

                    urlc2.sslSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory


                    with(urlc2) {
                        // optional default is GET
                        requestMethod = "POST"
                        doOutput = true
                        //doInput = true
                        //useCaches = false
                        //setRequestProperty("","")

                        //val wr = OutputStreamWriter(getOutputStream());

                        val wr = DataOutputStream(outputStream)
                        wr.write(postData2)
                        wr.flush()
                        Log.d(TAG, "URL : $url")
                        Log.d(TAG, "Response Code : $responseCode")
                        //if (responseCode==401) { init_hyber.clearData() }

                        BufferedReader(InputStreamReader(inputStream)).use {
                            val response = StringBuffer()

                            var inputLine = it.readLine()
                            while (inputLine != null) {
                                response.append(inputLine)
                                inputLine = it.readLine()
                            }
                            it.close()
                            Log.d(TAG, "Response : $response")
                        }
                        functionNetAnswer7 = responseCode.toString()
                    }
                } catch (e: Exception) {
                    functionNetAnswer7 = "500"
                }
            })
            threadNetF7.start()
            threadNetF7.join()
            return HyberDataApi(functionNetAnswer7.toInt(), "{}", 0)
        } else {
            return HyberDataApi(700, "{}", 0)
        }
    }
}