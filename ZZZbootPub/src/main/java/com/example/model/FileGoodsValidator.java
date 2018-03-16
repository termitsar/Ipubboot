package com.example.model;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class FileGoodsValidator implements Validator {
	
	public boolean supports(Class<?> clazz) {
		return FileGoods.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		FileGoods file = (FileGoods) obj;
		
		if(file.getFile()!=null){
			if (file.getFile().getSize() == 0) {
				errors.rejectValue("file", "missing.file","Файл не выбран!");
			}
		}
	}
}