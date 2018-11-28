package com.example.zlff.casefirst;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class RuleAdapter extends RecyclerView.Adapter<RuleAdapter.ViewHolder>{

    interface OnItemClickHandler {
        // 提供onItemClick方法作為點擊事件，括號內為接受的參數
        void onItemClick(String text);
        // 提供onItemRemove做為移除項目的事件
    }

    private ArrayList<String> names;

    private OnItemClickHandler mClickHandler;

    RuleAdapter(ArrayList<String> data, OnItemClickHandler clickHandler) {
        names = data;
        mClickHandler = clickHandler;
    }

    public RuleAdapter(ArrayList<String> names) {
        this.names = names;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rule_list_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textViewName.setText(names.get(position));

    }

    @Override
    public int getItemCount() {
        return names.size();
    }
    public void filterList(ArrayList<String> filterdNames) {
        this.names = filterdNames;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;


        ViewHolder(final View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String msg = names.get(getAdapterPosition());
                    // 4. 呼叫interface的method
                    mClickHandler.onItemClick(msg);
                }
            });
        }
    }

}
