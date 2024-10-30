package com.example.pe_prm;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExpenseListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExpenseAdapter adapter;
    private ExpenseDatabase database;
    private ExecutorService executorService;
    private List<Expense> expenseList; // Danh sách chi tiêu gốc

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo adapter với context
        adapter = new ExpenseAdapter(this); // Cung cấp context tại đây
        recyclerView.setAdapter(adapter);

        FloatingActionButton addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExpenseListActivity.this, AddExpenseActivity.class);
                startActivity(intent);
            }
        });

        database = ExpenseDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        loadExpenses(); // Tải danh sách chi tiêu
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExpenses();
    }

    private void loadExpenses() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final List<Expense> expenses = database.expenseDao().getAllExpenses();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setExpenses(expenses);
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu từ main_menu.xml
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Xử lý sự kiện menu
        int id = item.getItemId();
        if (id == R.id.menu_refresh) {
            loadExpenses();
            Toast.makeText(this, "List refreshed", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_settings) {
            // Xử lý cho Settings (nếu có)
            Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void searchExpenses(String query) {
        List<Expense> filteredList = new ArrayList<>();
        for (Expense expense : expenseList) {
            if (expense.getDescription().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(expense);
            }
        }
        adapter.setExpenses(filteredList); // Cập nhật danh sách trong adapter
    }
}
