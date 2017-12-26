package com.tomoapp.tomowallet.ui.home.cell;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;
import com.jaychang.srv.Updatable;
import com.tomoapp.tomowallet.R;
import com.tomoapp.tomowallet.helper.LogUtil;
import com.tomoapp.tomowallet.model.userInfo.pojo.Log;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by macbook on 12/26/17.
 */

public class LogCell extends SimpleCell<Log, LogCell.LogCellViewHolder> implements Updatable<Log> {

    private Context context;

    public LogCell(@NonNull Log item, Context context) {
        super(item);
        this.context = context;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.cell_log;
    }

    @NonNull
    @Override
    protected LogCellViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, @NonNull View view) {
        return new LogCellViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull LogCellViewHolder logCellViewHolder, int i, @NonNull Context context, Object o) {
        logCellViewHolder.setContent(getItem());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Log log) {
        return false;
    }

    @Override
    public Object getChangePayload(@NonNull Log log) {
        return null;
    }

    public class LogCellViewHolder extends SimpleViewHolder {
        @BindView(R.id.txt_transaction_type)
        TextView txtTransactionType;
        @BindView(R.id.txt_transaction_date)
        TextView txtTransactionDate;
        @BindView(R.id.txt_message)
        TextView txtMessage;
        @BindView(R.id.txt_tmc_side_chain)
        TextView txtTmcSideChain;
        @BindView(R.id.txt_tmc_main_chain)
        TextView txtTmcMainChain;
        @BindView(R.id.txt_tmc_total)
        TextView txtTmcTotal;

        public LogCellViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private Log log;
        private void setContent(Log log){
            try {
                this.log = log;
                LogUtil.d("setContent: " + log);
                txtTransactionDate.setText(log.getTime());
                txtTransactionType.setText(log.getType());
                switch (log.getType()){
                    case "cashOut":
                        txtTransactionType.setTextColor(context.getResources().getColor(R.color.color_blue));
                        break;
                    case "cashIn":
                        txtTransactionType.setTextColor(context.getResources().getColor(R.color.color_50));
                        break;
                    case "reward":
                        txtTransactionType.setTextColor(context.getResources().getColor(R.color.color_green));
                        break;
                    default:
                        txtTransactionType.setTextColor(context.getResources().getColor(R.color.color_3));
                        break;
                }
                txtMessage.setText(log.getMsg() == null ? "NULL" : log.getMsg());
                txtTmcMainChain.setText(String.format(Locale.US,"%.2f", log.getTmcMainchain()));
                txtTmcSideChain.setText(String.format(Locale.US,"%.2f", log.getTmcSidechain()));
                txtTmcTotal.setText(String.format(Locale.US,"%.2f", (log.getTmcSidechain() + log.getTmcMainchain())));
            } catch (Exception e) {
                LogUtil.e(e);
            }
        }
    }
}
