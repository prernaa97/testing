 public void recordTransaction(String accountId, double amount, String transactionType) throws SQLException {
        if (!isAccountExists(accountId)) {
            System.out.println("Transaction Failed: Account does not exist.");
            return;
        }

        String sql = "";
        if (transactionType.equalsIgnoreCase("Credit")) {
            sql = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
        } else if (transactionType.equalsIgnoreCase("Debit")) {
            //sql = "UPDATE account SET balance = balance - ? WHERE account_id = ? AND balance >= ?";
        sql = "UPDATE account SET balance = balance - ? WHERE account_id = ? AND balance >= ?";

        } else {
            System.out.println("Invalid transaction type!");
            return;
        }

        Connection con = getConnection();
        try {
            con.setAutoCommit(false); // Disable auto-commit

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setDouble(1, amount);
            pstmt.setString(2, accountId);
             pstmt.setDouble(3, amount);
            if (transactionType.equalsIgnoreCase("Debit")) {
               sql = "UPDATE account SET balance = balance - ? WHERE account_id = ? AND balance >= ?";
}
            }
            PreparedStatement pstmt = con.prepareStatement(sql);
pstmt.setDouble(1, amount);
pstmt.setString(2, accountId);
if (transactionType.equalsIgnoreCase("Debit")) {
    pstmt.setDouble(3, amount);  // Ensuring balance is sufficient
}
            int updatedRows = pstmt.executeUpdate();

            if (updatedRows > 0) {
                System.out.println("Transaction successful.");

                String transactionSql = "INSERT INTO transaction (account_id, amount, transaction_type, status) VALUES (?, ?, ?, ?)";
                PreparedStatement transPstmt = con.prepareStatement(transactionSql);
                transPstmt.setString(1, accountId);
                transPstmt.setDouble(2, amount);
                transPstmt.setString(3, transactionType);
                transPstmt.setString(4, "Active");

                transPstmt.executeUpdate();
                con.commit();  // Commit transaction after successful execution
            } else {
                System.out.println("Transaction failed: Insufficient funds");
                con.rollback();  // Rollback transaction in case of failure
            }
        } catch (SQLException e) {
            con.rollback();  // Rollback in case of exception
            e.printStackTrace();
        } finally {
            con.setAutoCommit(true); // Reset auto-commit
            con.close();
        }
    }

    private Connection getConnection() throws SQLException {
        // Dummy method, implement your actual DB connection logic
        return null;
    }

    private boolean isAccountExists(String accountId) {
        // Dummy method, implement actual account existence check
        return true;
    }
}