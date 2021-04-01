
  package com.dizitiveit.pms.model;
  
  import java.util.Date;
import java.util.UUID;

import javax.persistence.Column; 
  import javax.persistence.Entity; 
  import javax.persistence.GeneratedValue; 
  import javax.persistence.GenerationType;
  import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
  import javax.persistence.Table; 
  import javax.persistence.Temporal;
  import javax.persistence.TemporalType;
  
  import javax.persistence.Id;

  
  @Entity 
  @Table(name="confirmation_token")
  public class ConfirmationToken {
	  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name="token_id") 
  private long tokenId;
  
  @Column(name="confirmation_token") 
  private String confirmationToken;
  
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;
  
  @ManyToOne
	@JoinColumn(name="user_id")
	private  Users users;
  
  public ConfirmationToken(Users users) {
      this.users = users;
      createdDate = new Date();
      confirmationToken = UUID.randomUUID().toString();
  }
  
  public ConfirmationToken() {
	// TODO Auto-generated constructor stub
}

public long getTokenId() { 
	  return tokenId; 
	  }
  
  public void setTokenId(long tokenId) { 
	  this.tokenId = tokenId; 
	  }
  
  public String getConfirmationToken() { 
	  return confirmationToken; 
	  }
  
  public void setConfirmationToken(String confirmationToken) {
  this.confirmationToken = confirmationToken; 
  }
  
  public Date getCreatedDate() { 
	  return createdDate; 
	  }
  
  public void setCreatedDate(Date createdDate) {
	  this.createdDate = createdDate; 
	  }
   
  public Users getUsers() { 
	  return users; 
	  }
  
  public void setUsers(Users users) { 
	  this.users = users;
	  }
  
  
  
  }
 