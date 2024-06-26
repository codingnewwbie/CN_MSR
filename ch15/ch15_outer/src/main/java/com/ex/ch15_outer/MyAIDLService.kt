package com.ex.ch15_outer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.MediaStore.Audio.Media

class MyAIDLService : Service() {

    lateinit var player: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer()
    }

    override fun onDestroy() {
        player.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return object : MyAIDLInterface.Stub() {
            override fun getMaxDuration(): Int {
//                Maxduration 음악 실행되는 길이
                return if (player.isPlaying)
                    player.duration
                else 0
            }

            override fun start() {
                if (!player.isPlaying) {
                    player = MediaPlayer.create(this@MyAIDLService, R.raw.music)
                    try {
                        player.start()
                    } catch (e: Exception) {
                        e.printStackTrace()
//                    try catch - 예외처리
                    }
                }
            }

            override fun stop() {
                if (player.isPlaying)
                    player.stop()
            }
        }
    }
}