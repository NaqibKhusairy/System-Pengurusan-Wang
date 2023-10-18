package com.naqib.system_pengurusan_wang;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText descriptionEditText;
    private EditText amountEditText;
    private CheckBox incomeCheckBox;
    private ListView transactionList;
    private TextView balanceTextView;

    private ArrayList<String> transactions;
    private CustomListAdapter transactionAdapter;
    private double balance = 0.0;

    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        descriptionEditText = findViewById(R.id.descriptionEditText);
        amountEditText = findViewById(R.id.amountEditText);
        incomeCheckBox = findViewById(R.id.incomeCheckBox);
        transactionList = findViewById(R.id.transactionList);
        balanceTextView = findViewById(R.id.balanceTextView);

        transactions = new ArrayList<>();
        transactionAdapter = new CustomListAdapter(this, R.layout.list_item, transactions);
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

            // Format the amount with two decimal places using DecimalFormat
            DecimalFormat decimalFormat = new DecimalFormat("#0.00");
            String formattedAmount = decimalFormat.format(transaction.getAmount());

            transactions.add(transaction.getDescription() + ": RM" + formattedAmount + " (" + (isIncome ? "Income" : "Expense") + ")");
            transactionAdapter.notifyDataSetChanged();

            descriptionEditText.setText("");
            amountEditText.setText("");
            incomeCheckBox.setChecked(false);

            // Update the balance
            if (isIncome) {
                balance += amount;
            } else {
                balance -= amount;
            }

            updateBalanceView();
        }
    }

    private void loadTransactionsFromDatabase() {
        List<Transaction> transactionList = database.transactionDao().getAllTransactions();
        balance = 0.0;

        for (Transaction transaction : transactionList) {
            double transactionAmount = transaction.isIncome() ? transaction.getAmount() : -transaction.getAmount();
            balance += transactionAmount;

            transactions.add(transaction.getDescription() + ": RM" + String.format("%.2f", transaction.getAmount()) + " (" + (transaction.isIncome() ? "Income" : "Expense") + ")");
        }

        transactionAdapter.notifyDataSetChanged();
        updateBalanceView();
    }

    private void updateBalanceView() {
        balanceTextView.setText("Balance: RM" + String.format("%.2f", balance));
    }
}