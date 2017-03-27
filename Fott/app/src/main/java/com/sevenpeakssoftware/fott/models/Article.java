package com.sevenpeakssoftware.fott.models;

import com.google.gson.annotations.SerializedName;
import com.sevenpeakssoftware.fott.models.realmlistconverter.ItemListParcelConverter;
import com.sevenpeakssoftware.fott.models.realmlistconverter.TagListParcelConverter;

import org.parceler.Parcel;
import org.parceler.ParcelPropertyConverter;

import java.io.Serializable;

import io.realm.ArticleRealmProxy;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@Parcel(implementations = {ArticleRealmProxy.class},
        value = Parcel.Serialization.BEAN,
        analyze = {Article.class})
public class Article extends RealmObject implements Serializable {

    @PrimaryKey
    @SerializedName("id")
    private long id;
    @SerializedName("title")
    private String title;
    @SerializedName("dateTime")
    private String dateTime;
    @SerializedName("tags")
    private RealmList<Tag> tags = new RealmList<>();
    @SerializedName("content")
    private RealmList<Item> items = new RealmList<>();
    @SerializedName("ingress")
    private String ingress;
    @SerializedName("image")
    private String imageUrl;
    @SerializedName("created")
    private long created;
    @SerializedName("changed")
    private long changed;

    public Article() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public RealmList<Tag> getTags() {
        return tags;
    }

    @ParcelPropertyConverter(TagListParcelConverter.class)
    public void setTags(RealmList<Tag> tags) {
        this.tags = tags;
    }

    public RealmList<Item> getItems() {
        return items;
    }

    @ParcelPropertyConverter(ItemListParcelConverter.class)
    public void setItems(RealmList<Item> items) {
        this.items = items;
    }

    public String getIngress() {
        return ingress;
    }

    public void setIngress(String ingress) {
        this.ingress = ingress;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getChanged() {
        return changed;
    }

    public void setChanged(long changed) {
        this.changed = changed;
    }

}
