package com.sevenpeakssoftware.fott.screens.feeds.feeddetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sevenpeakssoftware.fott.R;
import com.sevenpeakssoftware.fott.models.Article;
import com.sevenpeakssoftware.fott.models.Item;
import com.sevenpeakssoftware.fott.screens.feeds.baseactivity.RealmActivity;
import com.sevenpeakssoftware.fott.utils.Util;

import org.parceler.Parcels;

public class FeedDetailActivity extends RealmActivity {

    public static final String ARTICLE = "article";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() == null) {
            Toast.makeText(this, "Unable to load feed", Toast.LENGTH_SHORT).show();
            finish();
        }

        Article article = Parcels.unwrap(getIntent().getParcelableExtra(ARTICLE));
        ;

        if (article == null) {
            Toast.makeText(this, "Unable to load feed", Toast.LENGTH_SHORT).show();
            finish();
        }

        setContentView(R.layout.activity_feed_detail);

        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.drawer_image)
                .showImageOnFail(R.drawable.drawer_image)
                .showImageOnLoading(R.drawable.drawer_image).build();
        imageLoader.displayImage(article.getImageUrl(), (AppCompatImageView) findViewById(R.id.image), options);

        ((AppCompatTextView) findViewById(R.id.title)).setText(article.getTitle());


        ((AppCompatTextView) findViewById(R.id.dateTime)).setText(Util.formatDate(article.getDateTime()));

        ((AppCompatTextView) findViewById(R.id.ingress)).setText(article.getIngress());

        if (!article.getItems().isEmpty()) {
            ViewGroup layoutContent = (ViewGroup) findViewById(R.id.layoutContent);

            for (Item item : article.getItems()) {
                View view = getLayoutInflater().inflate(R.layout.layout_content, null);
                if (item.getType().equalsIgnoreCase("text")) {
                    TextView contentTitle = (TextView) view.findViewById(R.id.contentTitle);
                    contentTitle.setText(item.getSubject());

                    TextView content = (TextView) view.findViewById(R.id.content);
                    content.setText(item.getDescription());
                }
                layoutContent.addView(view);
            }
        }

        ((ImageView) findViewById(R.id.arrowBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
