package com.example.model;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.model.FileGroups;


@Component
public class FileGroupsValidator implements Validator {
	
	public boolean supports(Class<?> clazz) {
		return FileGroups.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		FileGroups file = (FileGroups) obj;
		
		if(file.getFile()!=null){
			if (file.getFile().getSize() == 0) {
				errors.rejectValue("file", "missing.file","Файл не выбран!");
			}
		}
	}
}