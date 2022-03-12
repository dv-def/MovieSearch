package ru.dvn.moviesearch.model.movie

import android.os.Build
import android.os.Handler
import android.util.Log
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

class MovieListLoader(
    private val listener: MovieListLoaderListener,
    private val moviesLoadMode: MoviesLoadMode
) {
    interface MovieListLoaderListener {
        fun onLoaded(movieList: MovieList)
        fun onFailed(throwable: Throwable)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadMovieList() {
        try {
            val url = URL("https://kinopoiskapiunofficial.tech/api/v2.2/films/top?type=${moviesLoadMode.getMode()}")
            val handler = Handler()
            Thread {
                lateinit var urlConnection: HttpsURLConnection
                try {
                    urlConnection = (url.openConnection() as HttpsURLConnection).apply {
                        requestMethod = "GET"
                        readTimeout = 10000
                        addRequestProperty("X-API-KEY", BuildConfig.MOVIES_API_KEY)
//                        addRequestProperty("type", moviesLoadMode.name)
                    }

                    Log.d("LOADER", urlConnection.url.toString())
                    Log.d("LOADER", urlConnection.toString())
                    val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val json = reader.lines().collect(Collectors.joining("\n"))

                    val movieList = Gson().fromJson(json, MovieList::class.java)

                    handler.post {
                        listener.onLoaded(movieList)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    handler.post {
                        listener.onFailed(e)
                    }
                } finally {
                    urlConnection.disconnect()
                }
            }.start()

        } catch (e: MalformedURLException) {
            e.printStackTrace()
            listener.onFailed(e)
        }
    }
}