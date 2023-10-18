package com.naqib.system_pengurusan_wang;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText descriptionEditText;
    private EditText amountEditText;
    private CheckBox incomeCheckBox;
    private ListView transactionList;
    private ArrayList<String> transactions;
    private ArrayAdapter<String> transactionAdapter;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        descriptionEditText = findViewById(R.id.descriptionEditText);
        amountEditText = findViewById(R.id.amountEditText);
        incomeCheckBox = findViewById(R.id.incomeCheckBox);
        transactionList = findViewById(R.id.transactionList);

        transactions = new ArrayList<>();
        transactionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, transactions);
        transactionList.setAdapter(transactionAdapter);

        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "finance-db").allowMainThreadQueries().build();
        loadTransactionsFromDatabase();
    }

    public void addTransaction(View view) {
        String description = descriptionEditText.getText().toString();
        String amountText = amountEditText.getText().toString();

        if (!description.isEmpty() && !amountText.isEmpty()) {
            double amount = Double.parseDouble(amountText);
            boolean isIncome = incomeCheckBox.isChecked();

            Transaction transaction = new Transaction(description, amount, isIncome);
            database.transactionDao().insert(transaction);

            transactions.add(transaction.getDescription() + ": RM" + transaction.getAmount() + " (" + (isIncome ? "Income" : "Expense") + ")");
            transactionAdapter.notifyDataSetChanged();

            descriptionEditText.setText("");
            amountEditText.setText("");
            incomeCheckBox.setChecked(false);
        }
    }

    private void loadTransactionsFromDatabase() {
        List<Transaction> transactionList = database.transactionDao().getAllTransactions();
        for (Transaction transaction : transactionList) {
            transactions.add(transaction.getDescription() + ": RM" + transaction.getAmount() + " (" + (transaction.isIncome() ? "Income" : "Expense") + ")");
        }
        transactionAdapter.notifyDataSetChanged();
    }
}