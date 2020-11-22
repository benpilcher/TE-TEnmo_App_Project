package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;
import com.techelevator.tenmo.model.Transfer;

public interface TransferDAO {

	List<Transfer> listTransfers(Long userId);
	
	BigDecimal sendAmount(int senderId, int receiverId, BigDecimal transferAmount);
	
	Transfer getTransferDetails(Long userId, int transferId);
}
