package com.sevenpeakssoftware.fott.models;

import org.parceler.Parcel;

import io.realm.RealmObject;
import io.realm.TagRealmProxy;
import io.realm.annotations.RealmClass;

@RealmClass
@Parcel(implementations = {TagRealmProxy.class},
        value = Parcel.Serialization.BEAN,
        analyze = {Tag.class})
public class Tag extends RealmObject {

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
