

public class Account {

	private int accountId;
	private int userId;
	private Double balance;
	
	public Account() {
		
	}
	
	public Account (int accountId, int userId, double balance) {
		this.accountId = accountId;
		this.userId = userId;
		this.balance = balance;
	}
	
    @Override
    public String toString() {
        return "\n--------------------------------------------" +
                "\n Account Details" +
                "\n--------------------------------------------" +
                "\n Account ID: " + accountId +
                "\n User ID:'" + userId + '\'' +
                "\n Balance: $" + balance;
    }

	public int getAccountId() {
		return accountId;
	}

	public int getUserId() {
		return userId;
	}

	public double getBalance() {
		return balance;
	}
	
}
