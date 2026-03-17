package com.soundstage.app.data

import android.content.ContentResolver
import android.provider.MediaStore
import com.soundstage.app.data.models.Track

class MusicRepository(private val resolver: ContentResolver) {

    fun getAllTracks(): List<Track> {
        val tracks = mutableListOf<Track>()

        val cursor = resolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null, null, null, null
        )

        cursor?.use {
            val titleCol = it.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val artistCol = it.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val dataCol = it.getColumnIndex(MediaStore.Audio.Media.DATA)

            while (it.moveToNext()) {
                tracks.add(
                    Track(
                        id = it.getLong(0),
                        title = it.getString(titleCol),
                        artist = it.getString(artistCol),
                        album = "Unknown",
                        path = it.getString(dataCol),
                        duration = 0L
                    )
                )
            }
        }

        return tracks
    }
}
