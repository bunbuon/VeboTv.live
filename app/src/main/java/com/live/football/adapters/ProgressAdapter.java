package com.hoanmy.football.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.ctrlplusz.anytextview.AnyTextView;
import com.hoanmy.football.R;
import com.hoanmy.football.commons.ItemType;
import com.hoanmy.football.models.ProgressLiveMatch;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    static List<ProgressLiveMatch> progressLiveMatches;

    public ProgressAdapter(Context _context, List<ProgressLiveMatch> _progressLiveMatches) {
        this.context = _context;
        this.progressLiveMatches = _progressLiveMatches;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ItemType.HEADER.ordinal())
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.header_progress_match, parent, false));
        else
            return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress_match, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder)
            ((HeaderViewHolder) holder).showHeaderView(this, "Premier League", position);
        else
            ((ItemViewHolder) holder).showView(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return ItemType.HEADER.ordinal();
        else return ItemType.NORMAL.ordinal();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.logo_home)
        AppCompatImageView logoHome;
        @BindView(R.id.logo_away)
        AppCompatImageView logoAway;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void showHeaderView(final RecyclerView.Adapter adapter, String s, final int pos) {
            int _position = pos;

        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name_progress_home)
        AnyTextView txtProgressHome;
        @BindView(R.id.thumb_progress_home)
        AppCompatImageView thumbHome;

        @BindView(R.id.name_progress_away)
        AnyTextView txtProgressAway;
        @BindView(R.id.thumb_progress_away)
        AppCompatImageView thumbAway;
        @BindView(R.id.tv_score_match)
        AnyTextView txtScore;
        @BindView(R.id.progress_away)
        RelativeLayout layoutAway;
        @BindView(R.id.progress_home)
        RelativeLayout layoutHome;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void showView(final int pos) {
            int _position = pos - 1;
            ProgressLiveMatch progressLiveMatch = progressLiveMatches.get(_position);
            switch (progressLiveMatch.type) {
                case "period":
                    layoutAway.setVisibility(View.INVISIBLE);
                    layoutHome.setVisibility(View.INVISIBLE);
                    txtScore.setText(progressLiveMatch.time + "'" + " + " + progressLiveMatch.added_time);
                    break;
                case "in_game_penalty":
                case "var":
                    txtScore.setText(progressLiveMatch.time + "'");
                    if (progressLiveMatch.is_home) {
                        layoutAway.setVisibility(View.INVISIBLE);
                        txtProgressHome.setText(progressLiveMatch.getPlayer().name);
                    } else {
                        layoutHome.setVisibility(View.INVISIBLE);
                        txtProgressAway.setText(progressLiveMatch.getPlayer().name);
                    }
                    break;
                case "sub":
                    txtScore.setText(progressLiveMatch.time + "'");
                    if (progressLiveMatch.is_home) {
                        layoutAway.setVisibility(View.INVISIBLE);
                        txtProgressHome.setText(progressLiveMatch.getPlayer_in().name + " in " + progressLiveMatch.getPlayer_out().name + " out");
                    } else {
                        layoutHome.setVisibility(View.INVISIBLE);
                        txtProgressAway.setText(progressLiveMatch.getPlayer_in().name + " in " + progressLiveMatch.getPlayer_out().name + " out");
                    }
                    break;
                case "goal":
                    txtScore.setText(progressLiveMatch.time + "'");
                    if (progressLiveMatch.is_home) {
                        layoutAway.setVisibility(View.INVISIBLE);
                        if (progressLiveMatch.getAssist() != null)
                            txtProgressHome.setText(progressLiveMatch.getPlayer().name + " ghi bàn " + progressLiveMatch.getAssist().name + " kiến tạo");
                        else
                            txtProgressHome.setText(progressLiveMatch.getPlayer().name + " ghi bàn ");
                    } else {
                        layoutHome.setVisibility(View.INVISIBLE);
                        if (progressLiveMatch.getAssist() != null)
                            txtProgressAway.setText(progressLiveMatch.getPlayer().name + " ghi bàn " + progressLiveMatch.getAssist().name + " kiến tạo");
                        else
                            txtProgressAway.setText(progressLiveMatch.getPlayer().name + " ghi bàn ");
                    }
                    break;
                default:
                    break;
            }


        }
    }

    @Override
    public int getItemCount() {
        return progressLiveMatches.size();
    }
}
