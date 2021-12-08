package com.hoanmy.football.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ctrlplusz.anytextview.AnyTextView;
import com.google.android.material.card.MaterialCardView;
import com.hoanmy.football.R;
import com.hoanmy.football.commons.ItemType;
import com.hoanmy.football.models.Config;
import com.hoanmy.football.models.ListMatchInfo;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;

public class EndHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<ListMatchInfo> rootMatchInfos;
    private static Activity mActivity;
    private int numberItemInRow = 1;
    private int numberRowToShowAds = 4;
    private static Config config;

    public EndHomeAdapter(Activity activity, List<ListMatchInfo> infos) {
        mActivity = activity;
        this.rootMatchInfos = infos;
        config = Paper.book().read("ads");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ItemType.NORMAL.ordinal()) {
            View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_league, parent, false);
            return new ViewHolder(root);
        } else if (viewType == ItemType.HEADER.ordinal())
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item_name_league, parent, false));
        else
            return new NativeAdViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nativeads_fragment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).showViews(this, rootMatchInfos, position);
        } else if (holder instanceof HeaderViewHolder)
            ((HeaderViewHolder) holder).showHeaderView(this, "Premier League", position);
        else
            ((NativeAdViewHolder) holder).showViews(mActivity);
    }

    @Override
    public int getItemCount() {
        return rootMatchInfos.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return ItemType.HEADER.ordinal();
        return ((position + 1) % (numberItemInRow * numberRowToShowAds + 1) == 0 ? ItemType.ADS.ordinal() : ItemType.NORMAL.ordinal());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.logo_home)
        AppCompatImageView imgLogoHome;
        @BindView(R.id.logo_away)
        AppCompatImageView imgLogoAway;
        @BindView(R.id.tv_name_home)
        AnyTextView txtNameHome;
        @BindView(R.id.tv_name_away)
        AnyTextView txtNameAway;
        @BindView(R.id.tv_time_match)
        AnyTextView txtTimeMatch;
        @BindView(R.id.tv_status_match)
        AnyTextView txtStatus;
        @BindView(R.id.tv_score_home)
        AnyTextView txtScoreHome;
        @BindView(R.id.tv_score_away)
        AnyTextView txtScoreAway;
        @BindView(R.id.card_view_play)
        MaterialCardView materialCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void showViews(final RecyclerView.Adapter adapter, final List<ListMatchInfo> rootMatchInfos, final int position) {

//            RootMatchInfo rootMatchInfo = rootMatchInfos.get(position - 1);
//            Glide.with(mActivity).load(rootMatchInfo.getHome().logo).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgLogoHome);
//            Glide.with(mActivity).load(rootMatchInfo.getAway().logo).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgLogoAway);
//            txtNameHome.setText(rootMatchInfo.getHome().name);
//            txtNameAway.setText(rootMatchInfo.getAway().name);
//            txtTimeMatch.setText(rootMatchInfo.getFulltime());
//            materialCardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    EventBus.getDefault().post(Action.MAIN_AD);
//                    Paper.book().write("id_stream", rootMatchInfo.getId());
//                }
//            });

        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void showHeaderView(final RecyclerView.Adapter adapter, String s, final int position) {

        }
    }

    static class NativeAdViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_ads)
        AppCompatImageView imageView;

        public NativeAdViewHolder(View inflate) {
            super(inflate);
            ButterKnife.bind(this, inflate);
        }

        public void showViews(Activity activity) {
            int positionAd = new Random().nextInt(config.getAds().getBanner().size());
            String linkDirect = config.getAds().getBanner().get(positionAd).getClick_url();
            Glide.with(mActivity).load(config.getAds().getBanner().get(positionAd).getUrl()).into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkDirect));
                    mActivity.startActivity(browserIntent);
                }
            });
        }
    }
}
