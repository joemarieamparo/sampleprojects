package com.mindandinnovation.chatapp.screens.chat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mindandinnovation.chatapp.R;
import com.mindandinnovation.chatapp.widgets.AppChatMessageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lenovo ideapad on 4/15/2017.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.messageLayout)
    AppChatMessageView messageLayout;
    @BindView(R.id.tvMessage)
    TextView tvMessage;
    @BindView(R.id.tvUser)
    TextView tvUser;

    public MessageViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}