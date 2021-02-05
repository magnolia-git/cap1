package com.assignments.assignment7.services;

import java.io.Console;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.assignments.assignment7.repository.UserRepository;
import com.assignments.assignment7.util.JwtUtil;
import com.assignments.assignment7.models.*;
import com.assignments.assignment7.repository.*;

import Exceptions.AccountNotFoundException;
import Exceptions.ExceedsCombinedBalanceLimitException;
import Exceptions.NegativeBalanceException;
import Exceptions.TooManyAccountsException;
import Exceptions.TransactionFailureException;
import aj.org.objectweb.asm.Type;

@Service
public class MeritBankService {
	@Autowired
	private RolloverIRARepository RollIRA;
	@Autowired
	private RothIRARepository RothIRARepo;
	@Autowired
	private IRARepository irarepo;
	@Autowired
	private DBACheckingRepository DBACheckingRepo;
	@Autowired
	private AccountHoldersContactDetailsRepository ahContactDetailsrepository;
	@Autowired
	private AccountHolderRepository accountHolderRepository;
	@Autowired
	private SavingsAccountRepository savingsAccountRepository;
	@Autowired
	private CheckingAccountRepository checkingAccountRepository;
	@Autowired
	private CDAccountRepository cdAccountRepository;
	@Autowired
	private CDOfferingRepository cdOfferingRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private DepositTransactionRepository depositRepository;
	@Autowired 
	private WithdrawTransactionRepository withdrawRepository;
	@Autowired
	private TransferTransactionRepository transferTransactionRepository;
//	@Autowired
//	private MyUserDetailsService userDetailsService;
//	@Autowired
//	private JwtUtil jwtUtil;

	public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body("Error: Username is already taken!");
		}
		// Create new user
		User user = new User(signUpRequest.getUsername(), signUpRequest.getPassword());

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.AccountHolder)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.admin)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "AccountHolder":
					Role userRole = roleRepository.findByName(ERole.AccountHolder)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setActive(signUpRequest.isActive());
		user.setRoles(roles);
		userRepository.save(user);
		
		// Create Account Holder for User
		AccountHolder ah = new AccountHolder();
		AccountHoldersContactDetails ahDetails = new AccountHoldersContactDetails();
		ahDetails.setEmail(signUpRequest.getEmail());
		ahContactDetailsrepository.save(ahDetails);
		ah.setFirstName(signUpRequest.getFirstName())
			.setLastName(signUpRequest.getLastName())
			.setSSN(signUpRequest.getSsn()).setUser(user);
		ah.setAccountHoldersContactDetails(ahDetails);
		ah.setBirthDate(signUpRequest.getBirthDate());
		accountHolderRepository.save(ah);


		return ResponseEntity.ok("User registered successfully!");
	}

	public AccountHolder addAccountHolder(AccountHolder accountHolder) throws AccountNotFoundException {
		accountHolder.setUser(userRepository.findById(accountHolder.getUser().getId())
				.orElseThrow(() -> new AccountNotFoundException("Error: User is not found.")));
		return accountHolderRepository.save(accountHolder);
	}

	public List<AccountHolder> getAccountHolders() {
		return accountHolderRepository.findAll();
	}

	public AccountHolder getAccountHolderById(Integer id) throws AccountNotFoundException {
		return getById(id);
	}

	public AccountHolder getById(Integer id) {
		return accountHolderRepository.findById(id).orElse(null);
	}

//	public AccountHoldersContactDetails postContactDetails(@Valid @RequestBody AccountHoldersContactDetails ahContactDetails,
//			@PathVariable Integer id){
//		AccountHolder ah = getById(id);
//		ahContactDetails.setAccountHolder(ah);
//		//accountHolderRepository.save(ah);
//		ahContactDetailsrepository.save(ahContactDetails);
//		return ahContactDetails;
//	}
	public List<AccountHoldersContactDetails> getAccountHoldersContactDetails() {
		return ahContactDetailsrepository.findAll();
	}

	public CheckingAccount postCheckingAccount(CheckingAccount checkingAccount, Integer id)
			throws ExceedsCombinedBalanceLimitException {
		AccountHolder ah = getById(id);
		if (ah.getCombinedBalance() + checkingAccount.getBalance() > 250000) {
			throw new ExceedsCombinedBalanceLimitException("Balance exceeds limit");
		}
		ah.setCheckingAccounts(checkingAccount);
		checkingAccount.setAccountHolder(ah);
		checkingAccountRepository.save(checkingAccount);
		return checkingAccount;
	}

	public IRA postIRA(IRA ira, Integer id) {
		AccountHolder ah = getById(id);
		ah.setIra(ira);
		ira.setAccountHolder(ah);
		irarepo.save(ira);
		return ira;
	}

	public RothIRA postRothIRA(RothIRA ira, Integer id) {
		AccountHolder ah = getById(id);
		ah.setRothIRA(ira);
		ira.setAccountHolder(ah);
		RothIRARepo.save(ira);
		return ira;
	}

	public RolloverIRA postRolloverIRA(RolloverIRA ira, Integer id) {
		AccountHolder ah = getById(id);
		ah.setRollOverIRA(ira);
		ira.setAccountHolder(ah);
		RollIRA.save(ira);
		return ira;
	}

	public DBAChecking postDBACheckingAccount(DBAChecking dbacheckingAccount, Integer id)
			throws ExceedsCombinedBalanceLimitException, TooManyAccountsException {
		AccountHolder ah = getById(id);
		if (ah.getDbaCheckings().size() >= 3) {
			throw new TooManyAccountsException("can only have 3 DBA checking accounts ");
		}
		if (ah.getCombinedBalance() + dbacheckingAccount.getBalance() > 250000) {
			throw new ExceedsCombinedBalanceLimitException("Balance exceeds limit");
		}
		ah.setDbaCheckings((Arrays.asList(dbacheckingAccount)));
		dbacheckingAccount.setAccountHolder(ah);
		DBACheckingRepo.save(dbacheckingAccount);
		return dbacheckingAccount;
	}

	public List<DBAChecking> getDBACheckingAccountsById(@PathVariable Integer id) {
		return getById(id).getDbaCheckings();
	}

	public IRA getiraById(@PathVariable Integer id) {
		return getById(id).getIra();
	}

	public RothIRA getRothIraById(@PathVariable Integer id) {
		return getById(id).getRothIRA();
	}

	public RolloverIRA getRolloverIRAById(@PathVariable Integer id) {
		return getById(id).getRollOverIRA();
	}

	public CheckingAccount getCheckingAccountsById(@PathVariable Integer id) {
		return getById(id).getCheckingAccounts();
	}

	public SavingsAccount postSavingsAccount(SavingsAccount savingsAccount, int id)
			throws ExceedsCombinedBalanceLimitException {
		AccountHolder ah = getById(id);
		if (ah.getCombinedBalance() + savingsAccount.getBalance() > 250000) {
			throw new ExceedsCombinedBalanceLimitException("Balance exceeds limit");
		}
		ah.setSavingsAccounts(savingsAccount);
		savingsAccount.setAccountHolder(ah);

		savingsAccountRepository.save(savingsAccount);
		return savingsAccount;
	}

	public SavingsAccount getSavingsAccountsById(int id) throws AccountNotFoundException {
		return getById(id).getSavingsAccounts();
	}

	public CDAccount postCDAccount(CDAccount cdAccount, int id)
			throws AccountNotFoundException, ExceedsCombinedBalanceLimitException, TooManyAccountsException {
		AccountHolder ah = getById(id);
		if (ah.getCombinedBalance() + cdAccount.getBalance() > 250000) {
			throw new ExceedsCombinedBalanceLimitException("Balance exceeds limit");
		}
		if(ah.getcDAccounts().size() >= 1) {
			throw new TooManyAccountsException("Checking Account Already Exists");
		}
		ah.setcDAccounts(Arrays.asList(cdAccount));
		cdAccount.setAccountHolder(ah);
		cdAccountRepository.save(cdAccount);
		return cdAccount;
	}

	public List<CDAccount> getCDAccountsbyId(int id) {
		return getById(id).getcDAccounts();
	}

	public AccountHolder getMyAccountInfo(HttpServletRequest request) {
//		final String authorizationHeader = request.getHeader("Authorization");
//
		String username = request.getUserPrincipal().getName();// userDetails.;
//		String jwt = null;
		AccountHolder ah = null;
//
//		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//			jwt = authorizationHeader.substring(7);
//			username = jwtUtil.extractUsername(jwt);
//		}
		if (username != null) {

			User user = this.userRepository.findByUsername(username).orElseThrow(null);
			ah = user.getAccountHolder();
//            ah = accountHolderRepository.findById(user.getId()).orElseThrow(null);
		}
		return ah;
	}

	public CheckingAccount getMyCheckingAccounts(HttpServletRequest request) {
		AccountHolder ah = getMyAccountInfo(request);
		return ah.getCheckingAccounts();
	}

	public CheckingAccount postMyCheckingAccount(HttpServletRequest request, CheckingAccount checkingAccount)
			throws ExceedsCombinedBalanceLimitException, TooManyAccountsException {

		AccountHolder ah = getMyAccountInfo(request);
		if (ah.getCombinedBalance() + checkingAccount.getBalance() > 250000) {
			throw new ExceedsCombinedBalanceLimitException("Balance exceeds limit");
		}
		if(ah.getCheckingAccounts() != null) {
			throw new TooManyAccountsException("Checking Account Already Exists");
		}
		ah.setCheckingAccounts(checkingAccount);
		checkingAccount.setAccountHolder(ah);
		checkingAccountRepository.save(checkingAccount);
		return checkingAccount;
	}

	public DBAChecking postMyDBACheckingAccount(HttpServletRequest request, DBAChecking dbacheckingAccount)
			throws ExceedsCombinedBalanceLimitException, TooManyAccountsException {
		AccountHolder ah = getMyAccountInfo(request);
		if (ah.getDbaCheckings().size() >= 3) {
			throw new TooManyAccountsException("can only have 3 DBA checking accounts ");
		}
		if (ah.getCombinedBalance() + dbacheckingAccount.getBalance() > 250000) {
			throw new ExceedsCombinedBalanceLimitException("Balance exceeds limit");
		}
		ah.setDbaCheckings((Arrays.asList(dbacheckingAccount)));
		dbacheckingAccount.setAccountHolder(ah);
		DBACheckingRepo.save(dbacheckingAccount);
		return dbacheckingAccount;
	}

	public SavingsAccount postMySavingsAccount(HttpServletRequest request, SavingsAccount savingsAccount)
			throws ExceedsCombinedBalanceLimitException, TooManyAccountsException {

		AccountHolder ah = getMyAccountInfo(request);
		if (ah.getCombinedBalance() + savingsAccount.getBalance() > 250000) {
			throw new ExceedsCombinedBalanceLimitException("Balance exceeds limit");
		}
		if(ah.getSavingsAccounts() != null) {
			throw new TooManyAccountsException("Savings Account Already Exists");
		}
		ah.setSavingsAccounts(savingsAccount);
		savingsAccount.setAccountHolder(ah);
		savingsAccountRepository.save(savingsAccount);
		return savingsAccount;
	}

	public SavingsAccount getMySavingsAccounts(HttpServletRequest request) {
		AccountHolder ah = getMyAccountInfo(request);
		return ah.getSavingsAccounts();
	}

	public List<DBAChecking> getMyDBACheckingAccounts(HttpServletRequest request) {
		AccountHolder ah = getMyAccountInfo(request);
		return ah.getDbaCheckings();
	}

	public CDAccount postMyCDAccounts(HttpServletRequest request, CDAccount cDAccount)
			throws ExceedsCombinedBalanceLimitException, TooManyAccountsException {

		AccountHolder ah = getMyAccountInfo(request);
		if (ah.getCombinedBalance() + cDAccount.getBalance() > 250000) {
			throw new ExceedsCombinedBalanceLimitException("Balance exceeds limit");
		}
		if(ah.getcDAccounts().size() >= 1) {
			throw new TooManyAccountsException("Checking Account Already Exists");
		}
		ah.setcDAccounts((Arrays.asList(cDAccount)));
		cDAccount.setAccountHolder(ah);
		cdAccountRepository.save(cDAccount);
		return cDAccount;
	}

	public List<CDAccount> getMyCDAccount(HttpServletRequest request) {
		AccountHolder ah = getMyAccountInfo(request);
		return ah.getcDAccounts();
	}

	public CDOffering postCDOffering(CDOffering cdOffering) {
		return cdOfferingRepository.save(cdOffering);
	}

	public List<CDOffering> getCDOfferings() {
		return cdOfferingRepository.findAll();
	}

	public IRA postMyIRA(HttpServletRequest request, @Valid IRA ira) throws TooManyAccountsException {
		AccountHolder ah = getMyAccountInfo(request);
		if(ah.getIra() != null) {
			throw new TooManyAccountsException("Checking Account Already Exists");
		}
		ah.setIra(ira);
		ira.setAccountHolder(ah);
		irarepo.save(ira);
		return ira;
	}

	public IRA getMyIRA(HttpServletRequest request) {
		AccountHolder ah = getMyAccountInfo(request);
		return ah.getIra();
	}

	public RothIRA postMyRothIRA(HttpServletRequest request, @Valid RothIRA RothIRA) throws TooManyAccountsException {
		AccountHolder ah = getMyAccountInfo(request);
		if(ah.getRothIRA() != null) {
			throw new TooManyAccountsException("Checking Account Already Exists");
		}
		ah.setRothIRA(RothIRA);
		RothIRA.setAccountHolder(ah);
		RothIRARepo.save(RothIRA);
		return RothIRA;
	}

	public RothIRA getMyRothIRA(HttpServletRequest request) {
		AccountHolder ah = getMyAccountInfo(request);
		return ah.getRothIRA();
	}

	public RolloverIRA postMyRolloverIRA(HttpServletRequest request, @Valid RolloverIRA RolloverIRA) throws TooManyAccountsException {
		AccountHolder ah = getMyAccountInfo(request);
		if(ah.getRollOverIRA() != null) {
			throw new TooManyAccountsException("Checking Account Already Exists");
		}
		ah.setRollOverIRA(RolloverIRA);
		RolloverIRA.setAccountHolder(ah);
		RollIRA.save(RolloverIRA);
		return RolloverIRA;
	}

	public RolloverIRA getMyRolloverIRA(HttpServletRequest request) {
		AccountHolder ah = getMyAccountInfo(request);
		return ah.getRollOverIRA();
	}

	public BankAccount postMyDeposit(HttpServletRequest request, DepositTransaction deposit, String type)
			throws ExceedsCombinedBalanceLimitException, NegativeBalanceException {
		switch (type) {
		case "DBACheckingAccount":
			DBAChecking existingDBA;
			Optional<DBAChecking> dba = DBACheckingRepo.findById(deposit.getDbaChecking().getId());
			if (dba != null) {
				existingDBA = dba.get();
				deposit.setDbaChecking(existingDBA);
				deposit.process();
				DBACheckingRepo.save(existingDBA);
				
				depositRepository.save(deposit);
				return existingDBA;
			}
			else {
				new TransactionFailureException();
			}
			break;
		case "CheckingAccount":
			CheckingAccount existingChecking;
			Optional<CheckingAccount> check = checkingAccountRepository.findById(deposit.getChecking().getId());
			if (check.isPresent()) {
				existingChecking = check.get();
				deposit.setChecking(existingChecking);
				deposit.process();
				checkingAccountRepository.save(existingChecking);
				
				depositRepository.save(deposit);
				return existingChecking;
				
			} else {
				new TransactionFailureException();
			}
		case "SavingsAccount":
			SavingsAccount existingSavings;
			Optional<SavingsAccount> sav = savingsAccountRepository.findById(deposit.getSavings().getId());
			if (sav.isPresent()) {
				existingSavings = sav.get();
				deposit.setSavings(existingSavings);
				deposit.process();
				savingsAccountRepository.save(existingSavings);
				
				depositRepository.save(deposit);
				return existingSavings;
				
			} else {
				new TransactionFailureException();
			}
		case "CDAccount":
			CDAccount existingCDAccount;
			Optional<CDAccount> cda = cdAccountRepository.findById(deposit.getCdAccount().getId());
			if (cda.isPresent()) {
				existingCDAccount = cda.get();
				deposit.setCdAccount(existingCDAccount);
				deposit.process();
				cdAccountRepository.save(existingCDAccount);
				
				depositRepository.save(deposit);
				return existingCDAccount;
				
			} else {
				new TransactionFailureException();
			}
		case "IRA":
			IRA existingIRA;
			Optional<IRA> ira = irarepo.findById(deposit.getIra().getId());
			if (ira.isPresent()) {
				existingIRA = ira.get();
				deposit.setIra(existingIRA);
				deposit.process();
				irarepo.save(existingIRA);
				
				depositRepository.save(deposit);
				return existingIRA;
				
			} else {
				new TransactionFailureException();
			}
		case "RothIRA":
			RothIRA existingRothIRA;
			Optional<RothIRA> rothIRA = RothIRARepo.findById(deposit.getRothIRA().getId());
			if (rothIRA.isPresent()) {
				existingRothIRA = rothIRA.get();
				deposit.setRothIRA(existingRothIRA);
				deposit.process();
				RothIRARepo.save(existingRothIRA);
				
				depositRepository.save(deposit);
				return existingRothIRA;
				
			} else {
				new TransactionFailureException();
			}
		case "RolloverIRA":
			RolloverIRA existingRolloverIRA;
			Optional<RolloverIRA> rolloverIRA = RollIRA.findById(deposit.getRolloverIRA().getId());
			if (rolloverIRA.isPresent()) {
				existingRolloverIRA = rolloverIRA.get();
				deposit.setRolloverIRA(existingRolloverIRA);
				deposit.process();
				RollIRA.save(existingRolloverIRA);
				
				depositRepository.save(deposit);
				return existingRolloverIRA;
				
			} else {
				new TransactionFailureException();
			}
		default:
			new TransactionFailureException();
		}
		return null;
		
	}
	public List<Transaction> getMyDeposit(String location) {
		return depositRepository.findByLocation(location);
//		switch (location) {
//		case "DBACheckingAccount":
//			return depositRepository.findByLocation("dbaChecking");
//			//break;
//		case "CheckingAccount":
//			return depositRepository.findByLocation("checkingAccount");
//			//break;
//		case "SavingsAccount":
//			return depositRepository.findByLocation("savingsAccount");
//		case "CDAccount":
//			return depositRepository.findByLocation("cdAccount");
//		default:
//			break;
//		}
		//return null;
	}
	////////////////////////////////////////
	
	public BankAccount postMyWithdrawl(HttpServletRequest request, WithdrawTransaction withdraw, String type)
			throws ExceedsCombinedBalanceLimitException, NegativeBalanceException {
		switch (type) {
		case "DBACheckingAccount":
			DBAChecking existingDBA;
			Optional<DBAChecking> dba = DBACheckingRepo.findById(withdraw.getDbaChecking().getId());
			if (dba != null) {
				existingDBA = dba.get();
				withdraw.setDbaChecking(existingDBA);
				withdraw.process();
				DBACheckingRepo.save(existingDBA);
				
				withdrawRepository.save(withdraw);
				return existingDBA;
			}
			else {
				new TransactionFailureException();
			}
			break;
		case "CheckingAccount":
			CheckingAccount existingChecking;
			Optional<CheckingAccount> check = checkingAccountRepository.findById(withdraw.getChecking().getId());
			if (check.isPresent()) {
				existingChecking = check.get();
				withdraw.setChecking(existingChecking);
				withdraw.process();
				checkingAccountRepository.save(existingChecking);
				
				withdrawRepository.save(withdraw);
				return existingChecking;
				
			} else {
				new TransactionFailureException();
			}
		case "SavingsAccount":
			SavingsAccount existingSavings;
			Optional<SavingsAccount> sav = savingsAccountRepository.findById(withdraw.getSavings().getId());
			if (sav.isPresent()) {
				existingSavings = sav.get();
				withdraw.setSavings(existingSavings);
				withdraw.process();
				savingsAccountRepository.save(existingSavings);
				
				withdrawRepository.save(withdraw);
				return existingSavings;
				
			} else {
				new TransactionFailureException();
			}
		case "CDAccount":
			CDAccount existingCDAccount;
			Optional<CDAccount> cda = cdAccountRepository.findById(withdraw.getCdAccount().getId());
			if (cda.isPresent()) {
				existingCDAccount = cda.get();
				withdraw.setCdAccount(existingCDAccount);
				withdraw.process();
				cdAccountRepository.save(existingCDAccount);
				
				withdrawRepository.save(withdraw);
				return existingCDAccount;
				
			} else {
				new TransactionFailureException();
			}
		case "IRA":
			IRA existingIRA;
			Optional<IRA> ira = irarepo.findById(withdraw.getIra().getId());
			if (ira.isPresent()) {
				existingIRA = ira.get();
				withdraw.setIra(existingIRA);
				withdraw.process();
				irarepo.save(existingIRA);
				
				withdrawRepository.save(withdraw);
				return existingIRA;
				
			} else {
				new TransactionFailureException();
			}
		case "RothIRA":
			RothIRA existingRothIRA;
			Optional<RothIRA> rothIRA = RothIRARepo.findById(withdraw.getRothIRA().getId());
			if (rothIRA.isPresent()) {
				existingRothIRA = rothIRA.get();
				withdraw.setRothIRA(existingRothIRA);
				withdraw.process();
				RothIRARepo.save(existingRothIRA);
				
				withdrawRepository.save(withdraw);
				return existingRothIRA;
				
			} else {
				new TransactionFailureException();
			}
		case "RolloverIRA":
			RolloverIRA existingRolloverIRA;
			Optional<RolloverIRA> rolloverIRA = RollIRA.findById(withdraw.getRolloverIRA().getId());
			if (rolloverIRA.isPresent()) {
				existingRolloverIRA = rolloverIRA.get();
				withdraw.setRolloverIRA(existingRolloverIRA);
				withdraw.process();
				RollIRA.save(existingRolloverIRA);
				
				withdrawRepository.save(withdraw);
				return existingRolloverIRA;
				
			} else {
				new TransactionFailureException();
			}
		default:
			new TransactionFailureException();
		}
		return null;
		
	}
	public List<Transaction> getMyWithdrawl(String location) {
		return withdrawRepository.findByLocation(location);
	}

	//TRANSFERS
	
	public List<BankAccount> postMyTransfer(HttpServletRequest request, @Valid TransferTransaction transfer)
			throws NegativeBalanceException, ExceedsCombinedBalanceLimitException, TransactionFailureException {
		// TODO Auto-generated method stub
		AccountHolder ah = getMyAccountInfo(request);
		List<BankAccount> allAccounts = ah.getAllAccounts();
		String typeOfAccount;
		for(BankAccount ba : allAccounts) {
			if(ba.getId() == transfer.getSourceAccountID()) {
				transfer.setSourceAccount(ba);
			}
			if(ba.getId() == transfer.getTargetAccountID()) {
				transfer.setTargetAccount(ba);
			}
		}
		if(transfer.getSourceAccount() != null && transfer.getTargetAccount() != null)
			transfer.process();
		else
			throw new TransactionFailureException();
		
		saveTargetAccountByType(transfer.getTargetAccount().getTypeOfAccount(), transfer);
		saveSourceAccountByType(transfer.getSourceAccount().getTypeOfAccount(), transfer);
		transferTransactionRepository.save(transfer);
		return transfer.getSourceAndTransferAccounts();
	}
	public void saveTargetAccountByType(String typeOfAccount, TransferTransaction transfer)	{
		switch (typeOfAccount) {
		case "DBAChecking": 
			DBACheckingRepo.save((DBAChecking) transfer.getTargetAccount());
			break;
		case "CheckingAccount":
			checkingAccountRepository.save((CheckingAccount) transfer.getTargetAccount());
		default:
			break;
		}
	}
	public void saveSourceAccountByType(String typeOfAccount, TransferTransaction transfer)	{
		switch (typeOfAccount) {
		case "DBAChecking": 
			DBACheckingRepo.save((DBAChecking) transfer.getSourceAccount());
			break;
		case "CheckingAccount":
			checkingAccountRepository.save((CheckingAccount) transfer.getSourceAccount());
		default:
			break;
		}
	}
}



