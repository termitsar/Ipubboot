package com.example.model;

import org.springframework.web.multipart.MultipartFile;

public class FileGroups {

	MultipartFile file;
	
	String conclusion="";
	
	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}
	
	
	
}
