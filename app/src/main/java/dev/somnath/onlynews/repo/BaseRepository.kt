package dev.somnath.onlynews.repo

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.somnath.onlynews.models.ErrorResponse
import dev.somnath.onlynews.utils.state.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.net.ConnectException

abstract class BaseRepository {


    suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<T>,
        dataBaseCall: suspend() -> Flow<Any>,
        insertToDataBaseCall: suspend(List<Any>) -> Unit
    ): Flow<DataState<T?>> = flow {
        emit(DataState.Loading)
        try {
            val response = apiCall()
            Log.i("Response aya", response.body().toString())



            if (response.isSuccessful) {
                emit(DataState.Success(response.body()))
            } else {
                val gson = Gson()
                val type = object : TypeToken<ErrorResponse>() {}.type
                var errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)
                if (errorResponse != null) {
                    emit(DataState.Error(errorResponse.message))
                }

            }



        } catch (e: Exception) {
            if (e is ConnectException) {
                emit( DataState.Error(" No network connection "))
            } else {
                emit( DataState.Error(" error "))
            }
        }

    }


}