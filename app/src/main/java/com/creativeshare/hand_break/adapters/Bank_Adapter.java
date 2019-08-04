package com.creativeshare.hand_break.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.models.App_Commission;

import java.util.List;

public class Bank_Adapter  extends RecyclerView.Adapter<Bank_Adapter.MyHolder> {

    private List<App_Commission.Data> bankDataModelList;
    private Context context;

    public Bank_Adapter(List<App_Commission.Data> bankDataModelList, Context context) {
        this.bankDataModelList = bankDataModelList;
        this.context = context;


    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bank_row, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        App_Commission.Data bankModel = bankDataModelList.get(position);
        holder.BindData(bankModel);


    }

    @Override
    public int getItemCount() {
        return bankDataModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_bank_name,tv_account_iban,tv_account_number,tv_account_name;


        public MyHolder(View itemView) {
            super(itemView);

            tv_bank_name = itemView.findViewById(R.id.tv_bank_name);
            tv_account_iban = itemView.findViewById(R.id.tv_account_iban);
            tv_account_number = itemView.findViewById(R.id.tv_account_number);
            tv_account_name = itemView.findViewById(R.id.tv_account_name);



        }

        public void BindData(App_Commission.Data bankModel) {

            tv_bank_name.setText(bankModel.getAccount_bank_name());
            tv_account_iban.setText(bankModel.getAccount_IBAN());
            tv_account_number.setText(bankModel.getAccount_number());
            tv_account_name.setText(bankModel.getAccount_name());

        }
    }
}