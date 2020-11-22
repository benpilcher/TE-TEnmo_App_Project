package com.techelevator.tenmo;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserService;
import com.techelevator.view.ConsoleService;

public class App {

	private static final String API_BASE_URL = "http://localhost:8080/";

	private static final String MENU_OPTION_EXIT = "Exit";
	private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN,
			MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS,
			MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS,
			MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };

	private AuthenticatedUser currentUser;
	private ConsoleService console;
	private AuthenticationService authenticationService;
	private AccountService accountService;
	private UserService userService;
	private TransferService transferService;

	public static void main(String[] args) {
		App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL),
				new AccountService(API_BASE_URL), new UserService(API_BASE_URL), new TransferService(API_BASE_URL));
		app.run();
	}

	public App(ConsoleService console, AuthenticationService authenticationService, AccountService accountService,
			UserService userService, TransferService transferService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.accountService = accountService;
		this.userService = userService;
		this.transferService = transferService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");

		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while (true) {
			String choice = (String) console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if (MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		if (currentUser != null) {
			BigDecimal balance = accountService.getBalance(currentUser);

			System.out.println("Your current balance is: $" + balance);
		} else {
			System.out.println("currentUser is null");
		}
	}

	private void viewTransferHistory() {

		List<Transfer> allUserTransfers = transferService.listTransfers(currentUser);
		String username;
		String fromOrTo;

		System.out.println("-------------------------------------------");
		System.out.println("Transfers");
		System.out.println("ID\tFrom/To\t\tAmount");
		System.out.println("-------------------------------------------");

		for (Transfer transfer : allUserTransfers) {
			if (transfer.getAccountFrom() == (currentUser.getUser().getId())) {
				username = userService.findByUserId(Long.valueOf(transfer.getAccountTo())).getUsername();
				fromOrTo = "To: ";
			} else {
				username = userService.findByUserId(Long.valueOf(transfer.getAccountFrom())).getUsername();
				fromOrTo = "From: ";
			}
			System.out.println(
					transfer.getTransferId() + "\t" + fromOrTo + username + "\t$" + transfer.getTransferAmount());
		}
		System.out.println("---------");
		int choice = console.getUserInputInteger("Please enter transfer ID to view details (0 to cancel): ");

		if (choice == 0) {
			mainMenu();
		}

		try {
			Transfer selectedTransfer = transferService.getTransferDetails(Long.valueOf(currentUser.getUser().getId()),
					choice);

			String transferStatus = null;
			if (selectedTransfer.getTransferStatusId() == 1) {
				transferStatus = "Pending";
			} else if (selectedTransfer.getTransferStatusId() == 2) {
				transferStatus = "Approved";
			} else if (selectedTransfer.getTransferStatusId() == 3) {
				transferStatus = "Rejected";
			}

			String transferType = null;
			if (selectedTransfer.getTransferTypeId() == 1) {
				transferType = "Request";
			} else if (selectedTransfer.getTransferTypeId() == 2) {
				transferType = "Send";
			}

			System.out.println("--------------------------------------------");
			System.out.println("Transfer Details");
			System.out.println("--------------------------------------------");
			System.out.println("Id: " + selectedTransfer.getTransferId());
			System.out.println(
					"From: " + userService.findByUserId(Long.valueOf(selectedTransfer.getAccountFrom())).getUsername());
			System.out.println(
					"To: " + userService.findByUserId(Long.valueOf(selectedTransfer.getAccountTo())).getUsername());
			System.out.println("Type: " + transferType);
			System.out.println("Status: " + transferStatus);
			System.out.println("Amount: $" + selectedTransfer.getTransferAmount());

		} catch (NullPointerException e) {
			System.out.println("Invalid selection. Please make another selection.");
			viewTransferHistory();
		}

	}

	private void viewPendingRequests() {

	}

	private void sendBucks() {

		List<User> allUsers = userService.findAll();
		System.out.println("----------------------------------------");
		System.out.println("Users");
		System.out.println("ID\tName");
		System.out.println("----------------------------------------");
		
		for (User user : allUsers) {
			System.out.println(user.getId() + "\t" + user.getUsername());
		}
		
		System.out.println("--------------");
		
		int choice = console.getUserInputInteger("Enter ID of user you are sending to (0 to cancel)");
		
		if (choice == 0) {
			mainMenu();
		}
		
		boolean userValid = false;
		
		for (User user : allUsers) {
			if (choice == user.getId() && choice != currentUser.getUser().getId()) {
				userValid = true;
			}

		}
		
		if (userValid) {
			Double amountToSend = null;
			try {
				amountToSend = Double.parseDouble(console.getUserInput("Enter amount to send: $"));
				BigDecimal amountToSendBigDecimal = BigDecimal.valueOf(amountToSend);
				User receiver = userService.findByUserId(Long.valueOf(choice));

				BigDecimal validAmount = transferService.sendAmount(currentUser, receiver, amountToSendBigDecimal);
				
				if (validAmount == null) {
					System.out.println("Invalid amount. Please try again.");
					sendBucks();
				} else {
					System.out.println("Transfer successful!");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid amount. Please try again.");
				sendBucks();
			}
			
		} else {
			System.out.println("Invalid selection. Please make another selection.");
			sendBucks();
		}
		
	}

	private void requestBucks() {

	}

	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while (!isAuthenticated()) {
			String choice = (String) console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
		while (!isRegistered) // will keep looping until user is registered
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				authenticationService.register(credentials);
				isRegistered = true;
				System.out.println("Registration successful. You can now login.");
			} catch (AuthenticationServiceException e) {
				System.out.println("REGISTRATION ERROR: " + e.getMessage());
				System.out.println("Please attempt to register again.");
			}
		}
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) // will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: " + e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}

	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
