package com.assignments.assignment7.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.synth.Region;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.assignments.assignment7.models.AccountHolder;
import com.assignments.assignment7.models.AccountHoldersContactDetails;
import com.assignments.assignment7.models.AuthenticationRequest;
import com.assignments.assignment7.models.AuthenticationResponse;
import com.assignments.assignment7.models.BankAccount;
import com.assignments.assignment7.models.CDAccount;
import com.assignments.assignment7.models.CDOffering;
import com.assignments.assignment7.models.CheckingAccount;
import com.assignments.assignment7.models.DBAChecking;
import com.assignments.assignment7.models.DepositTransaction;
import com.assignments.assignment7.models.IRA;
import com.assignments.assignment7.models.RolloverIRA;
import com.assignments.assignment7.models.RothIRA;
import com.assignments.assignment7.models.SavingsAccount;
import com.assignments.assignment7.models.SignupRequest;
import com.assignments.assignment7.models.Transaction;
import com.assignments.assignment7.models.TransferTransaction;
import com.assignments.assignment7.models.WithdrawTransaction;
import com.assignments.assignment7.services.MeritBankService;
import com.assignments.assignment7.services.MyUserDetailsService;
import com.assignments.assignment7.util.JwtUtil;

import Exceptions.AccountNotFoundException;
import Exceptions.ExceedsCombinedBalanceLimitException;
import Exceptions.NegativeBalanceException;
import Exceptions.TooManyAccountsException;
import Exceptions.TransactionFailureException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("/api")
public class MeritBankController {
	Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private MeritBankService meritBankService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	@Autowired
	private JwtUtil jwtTokenUtil;

	//@PreAuthorize("hasAuthority('admin')")
	@PostMapping("/authenticate/createUser")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		return meritBankService.registerUser(signUpRequest);
	}

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAutheticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("incorrect username or password", e);
		}
		final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new AuthenticationResponse(jwt));

	}

	@PreAuthorize("hasAuthority('admin')")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/AccountHolders")
	public AccountHolder addAccountHolder(@Valid @RequestBody AccountHolder accountHolder)
			throws AccountNotFoundException {
		return meritBankService.addAccountHolder(accountHolder);
	}

	@PreAuthorize("hasAuthority('admin')")
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/AccountHolders")
	public List<AccountHolder> getAccountHolders() {
		return meritBankService.getAccountHolders();
	}

	@PreAuthorize("hasAuthority('admin')")
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/AccountHolders/{id}")
	public AccountHolder getAccountHolderById(@PathVariable Integer id) throws AccountNotFoundException {
		AccountHolder ah;
		// debug log - entering
		try {
			// use this only when someone logs in - to have record on log of login
			log.info("Entered /AccountHolders/{1} End Point");
			ah = meritBankService.getAccountHolderById(id);
		} catch (Exception e) {
			// error log - there's been an error + exception
			log.debug("getAccountById Started" + e);
			throw new AccountNotFoundException("Account id not found");
		}
		log.info("Entered /AccountHolders/{1} End Point");
		// debug log - returning
		return ah;
	}


	@PreAuthorize("hasAuthority('admin')")
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/AccountHolders/{id}/DBACheckingAccounts")
	public DBAChecking postDBACheckingAccount(@Valid @RequestBody DBAChecking checkingAccount,
			@PathVariable Integer id) throws ExceedsCombinedBalanceLimitException, TooManyAccountsException {
		return meritBankService.postDBACheckingAccount(checkingAccount, id);
	}
	
	@PreAuthorize("hasAuthority('admin')")
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/AccountHolders/{id}/IRA")
	public IRA postDBACheckingAccount(@Valid @RequestBody IRA ira,
			@PathVariable Integer id) throws ExceedsCombinedBalanceLimitException, TooManyAccountsException {
		return meritBankService.postIRA(ira, id);
	}
	@PreAuthorize("hasAuthority('admin')")
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/AccountHolders/{id}/RothIRA")
	public RothIRA postDBACheckingAccount(@Valid @RequestBody RothIRA ira,
			@PathVariable Integer id) throws ExceedsCombinedBalanceLimitException, TooManyAccountsException {
		return meritBankService.postRothIRA(ira, id);
	}
	@PreAuthorize("hasAuthority('admin')")
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/AccountHolders/{id}/RolloverIRA")
	public RolloverIRA postDBACheckingAccount(@Valid @RequestBody RolloverIRA ira,
			@PathVariable Integer id) throws ExceedsCombinedBalanceLimitException, TooManyAccountsException {
		return meritBankService.postRolloverIRA(ira, id);
	}
	
	@PreAuthorize("hasAuthority('admin')")
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/AccountHolders/{id}/CheckingAccounts")
	public CheckingAccount postCheckingAccount(@Valid @RequestBody CheckingAccount checkingAccount,
			@PathVariable Integer id) throws ExceedsCombinedBalanceLimitException {
		return meritBankService.postCheckingAccount(checkingAccount, id);
	}


	@PreAuthorize("hasAuthority('admin')")
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/AccountHolders/{id}/DBACheckingAccounts")
	public List<DBAChecking> getDBACheckingAccountsById(@PathVariable Integer id) throws AccountNotFoundException {

			return meritBankService.getDBACheckingAccountsById(id);

	}
	@PreAuthorize("hasAuthority('admin')")
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/AccountHolders/{id}/IRA")
	public IRA getIRAById(@PathVariable Integer id) throws AccountNotFoundException {

			return meritBankService.getiraById(id);

	}
	
	@PreAuthorize("hasAuthority('admin')")
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/AccountHolders/{id}/RothIRA")
	public RothIRA getRothIRAById(@PathVariable Integer id) throws AccountNotFoundException {

			return meritBankService.getRothIraById(id);

	}
	@PreAuthorize("hasAuthority('admin')")
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/AccountHolders/{id}/RolloverIRA")
	public RolloverIRA getRolloverIRAById(@PathVariable Integer id) throws AccountNotFoundException {

			return meritBankService.getRolloverIRAById(id);

	}
	
	@PreAuthorize("hasAuthority('admin')")
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/AccountHolders/{id}/CheckingAccounts")
	public CheckingAccount getCheckingAccountsById(@PathVariable Integer id) throws AccountNotFoundException {

			return meritBankService.getCheckingAccountsById(id);
		
	}

	@PreAuthorize("hasAuthority('admin')")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/AccountHolders/{id}/SavingsAccounts")
	public SavingsAccount postSavingsAccount(@Valid @RequestBody SavingsAccount savingsAccount, @PathVariable int id)
			throws ExceedsCombinedBalanceLimitException {
		return meritBankService.postSavingsAccount(savingsAccount, id);
	}

	@PreAuthorize("hasAuthority('admin')")
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/AccountHolders/{id}/SavingsAccounts")
	public SavingsAccount getSavingsAccountsById(@PathVariable int id) throws AccountNotFoundException {
		return meritBankService.getSavingsAccountsById(id);
	}

	@PreAuthorize("hasAuthority('admin')")
	@PostMapping(value = "/AccountHolders/{id}/CDAccounts")
	public CDAccount postCDAccount(@Valid @RequestBody CDAccount cdAccount, @PathVariable int id)
			throws AccountNotFoundException, ExceedsCombinedBalanceLimitException, TooManyAccountsException {
		return meritBankService.postCDAccount(cdAccount, id);
	}

	@PreAuthorize("hasAuthority('admin')")
	@ResponseStatus(HttpStatus.CREATED)
	@GetMapping(value = "/AccountHolders/{id}/CDAccounts")
	public List<CDAccount> getCDAccountsbyId(@PathVariable int id) {
		return meritBankService.getCDAccountsbyId(id);
	}

	
    @PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@GetMapping(value = "/Me")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public AccountHolder getMyAccountInfo(HttpServletRequest request) {
		return meritBankService.getMyAccountInfo(request);
	}
	
	
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@GetMapping(value = "/Me/CheckingAccount")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public CheckingAccount getMyCheckingAccounts(HttpServletRequest request) {
		return meritBankService.getMyCheckingAccounts(request);
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/Me/CheckingAccount")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public CheckingAccount postMyCheckingAccount(HttpServletRequest request,@Valid @RequestBody CheckingAccount checkingAccount)
			throws ExceedsCombinedBalanceLimitException, TooManyAccountsException {
		
		return meritBankService.postMyCheckingAccount(request, checkingAccount);
	}

	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/Me/SavingsAccounts")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public SavingsAccount postMySavingsAccounts(HttpServletRequest request, @Valid @RequestBody SavingsAccount savingsAccount) 
			throws ExceedsCombinedBalanceLimitException, TooManyAccountsException{
		return meritBankService.postMySavingsAccount(request, savingsAccount);
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/Me/IRA")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public IRA postMyIRA(HttpServletRequest request, @Valid @RequestBody IRA ira) 
			throws ExceedsCombinedBalanceLimitException, TooManyAccountsException{
		return meritBankService.postMyIRA(request, ira);
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@GetMapping(value = "/Me/IRA")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public IRA getMyIRA(HttpServletRequest request) 
			throws ExceedsCombinedBalanceLimitException{
		return meritBankService.getMyIRA(request);
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/Me/RothIRA")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public RothIRA postMyIRA(HttpServletRequest request, @Valid @RequestBody RothIRA RothIRA) 
			throws ExceedsCombinedBalanceLimitException, TooManyAccountsException{
		return meritBankService.postMyRothIRA(request, RothIRA);
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/Me/RolloverIRA")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public RolloverIRA postMyIRA(HttpServletRequest request, @Valid @RequestBody RolloverIRA RolloverIRA) 
			throws ExceedsCombinedBalanceLimitException, TooManyAccountsException{
		return meritBankService.postMyRolloverIRA(request, RolloverIRA);
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@GetMapping(value = "/Me/RothIRA")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public RothIRA getMyRothIRA(HttpServletRequest request) 
			throws ExceedsCombinedBalanceLimitException{
		return meritBankService.getMyRothIRA(request);
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@GetMapping(value = "/Me/RolloverIRA")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public RolloverIRA getMyRolloverIRA(HttpServletRequest request) 
			throws ExceedsCombinedBalanceLimitException{
		return meritBankService.getMyRolloverIRA(request);
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@GetMapping(value = "/Me/SavingsAccounts")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public SavingsAccount getMySavingsAccounts(HttpServletRequest request){
		return meritBankService.getMySavingsAccounts(request);
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/Me/CDAccounts")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public CDAccount postMyCDAccounts(HttpServletRequest request, @Valid @RequestBody CDAccount cDAccount)
	throws ExceedsCombinedBalanceLimitException, TooManyAccountsException {
		return meritBankService.postMyCDAccounts(request, cDAccount);
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@GetMapping(value = "/Me/CDAccounts")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public List<CDAccount> getMyCDAccounts(HttpServletRequest request) {
		return meritBankService.getMyCDAccount(request);
	}
	
	@PreAuthorize("hasAuthority('admin')")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/CDOfferings")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public CDOffering postCDOffering(@Valid @RequestBody CDOffering cdOffering) {
		return meritBankService.postCDOffering(cdOffering);
	}

	@PreAuthorize("hasAuthority('admin') or hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/CDOfferings")
	public List<CDOffering> getCDOfferings() {
		return meritBankService.getCDOfferings();
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/Me/DBACheckingAccount")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public DBAChecking postMyCheckingAccount(HttpServletRequest request,@Valid @RequestBody DBAChecking dbacheckingAccount)
			throws ExceedsCombinedBalanceLimitException, TooManyAccountsException {
		
		return meritBankService.postMyDBACheckingAccount(request, dbacheckingAccount);
	}
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@GetMapping(value = "/Me/DBACheckingAccount")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public List<DBAChecking> getMyDBACheckingAccounts(HttpServletRequest request) {
		return meritBankService.getMyDBACheckingAccounts(request);
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/Me/DBACheckingAccount/Deposit")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public BankAccount postMyDBACheckingDeposit(HttpServletRequest request
			,@Valid @RequestBody DepositTransaction deposit)
			throws ExceedsCombinedBalanceLimitException, NegativeBalanceException {
		
		return meritBankService.postMyDeposit(request, deposit, "DBACheckingAccount");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/Me/DBACheckingAccount/Deposit")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public List<Transaction> getMyDeposit() {
		return meritBankService.getMyDeposit("DBACheckingAccount");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/Me/CheckingAccount/Deposit")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public BankAccount postMyCheckingDeposit(HttpServletRequest request,
			@Valid @RequestBody DepositTransaction deposit)
		throws ExceedsCombinedBalanceLimitException, NegativeBalanceException{
		return meritBankService.postMyDeposit(request, deposit, "CheckingAccount");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/Me/CheckingAccount/Deposit")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public List<Transaction> getMyCheckingDeposit(){
		return meritBankService.getMyDeposit("CheckingAccount");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/Me/SavingsAccount/Deposit")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public BankAccount postMySavingsDeposit(HttpServletRequest request,
			@Valid @RequestBody DepositTransaction deposit)
		throws ExceedsCombinedBalanceLimitException, NegativeBalanceException{
		return meritBankService.postMyDeposit(request, deposit, "SavingsAccount");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/Me/SavingsAccount/Deposit")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public List<Transaction> getMySavingsDeposit(){
		return meritBankService.getMyDeposit("SavingsAccount");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/Me/CDAccount/Deposit")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public BankAccount postMyCDAccountDeposit(HttpServletRequest request,
			@Valid @RequestBody DepositTransaction deposit)
		throws ExceedsCombinedBalanceLimitException, NegativeBalanceException{
		return meritBankService.postMyDeposit(request, deposit, "CDAccount");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/Me/CDAccount/Deposit")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public List<Transaction> getMyCDAccountDeposit(){
		return meritBankService.getMyDeposit("CDAccount");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/Me/IRA/Deposit")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public BankAccount postMyIRADeposit(HttpServletRequest request,
			@Valid @RequestBody DepositTransaction deposit)
		throws ExceedsCombinedBalanceLimitException, NegativeBalanceException{
		return meritBankService.postMyDeposit(request, deposit, "IRA");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/Me/IRA/Deposit")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public List<Transaction> getMyIRADeposit(){
		return meritBankService.getMyDeposit("IRA");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/Me/RothIRA/Deposit")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public BankAccount postMyRothIRADeposit(HttpServletRequest request,
			@Valid @RequestBody DepositTransaction deposit)
		throws ExceedsCombinedBalanceLimitException, NegativeBalanceException{
		return meritBankService.postMyDeposit(request, deposit, "RothIRA");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/Me/RothIRA/Deposit")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public List<Transaction> getMyRothIRADeposit(){
		return meritBankService.getMyDeposit("RothIRA");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/Me/RolloverIRA/Deposit")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public BankAccount postMyRolloverIRADeposit(HttpServletRequest request,
			@Valid @RequestBody DepositTransaction deposit)
		throws ExceedsCombinedBalanceLimitException, NegativeBalanceException{
		return meritBankService.postMyDeposit(request, deposit, "RolloverIRA");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/Me/RolloverIRA/Deposit")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public List<Transaction> getMyRolloverIRADeposit(){
		return meritBankService.getMyDeposit("RolloverIRA");
	}
	
	//Withdraw 
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/Me/DBACheckingAccount/Withdraw")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public BankAccount postMyDBACheckingWithdraw(HttpServletRequest request
			,@Valid @RequestBody WithdrawTransaction withdraw)
			throws ExceedsCombinedBalanceLimitException, NegativeBalanceException {
		
		return meritBankService.postMyWithdrawl(request, withdraw, "DBACheckingAccount");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	@GetMapping(value = "/Me/DBACheckingAccount/Withdraw")
	public List<Transaction> getMyWithdrawl() {
		return meritBankService.getMyWithdrawl("DBACheckingAccount");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	@PostMapping(value = "/Me/CheckingAccount/Withdraw")
	public BankAccount postMyCheckingWithdrawl(HttpServletRequest request,
			@Valid @RequestBody WithdrawTransaction withdraw)
		throws ExceedsCombinedBalanceLimitException, NegativeBalanceException{
		return meritBankService.postMyWithdrawl(request, withdraw, "CheckingAccount");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	@GetMapping(value = "/Me/CheckingAccount/Withdraw")
	public List<Transaction> getMyCheckingWithdrawl(){
		return meritBankService.getMyWithdrawl("CheckingAccount");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	@PostMapping(value = "/Me/SavingsAccount/Withdraw")
	public BankAccount postMySavingsWithdrawl(HttpServletRequest request,
			@Valid @RequestBody WithdrawTransaction withdraw)
		throws ExceedsCombinedBalanceLimitException, NegativeBalanceException{
		return meritBankService.postMyWithdrawl(request, withdraw, "SavingsAccount");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	@GetMapping(value = "/Me/SavingsAccount/Withdraw")
	public List<Transaction> getMySavingsWithdrawl(){
		return meritBankService.getMyWithdrawl("SavingsAccount");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	@PostMapping(value = "/Me/CDAccount/Withdraw")
	public BankAccount postMyCDAccountWithdrawl(HttpServletRequest request,
			@Valid @RequestBody WithdrawTransaction withdraw)
		throws ExceedsCombinedBalanceLimitException, NegativeBalanceException{
		return meritBankService.postMyWithdrawl(request, withdraw, "CDAccount");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	@GetMapping(value = "/Me/CDAccount/Withdraw")
	public List<Transaction> getMyCDAccountWithdrawl(){
		return meritBankService.getMyWithdrawl("CDAccount");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	@PostMapping(value = "/Me/IRA/Withdraw")
	public BankAccount postMyIRAWithdrawl(HttpServletRequest request,
			@Valid @RequestBody WithdrawTransaction withdraw)
		throws ExceedsCombinedBalanceLimitException, NegativeBalanceException{
		return meritBankService.postMyWithdrawl(request, withdraw, "IRA");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	@GetMapping(value = "/Me/IRA/Withdraw")
	public List<Transaction> getMyIRAWithdrawl(){
		return meritBankService.getMyWithdrawl("IRA");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	@PostMapping(value = "/Me/RothIRA/Withdraw")
	public BankAccount postMyRothIRAWithdrawl(HttpServletRequest request,
			@Valid @RequestBody WithdrawTransaction withdraw)
		throws ExceedsCombinedBalanceLimitException, NegativeBalanceException{
		return meritBankService.postMyWithdrawl(request, withdraw, "RothIRA");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	@GetMapping(value = "/Me/RothIRA/Withdraw")
	public List<Transaction> getMyRothIRAWithdrawl(){
		return meritBankService.getMyWithdrawl("RothIRA");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	@PostMapping(value = "/Me/RolloverIRA/Withdraw")
	public BankAccount postMyRolloverIRAWithdrawl(HttpServletRequest request,
			@Valid @RequestBody WithdrawTransaction withdraw)
		throws ExceedsCombinedBalanceLimitException, NegativeBalanceException{
		return meritBankService.postMyWithdrawl(request, withdraw, "RolloverIRA");
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	@GetMapping(value = "/Me/RolloverIRA/Withdraw")
	public List<Transaction> getMyRolloverIRAWithdrawl(){
		return meritBankService.getMyWithdrawl("RolloverIRA");
	}
	
	///TRANSFERS 
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/Me/Transfer")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public List<BankAccount> postMyDBACheckingTransfer(HttpServletRequest request
			,@Valid @RequestBody TransferTransaction transfer)
			throws ExceedsCombinedBalanceLimitException, NegativeBalanceException, TransactionFailureException {
		return meritBankService.postMyTransfer(request, transfer);

	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/Me/Transfer")
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	public List<Transaction> getMyTransfer() {
		return meritBankService.getMyWithdrawl("DBACheckingAccount");
	}
	
}