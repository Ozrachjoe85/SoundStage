package com.soundstage.app.data

import android.content.ContentResolver
import android.provider.MediaStore
import com.soundstage.app.data.models.Track

class MusicRepository(private val resolver: ContentResolver) {

    fun getAllTracks(): List<Track> {
        val tracks = mutableListOf<Track>()

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION
        )

        val cursor = resolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection, null, null, null
        )

        cursor?.use {
            val idCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val dataCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val durationCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (it.moveToNext()) {
                tracks.add(
                    Track(
                        id = it.getLong(idCol),
                        title = it.getString(titleCol) ?: "Unknown Title",
                        artist = it.getString(artistCol) ?: "Unknown Artist",
                        album = it.getString(albumCol) ?: "Unknown Album",
                        path = it.getString(dataCol) ?: "",
                        duration = it.getLong(durationCol)
                    )
                )
            }
        }
        return tracks
    }
}
