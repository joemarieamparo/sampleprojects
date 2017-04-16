package com.mindandinnovation.chatapp.screens.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.mindandinnovation.chatapp.R;
import com.mindandinnovation.chatapp.screens.chat.ChatActivity;
import com.mindandinnovation.chatapp.utils.NetworkUtil;
import com.mindandinnovation.chatapp.utils.UiUtil;
import com.mindandinnovation.chatapp.utils.ValidatorUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lenovo ideapad on 4/15/2017.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.mainLayout)
    CoordinatorLayout mainLayout;
    @BindView(R.id.btnLogin)
    ActionProcessButton btnLogin;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            showChatActivity();
        }

        setupWindowAnimations();
    }

    @OnClick(R.id.btnLogin)
    public void onLoginClick(View view) {
        updatLoginButtonUi(ActionProcessButton.Mode.ENDLESS, 1);
        UiUtil.hideSoftKeyboard(this);
        if (!NetworkUtil.isConnected(this)) {
            showSnackbar(getString(R.string.no_network_available));
            updatLoginButtonUi(ActionProcessButton.Mode.PROGRESS, 0);
            return;
        }

        if (!validateData()) {
            updatLoginButtonUi(ActionProcessButton.Mode.PROGRESS, 0);
            return;
        }
        login(etUsername.getText().toString(), etPassword.getText().toString());
    }

    private void updatLoginButtonUi(ActionProcessButton.Mode mode, int progress) {
        btnLogin.setMode(mode);
        btnLogin.setProgress(progress);
    }

    private void login(final String email, final String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "login:onComplete: " + email + " > " + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            if (task.getException() != null && task.getException() instanceof
                                    FirebaseAuthInvalidUserException) {
                                createAccount(email, password);
                            } else {
                                updatLoginButtonUi(ActionProcessButton.Mode.PROGRESS, 0);
                                showSnackbar(task.getException().getMessage());
                            }
                            return;
                        }
                        showChatActivity();
                    }
                });
    }

    private boolean validateData() {
        etUsername.setError(null);
        etPassword.setError(null);

        if (ValidatorUtil.isNull(etUsername.getText().toString())) {
            etUsername.setError(getResources().getString(R.string.required));
            etUsername.requestFocus();
            return false;
        }

        if (!ValidatorUtil.validEmail(etUsername.getText().toString())) {
            etUsername.setError(getResources().getString(R.string.invalid_email_format));
            etUsername.requestFocus();
            return false;
        }

        if (ValidatorUtil.isNull(etPassword.getText().toString())) {
            etPassword.setError(getResources().getString(R.string.required));
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void createAccount(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            updatLoginButtonUi(ActionProcessButton.Mode.PROGRESS, 0);
                            showSnackbar(task.getException().getMessage());
                            return;
                        }
                        showChatActivity();
                    }
                });
    }

    private void showChatActivity() {
        startActivity(new Intent(LoginActivity.this, ChatActivity.class));
        finish();
    }

    private void setupWindowAnimations() {
        Transition slide = TransitionInflater.from(this).inflateTransition(R.transition.activity_slide);
        getWindow().setExitTransition(slide);
    }


    private void showSnackbar(String message) {
        final Snackbar snackbar = Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG);
        snackbar.setAction(android.R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.setActionTextColor(ContextCompat.getColor(this, android.R.color.white));
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
        snackbar.show();
    }
}
