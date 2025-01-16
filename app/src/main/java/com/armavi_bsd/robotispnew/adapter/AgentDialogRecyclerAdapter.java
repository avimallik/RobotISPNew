package com.armavi_bsd.robotispnew.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.armavi_bsd.robotispnew.R;
import com.armavi_bsd.robotispnew.model.AgentDialogModel;

import java.util.List;

public class AgentDialogRecyclerAdapter extends RecyclerView.Adapter<AgentDialogRecyclerAdapter.AgentViewHolder> {

    private List<AgentDialogModel> agentModelList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AgentDialogModel agentDialogModel);
    }

    public AgentDialogRecyclerAdapter(List<AgentDialogModel> agentModelList, OnItemClickListener listener) {
        this.agentModelList = agentModelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AgentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_customer, parent, false);
        return new AgentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgentViewHolder holder, int position) {
        AgentDialogModel agentDialogModel = agentModelList.get(position);
        holder.agNameTextView.setText(agentDialogModel.getAgName().toString().trim());
        holder.agIdTextView.setText(agentDialogModel.getAgId().toString().trim());
        holder.agMobileTextView.setText("Mobile: "+agentDialogModel.getAgMobileNumber().toString().trim());
        holder.agIPTextView.setText("IP: "+agentDialogModel.getAgIp().toString().trim());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(agentDialogModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return agentModelList.size();
    }

    public void updateList(List<AgentDialogModel> filteredList) {
        this.agentModelList = filteredList;
        notifyDataSetChanged();
    }

    public static class AgentViewHolder extends RecyclerView.ViewHolder {
        TextView agNameTextView,
                agIdTextView,
                agIPTextView,
                agMobileTextView;

        public AgentViewHolder(@NonNull View itemView) {
            super(itemView);
            agNameTextView = itemView.findViewById(R.id.agNameTextView);
            agIdTextView = itemView.findViewById(R.id.agIdTextView);
            agIPTextView = itemView.findViewById(R.id.agIPTextView);
            agMobileTextView = itemView.findViewById(R.id.agMobileTextView);
        }
    }
}
