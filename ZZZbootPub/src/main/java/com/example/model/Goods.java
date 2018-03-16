package com.example.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import com.example.model.Order.DeliveryGroup;
import com.fasterxml.jackson.annotation.JsonManagedReference;




@Entity
@Table(name = "goods")
public class Goods implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	@Size(min=1, message="Название товара не должно быть пустым!")
	String name;

	String description;

	int group_id;
	
	
	@OneToMany(mappedBy = "good",fetch=FetchType.EAGER, cascade = CascadeType.ALL,  orphanRemoval = true)
	@JsonManagedReference
	List<Picturesgoods> pictures;
	
	@Transient
	double price;
	
	@Transient
	Picturesgoods primPicture;

	@Transient
	int id_primPicture;
	
		
	public int getId_primPicture() {
		return id_primPicture;
	}

	public void setId_primPicture(int id_primPicture) {
		this.id_primPicture = id_primPicture;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getGroup_id() {
		return group_id;
	}

	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}

	public List<Picturesgoods> getPictures() {
		return pictures;
	}

	public void setPictures(List<Picturesgoods> pictures) {
		this.pictures = pictures;
	}

	public Picturesgoods getPrimPicture() {
		return primPicture;
	}

	public void setPrimPicture(Picturesgoods primPicture) {
		this.primPicture = primPicture;
	}
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Goods other = (Goods) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	
	
}
