package com.hoanmy.football.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ctrlplusz.anytextview.AnyTextView;
import com.hoanmy.football.R;
import com.hoanmy.football.commons.ItemType;
import com.hoanmy.football.models.Config;
import com.hoanmy.football.models.RootDataListMatch;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;

public class YesterdayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    static List<RootDataListMatch> rootMatchInfos;
    private static Activity mActivity;
    private int numberItemInRow = 1;
    private int numberRowToShowAds = 4;
    private static Config config;

    private LinearLayoutManager lln;
    private CItemMatchLeagueAdapter cItemMatchLeagueAdapter;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public YesterdayAdapter(Activity activity, List<RootDataListMatch> infos) {
        mActivity = activity;
        this.rootMatchInfos = infos;
        config = Paper.book().read("ads");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ItemType.HEADER.ordinal())
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item_name_league, parent, false));
        else
            return new NativeAdViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nativeads_fragment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder)
            ((HeaderViewHolder) holder).showHeaderView(this, "Premier League", position);
        else
            ((NativeAdViewHolder) holder).showViews(mActivity);

        lln = new LinearLayoutManager(
                ((HeaderViewHolder) holder)
                        .ChildRecyclerView
                        .getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        cItemMatchLeagueAdapter = new CItemMatchLeagueAdapter(mActivity, rootMatchInfos.get(position).getMatches());
        ((HeaderViewHolder) holder).ChildRecyclerView.setLayoutManager(lln);
        ((HeaderViewHolder) holder).ChildRecyclerView.setAdapter(cItemMatchLeagueAdapter);
        ((HeaderViewHolder) holder).ChildRecyclerView.setRecycledViewPool(viewPool);
    }


    @Override
    public int getItemCount() {
        return rootMatchInfos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return ItemType.HEADER.ordinal();
    }


    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.logo_league)
        AppCompatImageView logoLeague;
        @BindView(R.id.name_league)
        AnyTextView nameLeague;
        @BindView(R.id.name_blv)
        AnyTextView nameBLV;
        @BindView(R.id.list_match)
        RecyclerView ChildRecyclerView;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void showHeaderView(final RecyclerView.Adapter adapter, String s, final int pos) {
            int _position = pos;

            Glide.with(mActivity).load(rootMatchInfos.get(_position).getLeague().getLogo()).diskCacheStrategy(DiskCacheStrategy.ALL).into(logoLeague);
            nameLeague.setText(rootMatchInfos.get(_position).getLeague().name);
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
