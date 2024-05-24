package com.example.l3ezlaapp.Model

import android.os.Parcel
import android.os.Parcelable
import java.sql.Date

data class ItemModel(
    var id: Int = 0,
    var title: String = "",
    var description: String = "",
    var picUrl: ArrayList<String> = ArrayList(),
    var price: Double = 0.0,
    var numberInCart: Int = 0,
    var isLiked: Boolean = false,
    var ordered: Boolean = false,
    var userID: String = "",

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: ArrayList(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeStringList(picUrl)
        parcel.writeDouble(price)
        parcel.writeInt(numberInCart)
        parcel.writeByte(if (isLiked) 1 else 0)
        parcel.writeByte(if (ordered) 1 else 0)
        parcel.writeString(userID)
    }
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ItemModel> {
        override fun createFromParcel(parcel: Parcel): ItemModel {
            return ItemModel(parcel)
        }

        override fun newArray(size: Int): Array<ItemModel?> {
            return arrayOfNulls(size)
        }
    }
}


