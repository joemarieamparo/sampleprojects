package com.sevenpeakssoftware.fott.screens.feeds.baseactivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sevenpeakssoftware.fott.database.RealmController;

import io.realm.Realm;

public class RealmActivity extends AppCompatActivity {
    protected Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = RealmController.init(this).getRealm();
    }
}
