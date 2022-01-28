package org.sopt.havit.util

import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object CallbackUtil {
    fun <ResponseType> Call<ResponseType>.enqueueUtil(
        onSuccess : (ResponseType) -> Unit,
        onError : (ResponseBody?) -> Unit = {}
    ) {
        this.enqueue(object : Callback<ResponseType>{
            override fun onResponse(call: Call<ResponseType>, response: Response<ResponseType>) {
                if (response.isSuccessful){
                    response.body()?.let{
                        onSuccess(it)
                    }?: onError(response.errorBody())
                }
            }

            override fun onFailure(call: Call<ResponseType>, t: Throwable) {
                Log.e("CallbackUtil", t.toString() )
            }

        })
    }
}