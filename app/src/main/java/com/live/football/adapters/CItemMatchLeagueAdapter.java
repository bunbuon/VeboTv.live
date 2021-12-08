package com.hoanmy.football.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ctrlplusz.anytextview.AnyTextView;
import com.google.android.material.card.MaterialCardView;
import com.hoanmy.football.R;
import com.hoanmy.football.commons.Action;
import com.hoanmy.football.models.ListMatchInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CItemMatchLeagueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public Activity context;
    public List<ListMatchInfo> listMatchInfos;

    public CItemMatchLeagueAdapter(Activity context, List<ListMatchInfo> matchInfos) {
        this.context = context;
        this.listMatchInfos = matchInfos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_league, parent, false);
        return new ChildViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((ChildViewHolder) holder).showViews(listMatchInfos, position);

    }

    @Override
    public int getItemCount() {
        return listMatchInfos.size();
    }

    class ChildViewHolder extends RecyclerView.ViewHolder {

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

        ChildViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void showViews(List<ListMatchInfo> matchInfos, int position) {
            Glide.with(context).load(matchInfos.get(position).getHome().logo).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgLogoHome);
            Glide.with(context).load(matchInfos.get(position).getAway().logo).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgLogoAway);
            txtNameHome.setText(matchInfos.get(position).getHome().name);
            txtNameAway.setText(matchInfos.get(position).getAway().name);
            txtStatus.setText(matchInfos.get(position).getMatch_status());
            txtScoreAway.setText(matchInfos.get(position).getScores().away + "");
            txtScoreHome.setText(matchInfos.get(position).getScores().home + "");
            txtTimeMatch.setText(matchInfos.get(position).getParse_data().getTime());
            materialCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(Action.MAIN_AD);
//                    Paper.book().write("id_stream", rootMatchInfo.getId());
                }
            });
        }
    }
}
