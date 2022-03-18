package ru.dvn.moviesearch.model.movie.detail

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import ru.dvn.moviesearch.BuildConfig
import ru.dvn.moviesearch.view.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

const val EXTRA_DETAIL_ID = "EXTRA_DETAIL_ID"

class DetailService(val name: String = "detailService") : IntentService(name) {
    private val intent = Intent(EXTRA_DETAIL_ACTION)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            loadDetail(it.getIntExtra(EXTRA_DETAIL_ID, 1))
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadDetail(id: Int) {
        try {
            val url = URL("https://kinopoiskapiunofficial.tech/api/v2.2/films/$id")
            val handler = Handler()
            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = (url.openConnection() as HttpsURLConnection).apply {
                    requestMethod = "GET"
                    readTimeout = 10000
                    addRequestProperty("X-API-KEY", BuildConfig.MOVIES_API_KEY)
                }
                val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val json = reader.lines().collect(Collectors.joining("\n"))
                val film = Gson().fromJson(json, MovieDetailDto::class.java)

                handler.post {
                    onResponse(film)
                }
            } catch (e: Exception) {
                handler.post {
                    onError()
                }
                e.printStackTrace()
            } finally {
                urlConnection.disconnect()
            }
        } catch (e: MalformedURLException) {
            onError()
            e.printStackTrace()
        }
    }

    private fun onResponse(movieDetailDto: MovieDetailDto) {
        onSuccess(movieDetailDto)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun onSuccess(movieDetailDto: MovieDetailDto) {
        putStatus(EXTRA_DETAIL_STATUS_SUCCESS)
        intent.putExtra(EXTRA_DETAIL, movieDetailDto)
    }

    private fun onError() {
        putStatus(EXTRA_DETAIL_STATUS_ERROR)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun putStatus(status: String) {
        intent.putExtra(EXTRA_DETAIL_STATUS, status)
    }
}