package com.hoanmy.football.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.hoanmy.football.R;
import com.hoanmy.football.commons.Action;
import com.hoanmy.football.commons.ItemClickListener;
import com.hoanmy.football.models.LinkStream;
import com.hoanmy.football.models.PlayUrls;

import org.greenrobot.eventbus.EventBus;

import java.nio.file.Path;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;

public class ServerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<LinkStream> playUrls;
    static ItemClickListener itemClickListener;
    static Activity activity;

    interface Listener {
        void onItemClick(int position);
    }

    public ServerAdapter(Activity _activity, List<LinkStream> _playUrls, ItemClickListener _itemClickListener) {
        this.activity = _activity;
        this.playUrls = _playUrls;
        this.itemClickListener = _itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_server, parent, false);
        return new ServerAdapter.ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ServerAdapter.ViewHolder) holder).showViews(this, playUrls, position);
    }

    @Override
    public int getItemCount() {
        return playUrls.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.btn_server)
//        AppCompatButton btnServer;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void showViews(final RecyclerView.Adapter adapter, final List<LinkStream> _playUrls, final int position) {
//            btnServer.setText(_playUrls.get(position).getName());
//            if (_playUrls.get(position).isActive)
//                btnServer.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.border_radius_button_active));
//            else   btnServer.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.border_radius_button));
//            btnServer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    EventBus.getDefault().post(Action.CHANGE_SERVER);
//                    itemClickListener.onPositionClicked(position);
//                }
//            });
        }
    }
}
