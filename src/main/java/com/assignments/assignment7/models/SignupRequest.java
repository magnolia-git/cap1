package com.assignments.assignment7.models;


import java.time.LocalDate;
import java.util.Set;

import javax.validation.constraints.*;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
     
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    
    @NotBlank
    @Size(min = 2, max = 25)
    private String firstName;
    
    @NotBlank
    @Size(min = 2, max = 25)
    private String lastName;
    
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate birthDate;
    
    @NotBlank
    @Size(min = 2, max = 25)
    private String ssn;
    
    private Set<String> role;
    
    private boolean active;
  
    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
 
//    public String getEmail() {
//        return email;
//    }
// 
//    public void setEmail(String email) {
//        this.email = email;
//    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public Set<String> getRole() {
      return this.role;
    }
    
    public void setRole(Set<String> role) {
      this.role = role;
    }

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}