package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

	private int transferId;
	private int transferTypeId;
	private int transferStatusId;
	private int accountFrom;
	private int accountTo;
	private BigDecimal transferAmount;
	
	public Transfer() {
		
	}

	public Transfer(int transferId, int transferTypeId, int transferStatusId, int accountFrom, int accountTo, BigDecimal transferAmount) {
		this.transferId = transferId;
		this.transferTypeId = transferTypeId;
		this.transferStatusId = transferStatusId;
		this.accountFrom = accountFrom;
		this.accountTo = accountTo;
		this.transferAmount = transferAmount;
	}
	
    @Override
    public String toString() {
        return "\n--------------------------------------------" +
                "\n Transfer Details" +
                "\n--------------------------------------------" +
                "\n Transfer ID: " + transferId +
                "\n Transfer Type ID:'" + transferTypeId +
                "\n Transfer Status ID: " + transferStatusId +
        		"\n Origin account: " + accountFrom +
        		"\n Destination account: " + accountTo +
        		"\n Transfer Amount: $" + transferAmount;
    }
	
	public int getTransferId() {
		return transferId;
	}
	public int getTransferTypeId() {
		return transferTypeId;
	}
	public int getTransferStatusId() {
		return transferStatusId;
	}
	public int getAccountFrom() {
		return accountFrom;
	}
	public int getAccountTo() {
		return accountTo;
	}

	public BigDecimal getTransferAmount() {
		return transferAmount;
	}

	public void setTransferId(int transferId) {
		this.transferId = transferId;
	}

	public void setTransferTypeId(int transferTypeId) {
		this.transferTypeId = transferTypeId;
	}

	public void setTransferStatusId(int transferStatusId) {
		this.transferStatusId = transferStatusId;
	}

	public void setAccountFrom(int accountFrom) {
		this.accountFrom = accountFrom;
	}

	public void setAccountTo(int accountTo) {
		this.accountTo = accountTo;
	}

	public void setTransferAmount(BigDecimal transferAmount) {
		this.transferAmount = transferAmount;
	}
	
	
}
