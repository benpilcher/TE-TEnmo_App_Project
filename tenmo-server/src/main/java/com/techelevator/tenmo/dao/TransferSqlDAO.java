package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferIdNotFoundException;

@Component
public class TransferSqlDAO implements TransferDAO {

	private JdbcTemplate jdbcTemplate;
	
	public TransferSqlDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public List<Transfer> listTransfers(Long userId) {

        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfers JOIN accounts ON (transfers.account_from = accounts.account_id OR " +
        			 "transfers.account_to = accounts.account_id) WHERE user_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while(results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }

        return transfers;

	}

	@Override
	public Transfer getTransferDetails(Long userId, int transferId) throws TransferIdNotFoundException {
        for (Transfer transfer : this.listTransfers(userId)) {
            if(transfer.getTransferId() == transferId) { 
                return transfer;
            }
        }
        throw new TransferIdNotFoundException ();
    }
	
	@Override
	public BigDecimal sendAmount(int senderId, int receiverId, BigDecimal transferAmount) {
		
		String sqlTransfer = "INSERT INTO transfers (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) "
				     + "VALUES (DEFAULT, 2, 2, ?, ?, ?)";
		jdbcTemplate.update(sqlTransfer, senderId, receiverId, transferAmount);
		
		String sqlRemoveMoney = "UPDATE accounts SET balance = (balance - ?) WHERE account_id = ?";
		jdbcTemplate.update(sqlRemoveMoney, transferAmount, senderId);
		
		String sqlAddMoney= "UPDATE accounts SET balance = (balance + ?) WHERE account_id = ?";
		jdbcTemplate.update(sqlAddMoney, transferAmount, receiverId);
		
		return transferAmount;
	}



    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));
        transfer.setTransferAmount(rs.getBigDecimal("amount"));
       
        return transfer;
    }
	
}
