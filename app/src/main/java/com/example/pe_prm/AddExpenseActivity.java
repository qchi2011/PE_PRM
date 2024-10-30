package com.example.pe_prm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {

    private TextView textViewDate;
    private EditText editTextDescription, editTextType, editTextProductOrService, editTextSupplier;
    private Button buttonSave;
    private ExpenseDatabase database;
    private int expenseId = -1; // ID của chi tiêu nếu đang chỉnh sửa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        textViewDate = findViewById(R.id.text_view_date);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextType = findViewById(R.id.edit_text_type);
        editTextProductOrService = findViewById(R.id.edit_text_product_or_service);
        editTextSupplier = findViewById(R.id.edit_text_supplier);
        buttonSave = findViewById(R.id.button_save);

        // Khởi tạo database
        database = ExpenseDatabase.getInstance(this);

        // Nhận expense_id từ Intent (nếu có)
        expenseId = getIntent().getIntExtra("expense_id", -1);
        if (expenseId != -1) {
            loadExpenseDetails(expenseId); // Tải thông tin chi tiêu để chỉnh sửa
        } else {
            // Lấy ngày hiện tại
            String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());
            textViewDate.setText(currentDate);
        }

        buttonSave.setOnClickListener(view -> saveExpense());
    }

    private void loadExpenseDetails(int id) {
        new Thread(() -> {
            Expense expense = database.expenseDao().getExpenseById(id);
            runOnUiThread(() -> {
                if (expense != null) {
                    textViewDate.setText(expense.getDate());
                    editTextDescription.setText(expense.getDescription());
                    editTextType.setText(expense.getType());
                    editTextProductOrService.setText(expense.getProductOrService());
                    editTextSupplier.setText(expense.getSupplier());
                } else {
                    Toast.makeText(this, "Expense not found", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void saveExpense() {
        String description = editTextDescription.getText().toString();
        String type = editTextType.getText().toString();
        String productOrService = editTextProductOrService.getText().toString();
        String supplier = editTextSupplier.getText().toString();
        String date = textViewDate.getText().toString();

        // Kiểm tra các trường
        if (description.isEmpty() || type.isEmpty() || productOrService.isEmpty() || supplier.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Expense expense = new Expense();
        expense.setDate(date);
        expense.setDescription(description);
        expense.setType(type);
        expense.setProductOrService(productOrService);
        expense.setSupplier(supplier);

        if (expenseId != -1) {
            expense.setId(expenseId); // Cập nhật ID để chỉnh sửa
            new Thread(() -> {
                database.expenseDao().update(expense); // Cần thêm phương thức cập nhật
                runOnUiThread(() -> {
                    Toast.makeText(AddExpenseActivity.this, "Expense updated", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        } else {
            new Thread(() -> {
                database.expenseDao().insert(expense);
                runOnUiThread(() -> {
                    Toast.makeText(AddExpenseActivity.this, "Expense saved", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        }
    }
}
