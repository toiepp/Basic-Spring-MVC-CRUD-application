package me.mikholsky.validation.required;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * For annotating field that have to be filled in HTML form */
@Constraint(validatedBy = RequiredConstrainValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Required {
	String message() default "This field is required";

	Class<?>[] groups() default  {};

	Class<Payload>[] payload() default {};
}
