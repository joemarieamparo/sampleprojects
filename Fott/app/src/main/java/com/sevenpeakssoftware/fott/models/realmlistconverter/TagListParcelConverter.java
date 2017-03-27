package com.sevenpeakssoftware.fott.models.realmlistconverter;

import com.sevenpeakssoftware.fott.models.Tag;

import org.parceler.Parcels;

public class TagListParcelConverter extends RealmListParcelConverter<Tag> {

    @Override
    public void itemToParcel(Tag input, android.os.Parcel parcel) {
        parcel.writeParcelable(Parcels.wrap(input), 0);
    }

    @Override
    public Tag itemFromParcel(android.os.Parcel parcel) {
        return Parcels.unwrap(parcel.readParcelable(Tag.class.getClassLoader()));
    }
}