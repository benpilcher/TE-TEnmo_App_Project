package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;


@RestController
public class TransferController {

	private AccountDAO accountDAO;
	private TransferDAO transferDAO;
	private UserDAO userDAO;
	
	public TransferController(AccountDAO accountDAO, TransferDAO transferDAO, UserDAO userDAO) {
		this.accountDAO = accountDAO;
		this.transferDAO = transferDAO;
		this.userDAO = userDAO;
	}
	
	@RequestMapping(path = "users/accounts/{account_id}", method = RequestMethod.GET)
	public BigDecimal getBalance(@PathVariable int account_id) {
		BigDecimal myBalance = accountDAO.getBalance(account_id);
		return myBalance;
	}
	
	@RequestMapping(path = "users", method = RequestMethod.GET)
	public List<User> findAll() {
		List<User> allUsers = userDAO.findAll();
		return allUsers;
	}
	
	@RequestMapping(path = "users/{user_id}", method = RequestMethod.GET)
	public User findByUserId(@PathVariable Long user_id) {
		User user = userDAO.findByUserId(user_id);
		return user;
	}
	
	@RequestMapping(path = "users/accounts/ids/{user_id}", method = RequestMethod.GET)
	public Integer getAccountId(@PathVariable int user_id) {
		Integer accountId = accountDAO.getAccountId(user_id);
		return accountId;
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path = "transfers", method = RequestMethod.POST)
	public BigDecimal sendAmount(@RequestBody Transfer transfer) {
		BigDecimal amountSent = transferDAO.sendAmount(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getTransferAmount());
		return amountSent;
	}
	
	@RequestMapping(path = "transfers/{user_id}", method = RequestMethod.GET)
	public List<Transfer> listTransfers(@PathVariable Long user_id) {
		List<Transfer> allTransfers = transferDAO.listTransfers(user_id);
		return allTransfers;
	}
	
	@RequestMapping(path = "{user_id}/transfers/details/{transfer_id}", method = RequestMethod.GET)
	public Transfer getTransferDetails(@PathVariable Long user_id, @PathVariable int transfer_id) {
		Transfer selectedTransfer = transferDAO.getTransferDetails(user_id, transfer_id);
		return selectedTransfer;
	}

}
