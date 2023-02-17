package me.mikholsky.validation.required;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RequiredConstrainValidator implements ConstraintValidator<Required, String> {
	@Override
	public void initialize(Required constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value != null;
	}
}
