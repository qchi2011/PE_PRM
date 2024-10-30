package com.example.pe_prm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenses;
    private Context context; // Đảm bảo context được khai báo

    public ExpenseAdapter(Context context) {
        this.context = context; // Khởi tạo context
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenses.get(position);
        holder.textViewDate.setText(expense.getDate());
        holder.textViewDescription.setText(expense.getDescription());
        holder.textViewType.setText(expense.getType());
        holder.textViewProductOrService.setText(expense.getProductOrService());
        holder.textViewSupplier.setText(expense.getSupplier());

        // Thiết lập sự kiện click để chuyển đến ExpenseDetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ExpenseDetailActivity.class);
            intent.putExtra("expense_id", expense.getId()); // Đảm bảo getId() trả về giá trị hợp lệ
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return expenses != null ? expenses.size() : 0;
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate, textViewDescription, textViewType, textViewProductOrService, textViewSupplier;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.text_view_date);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewType = itemView.findViewById(R.id.text_view_type);
            textViewProductOrService = itemView.findViewById(R.id.text_view_product_or_service);
            textViewSupplier = itemView.findViewById(R.id.text_view_supplier);
        }
    }
}
