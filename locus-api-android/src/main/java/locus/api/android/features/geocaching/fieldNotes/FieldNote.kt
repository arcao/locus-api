package locus.api.android.features.geocaching.fieldNotes

import locus.api.objects.Storable
import locus.api.objects.geocaching.GeocachingLog
import locus.api.utils.DataReaderBigEndian
import locus.api.utils.DataWriterBigEndian
import java.io.IOException

/**
 * Created by menion on 7. 7. 2014.
 * Class is part of Locus project
 */
class FieldNote : Storable {

    // ID of log in database
    var id: Long = 0
    // code of cache
    var cacheCode = ""
    // name of cache
    var cacheName = ""
    // type of log
    var type = GeocachingLog.CACHE_LOG_TYPE_FOUND
    // time of this log (in UTC+0)
    var time = 0L
    // note of log defined by user
    var note = ""
    // flag if log is marked as mFavorite
    var isFavorite = false
    // flag if log was already send
    var isLogged = false
    // list of attached images
    var images: MutableList<FieldNoteImage> = arrayListOf()
        private set
    // list of attached items
    var items: MutableList<FieldNoteItem> = arrayListOf()
        private set

    /**
     * Default empty constructor.
     */
    constructor() : super()

    @Throws(IOException::class)
    constructor(data: ByteArray) : super(data)

    /**************************************************/
    // STORABLE
    /**************************************************/

    override fun getVersion(): Int {
        return 1
    }

    override fun reset() {
        id = -1L
        cacheCode = ""
        cacheName = ""
        type = GeocachingLog.CACHE_LOG_TYPE_FOUND
        time = 0L
        note = ""
        isFavorite = false
        isLogged = false
        images = arrayListOf()

        // V1
        items = arrayListOf()
    }

    @Throws(IOException::class)
    override fun readObject(version: Int, dr: DataReaderBigEndian) {
        id = dr.readLong()
        cacheCode = dr.readString()
        cacheName = dr.readString()
        type = dr.readInt()
        time = dr.readLong()
        note = dr.readString()
        isFavorite = dr.readBoolean()
        isLogged = dr.readBoolean()
        images = dr.readListStorable(FieldNoteImage::class.java)

        // V1
        if (version >= 1) {
            items = dr.readListStorable(FieldNoteItem::class.java)
        }
    }

    @Throws(IOException::class)
    override fun writeObject(dw: DataWriterBigEndian) {
        dw.writeLong(id)
        dw.writeString(cacheCode)
        dw.writeString(cacheName)
        dw.writeInt(type)
        dw.writeLong(time)
        dw.writeString(note)
        dw.writeBoolean(isFavorite)
        dw.writeBoolean(isLogged)
        dw.writeListStorable(images)

        // V1
        dw.writeListStorable(items)
    }
}
