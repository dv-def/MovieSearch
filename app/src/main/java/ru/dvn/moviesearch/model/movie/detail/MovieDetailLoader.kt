package ru.dvn.moviesearch.model.movie.detail

import android.os.Build
import android.os.Handler
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import ru.dvn.moviesearch.BuildConfig
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class MovieDetailLoader(
    private val listener: MovieDetailsLoaderListener
) {

    interface MovieDetailsLoaderListener {
        fun onLoaded(movieDetailDto: MovieDetailDto)
        fun onFailed(throwable: Throwable)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadMovie(id: Int) {
        try {
            val url = URL("https://kinopoiskapiunofficial.tech/api/v2.2/films/$id")
            val handler = Handler()
            Thread {
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
                        listener.onLoaded(movieDetailDto = film)
                    }
                } catch (e: Exception) {
                    handler.post {
                        listener.onFailed(e)
                    }
                    e.printStackTrace()
                } finally {
                    urlConnection.disconnect()
                }
            }.start()
        } catch (e: MalformedURLException) {
            listener.onFailed(e)
            e.printStackTrace()
        }

    }
}