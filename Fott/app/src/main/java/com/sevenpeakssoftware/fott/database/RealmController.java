package com.sevenpeakssoftware.fott.database;


import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.sevenpeakssoftware.fott.models.Article;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class RealmController {

    private static final String TAG = RealmController.class.getSimpleName();

    private static final String DATABASE_NAME = "fottfeed.realm";

    private static RealmController instance;
    private static Realm realm;

    private RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController init(Activity activity) {
        if (instance == null) {
            Realm.setDefaultConfiguration(new RealmConfiguration.Builder(activity)
                    .name(DATABASE_NAME)
                    .schemaVersion(0)
                    .deleteRealmIfMigrationNeeded()
                    .build());

            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {
        return realm;
    }

    public void refresh() {
        realm.refresh();
    }

    public void clearAll() {
        realm.beginTransaction();
        realm.clear(Article.class);
        realm.commitTransaction();
    }

    private void saveToDatabase(List<Article> articles) {
        for (Article article : articles) {
            realm.beginTransaction();
            try {
                realm.copyToRealmOrUpdate(article);
            } catch (Exception e) {
                Log.e(TAG, "Error during saving data : " + e.getMessage());
            }
            realm.commitTransaction();
        }
    }

    public RealmResults<Article> getFeeds() {
        return realm.where(Article.class).findAll();
    }

    public Article getFeedById(String id) {
        return realm.where(Article.class).equalTo("id", id).findFirst();
    }

    public boolean hasFeeds() {
        return !realm.allObjects(Article.class).isEmpty();
    }
}
