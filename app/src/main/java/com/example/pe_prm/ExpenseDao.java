package com.example.pe_prm;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExpenseDao {

    @Insert
    void insert(Expense expense);

    @Update
    void update(Expense expense);

    @Delete
    void delete(Expense expense);



    @Query("SELECT * FROM expenses ORDER BY date DESC")
    List<Expense> getAllExpenses();
    @Query("SELECT * FROM expenses WHERE id = :id LIMIT 1") // Thêm phương thức này
    Expense getExpenseById(int id);
    @Query("DELETE FROM expenses WHERE id = :id")
    void deleteExpense(int id); // Thêm phương thức xóa
}
