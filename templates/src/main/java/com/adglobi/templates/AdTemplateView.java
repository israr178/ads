package com.adglobi.templates;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adglobi.ads.UnifiedAd;

public class AdTemplateView extends FrameLayout {
    private int templateType;
    ImageView ad_main_image;
    ImageButton ad_close,ad_privacy;
    RelativeLayout adContainer;
    Context mContext;
    UnifiedAd mUnifiedAd;
    AdInteractionListener adInteractionListener;

    public AdTemplateView(@NonNull Context context) {
        super(context);
    }

    public AdTemplateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
        mContext = context;
    }

    public AdTemplateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
        mContext = context;
    }

    public AdTemplateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
        mContext = context;
    }

    private void initView(Context context, AttributeSet attributeSet) {
        mContext = context;
        TypedArray attributes =
                context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.AdTemplateView, 0, 0);

        try {
            templateType =
                    attributes.getResourceId(
                            R.styleable.AdTemplateView_adglobi_template_type, R.layout.ad_template_view);
        } finally {
            attributes.recycle();
        }
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(templateType, this);
        ad_close = findViewById(R.id.ad_close);
        ad_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adInteractionListener !=null) {
                    adInteractionListener.onAdCloseClicked();
                }
            }
        });


        ad_privacy = findViewById(R.id.ad_privacy);
        ad_privacy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.adglobi.com/Index/privacy"));
                mContext.startActivity(browserIntent);

            }
        });
        ad_main_image = findViewById(R.id.ad_main_image);
        adContainer = findViewById(R.id.adContainer);
        ad_main_image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("israr", "image clicked");
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUnifiedAd.getAdInfo().getClick_url()));
                mContext.startActivity(browserIntent);
            }
        });
    }
    public void setAdInteractionListener(AdInteractionListener adInteractionListener){
        this.adInteractionListener = adInteractionListener;
    }
    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
    }
    public void setNativeAd(UnifiedAd adGlobiUnifiedAd){
        mUnifiedAd = adGlobiUnifiedAd;
        this.ad_main_image.setImageBitmap(adGlobiUnifiedAd.getAdImage());

    }

}
