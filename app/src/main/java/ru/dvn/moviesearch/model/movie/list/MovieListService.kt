package ru.dvn.moviesearch.model.movie.list

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

const val EXTRA_MOVIE_LOAD_MODE = "EXTRA_MOVIE_LOAD_MODE"

class MovieListService() : Service() {
    private val intent = Intent(MOVIE_LIST_RECEIVER_ACTION)

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            it.extras?.getString(EXTRA_MOVIE_LOAD_MODE)?.let { mode ->
                loadMovieList(mode)
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadMovieList(loadMode: String) {
        try {
            val url = URL("https://kinopoiskapiunofficial.tech/api/v2.2/films/top?type=$loadMode")
            val handler = Handler()

            lateinit var urlConnection: HttpsURLConnection
            Thread {
                try {
                    urlConnection = (url.openConnection() as HttpsURLConnection).apply {
                        requestMethod = "GET"
                        readTimeout = 10000
                        addRequestProperty("X-API-KEY", BuildConfig.MOVIES_API_KEY)
                    }

                    val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val json = reader.lines().collect(Collectors.joining("\n"))

                    val movieList = Gson().fromJson(json, MovieList::class.java)

                    handler.post {
                        onResponse(loadMode, movieList)
                    }

                } catch (e: Exception) {
                    handler.post {
                        onError(loadMode)
                    }
                } finally {
                    urlConnection.disconnect()
                }
            }.start()
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
    }

    private fun onResponse(loadMode: String, movieList: MovieList) {
        onSuccess(loadMode, movieList)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        this.stopSelf()
    }

    private fun onSuccess(loadMode: String, movieList: MovieList) {
        putStatus(EXTRA_TOP_FILMS_STATUS_SUCCESS)
        intent.putExtra(EXTRA_LOAD_MODE, loadMode)
        intent.putExtra(EXTRA_MOVIE_LIST, movieList)
    }

    private fun onError(loadMode: String) {
        putStatus(EXTRA_TOP_FILMS_STATUS_ERROR)
        intent.putExtra(EXTRA_LOAD_MODE, loadMode)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        this.stopSelf()
    }

    private fun putStatus(status: String) {
        intent.putExtra(EXTRA_TOP_FILMS_STATUS, status)
    }
}