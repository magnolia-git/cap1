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
import Exceptions.ToManyAccountsException;
import Exceptions.TransactionFailureException;

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
	private MyUserDetailsService userDetailsService;
	@Autowired
	private JwtUtil jwtUtil;

	public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body("Error: Username is already taken!");
		}
		// Create new user's account
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
		ah.setCheckingAccounts((checkingAccount));
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
			throws ExceedsCombinedBalanceLimitException, ToManyAccountsException {
		AccountHolder ah = getById(id);
		if (ah.getDbaCheckings().size() >= 3) {
			throw new ToManyAccountsException("can only have 3 DBA checking accounts ");
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
			throws AccountNotFoundException, ExceedsCombinedBalanceLimitException {
		AccountHolder ah = getById(id);
		if (ah.getCombinedBalance() + cdAccount.getBalance() > 250000) {
			throw new ExceedsCombinedBalanceLimitException("Balance exceeds limit");
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
			throws ExceedsCombinedBalanceLimitException {

		AccountHolder ah = getMyAccountInfo(request);
		if (ah.getCombinedBalance() + checkingAccount.getBalance() > 250000) {
			throw new ExceedsCombinedBalanceLimitException("Balance exceeds limit");
		}
		ah.setCheckingAccounts(checkingAccount);
		checkingAccount.setAccountHolder(ah);
		checkingAccountRepository.save(checkingAccount);
		return checkingAccount;
	}

	public DBAChecking postMyDBACheckingAccount(HttpServletRequest request, DBAChecking dbacheckingAccount)
			throws ExceedsCombinedBalanceLimitException, ToManyAccountsException {
		AccountHolder ah = getMyAccountInfo(request);
		if (ah.getDbaCheckings().size() >= 3) {
			throw new ToManyAccountsException("can only have 3 DBA checking accounts ");
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
			throws ExceedsCombinedBalanceLimitException {

		AccountHolder ah = getMyAccountInfo(request);
		if (ah.getCombinedBalance() + savingsAccount.getBalance() > 250000) {
			throw new ExceedsCombinedBalanceLimitException("Balance exceeds limit");
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
			throws ExceedsCombinedBalanceLimitException {

		AccountHolder ah = getMyAccountInfo(request);
		if (ah.getCombinedBalance() + cDAccount.getBalance() > 250000) {
			throw new ExceedsCombinedBalanceLimitException("Balance exceeds limit");
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

	public IRA postMyIRA(HttpServletRequest request, @Valid IRA ira) {
		AccountHolder ah = getMyAccountInfo(request);
		ah.setIra(ira);
		ira.setAccountHolder(ah);
		irarepo.save(ira);
		return ira;
	}

	public IRA getMyIRA(HttpServletRequest request) {
		AccountHolder ah = getMyAccountInfo(request);
		return ah.getIra();
	}

	public RothIRA postMyRothIRA(HttpServletRequest request, @Valid RothIRA RothIRA) {
		AccountHolder ah = getMyAccountInfo(request);
		ah.setRothIRA(RothIRA);
		RothIRA.setAccountHolder(ah);
		RothIRARepo.save(RothIRA);
		return RothIRA;
	}

	public RothIRA getMyRothIRA(HttpServletRequest request) {
		AccountHolder ah = getMyAccountInfo(request);
		return ah.getRothIRA();
	}

	public RolloverIRA postMyRolloverIRA(HttpServletRequest request, @Valid RolloverIRA RolloverIRA) {
		AccountHolder ah = getMyAccountInfo(request);
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
			// deposit.setBankAccount( request.getParameter("bankAccount"));
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
			// deposit.setBankAccount(getMyAccountInfo(request).getDbaCheckings().get(0));
			
			// Object test3 = request.getUserPrincipal().getName();
//			ah.setcDAccounts((Arrays.asList(cDAccount)));
//			cDAccount.setAccountHolder(ah);
//			cdAccountRepository.save(cDAccount);
			
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
		case "three":
			System.out.println("three");
			break;
		default:
			System.out.println("no match");
			break;
		}
		return null;
	}
	public List<Transaction> getMyDeposit(String location) {
		switch (location) {
		case "DBACheckingAccount":
			return depositRepository.findByLocation("dbaChecking");
			//break;
		case "CheckingAccount":
			return depositRepository.findByLocation("checkingAccount");
			//break;
		case "SavingsAccount":
			return depositRepository.findByLocation("savingsAccount");
		default:
			break;
		}
		return null;
	}
}