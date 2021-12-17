package com.scb.app.ui.home

import android.os.Parcel
import com.scb.app.util.KParcelable
import com.scb.app.util.parcelableCreator

data class MovieView(val id: String, val name: String, val poster: String) : KParcelable {
    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::MovieView)
    }

    constructor(parcel: Parcel) : this(parcel.readString()!!, parcel.readString()!!, parcel.readString()!!)

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeString(id)
            writeString(name)
            writeString(poster)
        }
    }
}
