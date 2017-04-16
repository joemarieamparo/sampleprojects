package com.mindandinnovation.chatapp.screens.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.mindandinnovation.chatapp.R;
import com.mindandinnovation.chatapp.screens.login.LoginActivity;
import com.mindandinnovation.chatapp.utils.NetworkUtil;
import com.mindandinnovation.chatapp.utils.ValidatorUtil;
import com.mindandinnovation.chatapp.widgets.AppChatMessageView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lenovo ideapad on 4/15/2017.
 */

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = ChatActivity.class.getSimpleName();

    private static final String REMOTE_CONFIG_MESSAGE_LENGTH = "message_length";
    private static final String JSON_TAG_MESSAGE = "message";
    private static final Long DEFAULT_MESSAGE_MAX_LENGTH = 100L;
    private static final long CACHE_EXPIRATION_IN_SECONDS = 3600;

    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.tvLoading)
    TextView tvLoading;
    @BindView(R.id.rvMessages)
    RecyclerView rvMessages;
    @BindView(R.id.btnSend)
    Button btnSend;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btnLogout)
    Button btnLogout;
    @BindView(R.id.mainLayout)
    CoordinatorLayout mainLayout;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseRemoteConfig firebaseRemoteConfig;
    private DatabaseReference firebaseDatabaseReference;

    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        btnLogout.setVisibility(Button.VISIBLE);

        if (!initAuth()) {
            showLogin();
            return;
        }

        initRemoteConfig();
        fetchConfig();
        setUpMessages();
        setUpMessageInput();
        enableSendButton(etMessage.getText().toString());

        setupWindowAnimations();
    }

    private boolean initAuth() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            return false;
        }
        return true;
    }

    private void showLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void initRemoteConfig() {
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings firebaseRemoteConfigSettings =
                new FirebaseRemoteConfigSettings.Builder()
                        .setDeveloperModeEnabled(true)
                        .build();

        Map<String, Object> defaultConfigMap = new HashMap<>();
        defaultConfigMap.put(REMOTE_CONFIG_MESSAGE_LENGTH, DEFAULT_MESSAGE_MAX_LENGTH);

        firebaseRemoteConfig.setConfigSettings(firebaseRemoteConfigSettings);
        firebaseRemoteConfig.setDefaults(defaultConfigMap);
    }

    private void fetchConfig() {
        firebaseRemoteConfig.fetch(CACHE_EXPIRATION_IN_SECONDS)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        firebaseRemoteConfig.activateFetched();
                        applyMessageMaxLength();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error fetching config: " + e.getMessage());
                        applyMessageMaxLength();
                    }
                });
    }

    private void applyMessageMaxLength() {
        etMessage.setFilters(new InputFilter[]{new
                InputFilter.LengthFilter(getMessageMaxLength())});
    }

    private int getMessageMaxLength() {
        Long maxLength = firebaseRemoteConfig.getLong(REMOTE_CONFIG_MESSAGE_LENGTH);
        return maxLength.intValue() > DEFAULT_MESSAGE_MAX_LENGTH.intValue() ?
                maxLength.intValue() : DEFAULT_MESSAGE_MAX_LENGTH.intValue();
    }

    private void setUpMessages() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rvMessages.setLayoutManager(linearLayoutManager);

        firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener
                () {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvLoading.setVisibility(ProgressBar.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                tvLoading.setVisibility(ProgressBar.INVISIBLE);
            }
        });
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(
                Message.class,
                R.layout.layout_message,
                MessageViewHolder.class,
                firebaseDatabaseReference.child(JSON_TAG_MESSAGE)) {

            @Override
            protected void populateViewHolder(final MessageViewHolder viewHolder,
                                              Message message, int position) {
                tvLoading.setVisibility(ProgressBar.INVISIBLE);
                if (!ValidatorUtil.isNull(message.getMessage())) {
                    viewHolder.tvMessage.setText(message.getMessage());
                    viewHolder.tvMessage.setVisibility(TextView.VISIBLE);
                } else {
                    viewHolder.tvMessage.setVisibility(TextView.VISIBLE);
                }

                if (!ValidatorUtil.isNull(message.getUser())) {
                    boolean isOwnMessage = message.getUser().equals(firebaseUser.getEmail());
                    viewHolder.tvUser.setText(isOwnMessage ? "You"
                            : message.getUser().substring(0, message.getUser().lastIndexOf("@")));
                    updateMessageLayout(viewHolder, isOwnMessage);
                    viewHolder.tvUser.setVisibility(TextView.VISIBLE);
                } else {
                    viewHolder.tvUser.setVisibility(TextView.VISIBLE);
                }
            }
        };

        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                int messagesCount = firebaseRecyclerAdapter.getItemCount();
                int lastVisiblePosition =
                        linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (messagesCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    rvMessages.scrollToPosition(positionStart);
                }
            }
        });

        rvMessages.setLayoutManager(linearLayoutManager);
        rvMessages.setAdapter(firebaseRecyclerAdapter);
    }

    private void updateMessageLayout(MessageViewHolder viewHolder, boolean isOwnMessage) {
        LinearLayout.LayoutParams messageLP =
                (LinearLayout.LayoutParams) viewHolder.messageLayout.getLayoutParams();
        if (isOwnMessage) {
            messageLP.setMargins((int) getResources().getDimension(R.dimen.message_horizontal_margin), 0, 0, 0);
            viewHolder.messageLayout.setArrowPosition(AppChatMessageView.ArrowPosition.RIGHT);
        } else {
            messageLP.setMargins(0, 0, (int) getResources().getDimension(R.dimen.message_horizontal_margin), 0);
            viewHolder.messageLayout.setArrowPosition(AppChatMessageView.ArrowPosition.LEFT);
        }
        viewHolder.messageLayout.setLayoutParams(messageLP);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) viewHolder.tvUser.getLayoutParams();
        lp.gravity = isOwnMessage ? Gravity.RIGHT : Gravity.LEFT;
        viewHolder.tvUser.setLayoutParams(lp);

    }

    private void setUpMessageInput() {
        applyMessageMaxLength();
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                enableSendButton(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void enableSendButton(String message) {
        btnSend.setEnabled(message.length() > 0 ? true : false);
        btnSend.setBackground(message.length() > 0 ?
                ContextCompat.getDrawable(this, R.drawable.rounded_corner_enabled_button)
                : ContextCompat.getDrawable(this, R.drawable.rounded_corner_disabled_button));
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

    @OnClick(R.id.btnSend)
    public void sendMessage(View view) {
        if (NetworkUtil.isConnected(this)) {
            firebaseDatabaseReference.child(JSON_TAG_MESSAGE)
                    .push().setValue(new Message(etMessage.getText().toString(), firebaseUser.getEmail()));
            etMessage.setText("");
        } else {
            showSnackbar(getString(R.string.no_network_available));
        }
    }

    @OnClick(R.id.btnLogout)
    public void logout(View view) {
        firebaseAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
