package com.example.pe_prm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ExpenseDetailActivity extends AppCompatActivity {

    private TextView textViewDate, textViewDescription, textViewType, textViewProductOrService, textViewSupplier;
    private ImageButton buttonEdit, buttonDelete;
    private ExpenseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Hiển thị nút back
        getSupportActionBar().setTitle("Expense Details");

        textViewDate = findViewById(R.id.text_view_date);
        textViewDescription = findViewById(R.id.text_view_description);
        textViewType = findViewById(R.id.text_view_type);
        textViewProductOrService = findViewById(R.id.text_view_product_or_service);
        textViewSupplier = findViewById(R.id.text_view_supplier);
        buttonDelete = findViewById(R.id.button_delete);
        buttonEdit = findViewById(R.id.button_edit);

        // Khởi tạo database
        database = ExpenseDatabase.getInstance(this);

        // Nhận expense_id từ Intent
        int expenseId = getIntent().getIntExtra("expense_id", -1);
        if (expenseId != -1) {
            loadExpenseDetails(expenseId); // Tải chi tiết chi tiêu
        } else {
            Toast.makeText(this, "Invalid expense ID", Toast.LENGTH_SHORT).show();
            finish(); // Quay lại nếu không hợp lệ
        }

        buttonEdit.setOnClickListener(view -> {
            Intent intent = new Intent(ExpenseDetailActivity.this, AddExpenseActivity.class);
            intent.putExtra("expense_id", expenseId); // Chuyển ID để chỉnh sửa
            startActivity(intent);
        });

        buttonDelete.setOnClickListener(view -> {
            deleteExpense(expenseId);
        });
        toolbar.setNavigationOnClickListener(v -> finish()); // Quay lại danh sách khi nhấn nút back

    }

    private void loadExpenseDetails(int id) {
        // Lấy chi tiết từ cơ sở dữ liệu bằng ID
        new Thread(() -> {
            Expense expense = database.expenseDao().getExpenseById(id);
            runOnUiThread(() -> {
                if (expense != null) {
                    textViewDate.setText(expense.getDate());
                    textViewDescription.setText(expense.getDescription());
                    textViewType.setText(expense.getType());
                    textViewProductOrService.setText(expense.getProductOrService());
                    textViewSupplier.setText(expense.getSupplier());
                } else {
                    Toast.makeText(this, "Expense not found", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void deleteExpense(int id) {
        new Thread(() -> {
            database.expenseDao().deleteExpense(id); // Phương thức xóa trong ExpenseDao
            runOnUiThread(() -> {
                Toast.makeText(this, "Expense deleted", Toast.LENGTH_SHORT).show();
                finish(); // Quay lại danh sách
            });
        }).start();
    }
}
