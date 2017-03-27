package com.sevenpeakssoftware.fott.network;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sevenpeakssoftware.fott.models.Tag;
import com.sevenpeakssoftware.fott.models.realmlistconverter.TagRealmListConverter;

import io.realm.RealmList;
import io.realm.RealmObject;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "https://www.apphusetreach.no/application/40495/";

    public static Retrofit getBaseClient() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<RealmList<Tag>>() {
                        }.getType(),
                        new TagRealmListConverter())
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}