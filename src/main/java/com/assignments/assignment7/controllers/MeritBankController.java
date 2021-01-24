
package com.assignments.assignment7.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import com.assignments.assignment7.models.CDAccount;
import com.assignments.assignment7.models.CDOffering;
import com.assignments.assignment7.models.CheckingAccount;
import com.assignments.assignment7.models.DBAChecking;
import com.assignments.assignment7.models.SavingsAccount;
import com.assignments.assignment7.models.SignupRequest;
import com.assignments.assignment7.services.MeritBankService;
import com.assignments.assignment7.services.MyUserDetailsService;
import com.assignments.assignment7.util.JwtUtil;

import Exceptions.AccountNotFoundException;
import Exceptions.ExceedsCombinedBalanceLimitException;
import Exceptions.ToManyAccountsException;

@RestController
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

	@PreAuthorize("hasAuthority('admin')")
	@PostMapping("/authenticate/createUser")
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
			@PathVariable Integer id) throws ExceedsCombinedBalanceLimitException, ToManyAccountsException {
		return meritBankService.postDBACheckingAccount(checkingAccount, id);
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
			throws AccountNotFoundException, ExceedsCombinedBalanceLimitException {
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
	public AccountHolder getMyAccountInfo(HttpServletRequest request) {
		return meritBankService.getMyAccountInfo(request);
	}
	
	
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@GetMapping(value = "/Me/CheckingAccount")
	public CheckingAccount getMyCheckingAccounts(HttpServletRequest request) {
		return meritBankService.getMyCheckingAccounts(request);
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/Me/CheckingAccount")
	public CheckingAccount postMyCheckingAccount(HttpServletRequest request,@Valid @RequestBody CheckingAccount checkingAccount)
			throws ExceedsCombinedBalanceLimitException {
		
		return meritBankService.postMyCheckingAccount(request, checkingAccount);
	}

	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/Me/SavingsAccounts")
	public SavingsAccount postMySavingsAccounts(HttpServletRequest request, @Valid @RequestBody SavingsAccount savingsAccount) 
			throws ExceedsCombinedBalanceLimitException{
		return meritBankService.postMySavingsAccount(request, savingsAccount);
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@GetMapping(value = "/Me/SavingsAccounts")
	public SavingsAccount getMySavingsAccounts(HttpServletRequest request){
		return meritBankService.getMySavingsAccounts(request);
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/Me/CDAccounts")
	public CDAccount postMyCDAccounts(HttpServletRequest request, @Valid @RequestBody CDAccount cDAccount)
	throws ExceedsCombinedBalanceLimitException {
		return meritBankService.postMyCDAccounts(request, cDAccount);
	}
	
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@GetMapping(value = "/Me/CDAccounts")
	public List<CDAccount> getMyCDAccounts(HttpServletRequest request) {
		return meritBankService.getMyCDAccount(request);
	}
	
	@PreAuthorize("hasAuthority('admin')")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/CDOfferings")
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
	public DBAChecking postMyCheckingAccount(HttpServletRequest request,@Valid @RequestBody DBAChecking dbacheckingAccount)
			throws ExceedsCombinedBalanceLimitException, ToManyAccountsException {
		
		return meritBankService.postMyDBACheckingAccount(request, dbacheckingAccount);
	}
	@PreAuthorize("hasAuthority('AccountHolder')")
	@ResponseStatus(HttpStatus.CREATED)
	@GetMapping(value = "/Me/CDAccounts")
	public List<DBAChecking> getMyDBACheckingAccounts(HttpServletRequest request) {
		return meritBankService.getMyDBACheckingAccounts(request);
	}
	

}