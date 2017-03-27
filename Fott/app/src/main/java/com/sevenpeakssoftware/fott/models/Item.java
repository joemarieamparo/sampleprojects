package com.sevenpeakssoftware.fott.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;

import io.realm.ItemRealmProxy;
import io.realm.RealmObject;

@Parcel(implementations = {ItemRealmProxy.class},
        value = Parcel.Serialization.BEAN,
        analyze = {Item.class})
public class Item extends RealmObject implements Serializable {

    @SerializedName("type")
    private String type;
    @SerializedName("subject")
    private String subject;
    @SerializedName("description")
    private String description;

    public Item() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
