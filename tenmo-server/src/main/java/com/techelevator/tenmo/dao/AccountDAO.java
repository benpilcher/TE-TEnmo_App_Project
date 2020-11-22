package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface AccountDAO {

	BigDecimal getBalance(int accountId);
	
	Integer getAccountId(int userId);

}
