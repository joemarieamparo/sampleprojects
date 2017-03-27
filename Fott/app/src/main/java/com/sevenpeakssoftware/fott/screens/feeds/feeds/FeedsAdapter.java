package com.sevenpeakssoftware.fott.screens.feeds.feeds;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sevenpeakssoftware.fott.R;
import com.sevenpeakssoftware.fott.models.Article;
import com.sevenpeakssoftware.fott.utils.Util;

import io.realm.RealmResults;

public class FeedsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int EMPTY_VIEW = -1;
    private static final int DOTS_VIEW = -2;

    private RealmResults<Article> articles;
    private LayoutInflater inflater;

    private OnItemClickListener onItemClickListener;

    private ImageLoader imageLoader;

    public FeedsAdapter(RealmResults<Article> articles) {
        this.articles = articles;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == EMPTY_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_empty, parent, false);
            EmptyViewHolder empty = new EmptyViewHolder(view);
            return empty;
        }
        if (viewType == DOTS_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dots, parent, false);
            DotsViewHolder dots = new DotsViewHolder(view);
            return dots;
        }

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_feed_item, parent, false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position > articles.size() -1) {
            return;
        }
        if (holder instanceof FeedViewHolder) {
            final Article article = articles.get(position == 0 ? position : position - 1);

            final FeedViewHolder viewHolder = (FeedViewHolder) holder;

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(R.drawable.drawer_image)
                    .showImageOnFail(R.drawable.drawer_image)
                    .showImageOnLoading(R.drawable.drawer_image).build();
            imageLoader.displayImage(article.getImageUrl(), viewHolder.image, options);
            viewHolder.title.setText(article.getTitle());
            viewHolder.dateTime.setText(Util.formatDate(article.getDateTime()));
            viewHolder.ingress.setText(article.getIngress());
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onClick(viewHolder, article);
                    }
                }
            });
        }



    }

    @Override
    public int getItemCount() {
        return articles.size() > 0 ? articles.size() + (articles.size() - 1) : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (articles.size() == 0) {
            return EMPTY_VIEW;
        }

        if (position % 2 != 0) {
            return DOTS_VIEW;
        }

        return super.getItemViewType(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public interface OnItemClickListener {
        public void onClick(FeedViewHolder holder, Article article);
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public AppCompatImageView image;
        public AppCompatTextView title;
        public TextView dateTime;
        public TextView ingress;
        public ImageView showDetail;

        public FeedViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.cardView);
            image = (AppCompatImageView) view.findViewById(R.id.image);
            title = (AppCompatTextView) view.findViewById(R.id.title);
            dateTime = (AppCompatTextView) view.findViewById(R.id.dateTime);
            ingress = (AppCompatTextView) view.findViewById(R.id.ingress);
            showDetail = (ImageView) view.findViewById(R.id.showDetail);
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }
    public class DotsViewHolder extends RecyclerView.ViewHolder {
        public DotsViewHolder(View itemView) {
            super(itemView);
        }
    }
}