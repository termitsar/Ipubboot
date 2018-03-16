package com.example.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Table(name = "order_goods")
public class Order implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int    id;
	int id_user;
	
	String fam;
	String im;
	String ot;
	String email;
	String phone;
	String nomdoc;
	String street;
	String sity;
	String region;
	@Column(name="indexx")
	String index;
	
	@Transient
	Boolean agree;	
	
	String deliver="1";
	double deliver_price=0.0;
	
	String payment;

	String Comment;

	@Fetch(value = FetchMode.SUBSELECT)
	@JsonManagedReference
	@OneToMany(mappedBy = "order",fetch=FetchType.EAGER, cascade = { CascadeType.ALL},  orphanRemoval = true)
	List<GoodsBasket> goodsBasket = new ArrayList<GoodsBasket>();
	
	@Transient
	int total_quantity = goodsBasket.size();
	@Transient
	double total_price;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "cr_date", updatable=false,insertable=false)
	private Date cr_date; 
	
	@OneToOne
    @JoinColumn(name = "state")
	StateOrder state_order;
	
	@Transient
	List<StateOrder> liststate;
	
	public Order() {	}
	
	
	
	
	public List<StateOrder> getListstate() {
		return liststate;
	}

	public void setListstate(List<StateOrder> liststate) {
		this.liststate = liststate;
	}

	public Date getCr_date() {
		return cr_date;
	}

	public void setCr_date(Date cr_date) {
		this.cr_date = cr_date;
	}

	public StateOrder getState_order() {
		return state_order;
	}

	public void setState_order(StateOrder state_order) {
		this.state_order = state_order;
	}

	public int getId_user() {
		return id_user;
	}

	public void setId_user(int id_user) {
		this.id_user = id_user;
	}

	
	public double getDeliver_price() {
		deliver_price= 0.0;
		if (deliver.equals("1")) deliver_price= 0.0;
		if (deliver.equals("2")) deliver_price= 350.0;
		if (deliver.equals("3")) deliver_price= 2000.0;
		return deliver_price;
	}


//	public double getDeliver_price() {
//		return deliver_price;
//	}

	public void setDeliver_price(double deliver_price) {
		this.deliver_price = deliver_price;
	}

	public String getComment() {
		return Comment;
	}

	public void setComment(String comment) {
		Comment = comment;
	}

	public int getTotal_quantity() {
		return total_quantity;
	}

	public void setTotal_quantity(int total_quantity) {
		this.total_quantity = total_quantity;
	}

	public double getTotal_price() {
		return total_price;
	}

	public void setTotal_price(double total_price) {
		this.total_price = total_price;
	}

	public List<GoodsBasket> getGoodsBasket() {
		return goodsBasket;
	}

	public void setGoodsBasket(List<GoodsBasket> goodsBasket) {
		this.goodsBasket = goodsBasket;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@NotEmpty(message="Поле Фамилия должно быть заполнено!", groups={ContactGroup.class})
	public String getFam() {
		return fam;
	}
	public void setFam(String fam) {
		this.fam = fam;
	}
	
	@NotEmpty(message="Поле Имя должно быть заполнено!", groups={ContactGroup.class})
	public String getIm() {
		return im;
	}
	public void setIm(String im) {
		this.im = im;
	}
	
	@NotEmpty(message="Поле Отчество должно быть заполнено!", groups={ContactGroup.class})
	public String getOt() {
		return ot;
	}
	public void setOt(String ot) {
		this.ot = ot; 
	}
	
	@NotEmpty(message="Поле Электронная почта должно быть заполнено!", groups={ContactGroup.class})
	//@Email
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@Pattern(regexp="[0-9()+]{6,15}", message = "Номер должен быть не менее 6 цифр и не более 15", groups={ContactGroup.class})
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNomdoc() {
		return nomdoc;
	}

	public void setNomdoc(String nomdoc) {
		this.nomdoc = nomdoc;
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

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public Boolean getAgree() {
		return agree;
	}

	public void setAgree(Boolean agree) {
		this.agree = agree;
	}

	@NotEmpty(message="Выберите метод доставки заказа!", groups={DeliveryGroup.class})
	public String getDeliver() {
		return deliver;
	}

	public void setDeliver(String deliver) {
		this.deliver = deliver;
	}

	
	@NotEmpty(message="Выберите вариант оплаты заказа!", groups={PaymentGroup.class})	
	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}



 interface ContactGroup	{}
 interface DeliveryGroup {}
 interface PaymentGroup {}
	
}
