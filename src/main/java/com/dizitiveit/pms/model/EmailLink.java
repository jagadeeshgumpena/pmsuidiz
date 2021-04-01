package com.dizitiveit.pms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name="email_links")
public class EmailLink {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	  @Column(name="link_id")
	  private int linkId;
	
	@Column(name="value")
	private String value;
	
	@Column(name="link")
	private String link;

	public int getLinkId() {
		return linkId;
	}

	public void setLinkId(int linkId) {
		this.linkId = linkId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	
	
	

}
