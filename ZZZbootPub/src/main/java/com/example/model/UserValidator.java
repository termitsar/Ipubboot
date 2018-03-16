package com.example.model;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.model.FileGroups;



@Component
public class UserValidator implements Validator {
	
	public boolean supports(Class<?> clazz) {
		return User.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		User user = (User) obj;
		
          
			if (user.getId() != 0) {    // если пользователь уже есть
				if (!user.getNewpassword().isEmpty())  // если поля пароля не пустое (значит хочет поменять)
				{
				    if (user.getNewpassword().length()<6) // если оно меньше 6 символов
				    {
				    	errors.rejectValue("newpassword", "missing.file","Пароль должен быть не менее 6 символов");
				    }
				    else	
				    {
				    	 if (!user.getNewpassword().equalsIgnoreCase(user.getNewpasswordagain()))
						    {
						    	errors.rejectValue("newpasswordagain", "missing.file","Повторный ввод пароля не совпадает с предыдущим полем!");
						    }
				    }
				}
			}
			else
			{
				if (user.getUsername()!=null)
				{
					if (user.getUsername().isEmpty()) 
				    {
				    	errors.rejectValue("username", "missing.file","Введите логин");
				    }
					
					if (user.getNewpassword().length()<6) // если оно меньше 6 символов
				    {
				    	errors.rejectValue("newpassword", "missing.file","Пароль должен быть не менее 6 символов");
				    }
				    else	
				    {
				    	 if (!user.getNewpassword().equalsIgnoreCase(user.getNewpasswordagain()))
						    {
						    	errors.rejectValue("newpasswordagain", "missing.file","Повторный ввод пароля не совпадает с предыдущим полем!");
						    }
				    }
					
					if (user.getEmail().isEmpty()) 
				    {
				    	errors.rejectValue("email", "missing.file","Введите электронную почту!");
				    }
				}	
			}

	}
}