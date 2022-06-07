package ru.dvn.moviesearch

import android.app.Application
import android.widget.Toast
import androidx.room.Room
import ru.dvn.moviesearch.room.DataBase
import java.lang.IllegalStateException

class App : Application() {
    companion object {
        private const val DB_NAME = "kino_top.db"

        private var appInstance: App? = null

        private var db: DataBase? = null

        fun getDatabase(): DataBase {
            if (db == null) {
                if (appInstance == null) {
                    throw IllegalStateException("Application is null while creating DataBase")
                }

                synchronized(DataBase::class.java) {
                    db = Room.databaseBuilder(
                        appInstance!!.applicationContext,
                        DataBase::class.java,
                        DB_NAME
                    ).build()
                }
            }

            return db!!
        }
    }


    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }
}