package com.example.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name = "USERS")
public class User   implements Serializable {
	private int id;
	private String username;
	private String password;
	private String newpassword;
	private String newpasswordagain;
	
	private String fam;
	private String im;
	private String ot;
	private String email;
	private String phone;
	private String street;
	private String sity;
	private String region;
	@Column(name="indexx")
	private String indexx;
	

	private Date cr_date; 

	
	List<Roles> Role_list;
	List<Roles> Role_list_full;
		
	private String previous_url;
	

	
	@Transient	
	public String getPrevious_url() {
		return previous_url;
	}

	public void setPrevious_url(String previous_url) {
		this.previous_url = previous_url;
	}

	@Transient
	public String getNewpassword() {
		return newpassword;
	}

	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}
	@Transient
	public String getNewpasswordagain() {
		return newpasswordagain;
	}

	public void setNewpasswordagain(String newpasswordagain) {
		this.newpasswordagain = newpasswordagain;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	public int getId() {
		return id;
	}
	
	@Transient
	public List<Roles> getRole_list_full() {
		return Role_list_full;
	}

	public void setRole_list_full(List<Roles> role_list_full) {
		Role_list_full = role_list_full;
	}

	@Fetch(value = FetchMode.SUBSELECT)
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH })
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	@OrderBy(value="id ASC")
	public List<Roles> getRole_list() {
		return Role_list;
	}

	public void setRole_list(List<Roles> role_list) {
		Role_list = role_list;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

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

	public String getFam() {
		return fam;
	}

	public void setFam(String fam) {
		this.fam = fam;
	}

	public String getIm() {
		return im;
	}

	public void setIm(String im) {
		this.im = im;
	}

	public String getOt() {
		return ot;
	}

	public void setOt(String ot) {
		this.ot = ot;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getSity() {
		return sity;
	}

	public void setSity(String sity) {
		this.sity = sity;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getIndexx() {
		return indexx;
	}

	public void setIndexx(String indexx) {
		this.indexx = indexx;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "current_date", updatable=false,insertable=false)
	public Date getCr_date() {
		return cr_date;
	}

	public void setCr_date(Date cr_date) {
		this.cr_date = cr_date;
	}

	

	
}
