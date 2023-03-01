package me.mikholsky.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
public class RepositoryLoggingAspect {
	private String getCurrentTime() {
		return DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SS").format(LocalDateTime.now());
	}

	/**
	 * applies for each method from all repositories
	 */
	@Pointcut("execution(* me.mikholsky.repositories.*.*(..))")
	public void forAllMethodsPointCut() {
	}

	@Pointcut("!execution(public void me.mikholsky.repositories.*.set*(..))")
	public void excludeSetterMethodsPointcut() {}

	@After("forAllMethodsPointCut() && excludeSetterMethodsPointcut()")
	public void afterAllMethodsAdvice(JoinPoint joinPoint) {
		System.out.println(getCurrentTime() + " Execution of [" + joinPoint.getSignature().toLongString() + "]");
	}


}
