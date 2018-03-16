package com.example.model;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OrderValidatorImp implements ConstraintValidator<OrderValidator, Order> {

    public void initialize(OrderValidator OrderValidator) {
    }

    public boolean isValid(Order order, ConstraintValidatorContext context) {
	
	return false;
    }

}
