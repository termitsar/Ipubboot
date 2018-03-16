package com.example.model;

import java.io.Serializable;

import java.util.List;

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

@Entity
@Table(name = "groupsgoods")
public class Groupsgoods implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	
	String name;

	@ManyToOne() // add column definitions as needed
	//@JoinColumn(name = "parent_id")
	private Groupsgoods parent; // each Domain with parent==null is a root
				

	@OneToMany(mappedBy = "parent", fetch=FetchType.EAGER) // add column definitions as needed
	//@JoinColumn(name = "parent_id" )
	
	private List<Groupsgoods> subgroup;

	
	

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

	public Groupsgoods getParent() {
		return parent;
	}

	public void setParent(Groupsgoods parent) {
		this.parent = parent;
	}

	public List<Groupsgoods> getSubgroup() {
		return subgroup;
	}

	public void setSubgroup(List<Groupsgoods> subgroup) {
		this.subgroup = subgroup;
	}

	
	
}
