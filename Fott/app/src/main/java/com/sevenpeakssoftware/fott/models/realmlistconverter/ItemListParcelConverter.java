package com.sevenpeakssoftware.fott.models.realmlistconverter;

import com.sevenpeakssoftware.fott.models.Item;

import org.parceler.Parcels;

public class ItemListParcelConverter extends RealmListParcelConverter<Item> {

    @Override
    public void itemToParcel(Item input, android.os.Parcel parcel) {
        parcel.writeParcelable(Parcels.wrap(input), 0);
    }

    @Override
    public Item itemFromParcel(android.os.Parcel parcel) {
        return Parcels.unwrap(parcel.readParcelable(Item.class.getClassLoader()));
    }
}