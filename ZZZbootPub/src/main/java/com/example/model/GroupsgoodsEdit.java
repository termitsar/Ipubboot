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


public class GroupsgoodsEdit  {

int id_group;
String new_name="";
boolean newgood; 
List<Groupsgoods> grlev1;
int newparent;
String error="";


public String getError() {
	return error;
}
public void setError(String error) {
	this.error = error;
}
public int getNewparent() {
	return newparent;
}
public void setNewparent(int newparent) {
	this.newparent = newparent;
}
public List<Groupsgoods> getGrlev1() {
	return grlev1;
}
public void setGrlev1(List<Groupsgoods> grlev1) {
	this.grlev1 = grlev1;
}
public boolean isNewgood() {
	return newgood;
}
public void setNewgood(boolean newgood) {
	this.newgood = newgood;
}
public int getId_group() {
	return id_group;
}
public void setId_group(int id_group) {
	this.id_group = id_group;
}
public String getNew_name() {
	return new_name;
}
public void setNew_name(String new_name) {
	this.new_name = new_name;
}



}
