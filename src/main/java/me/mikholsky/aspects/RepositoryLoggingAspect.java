package me.mikholsky.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.processor.SpringErrorClassTagProcessor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Aspect
@Component
@Order(1)
public class RepositoryLoggingAspect {
	private String getCurrentTime() {
		return DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SS").format(LocalDateTime.now());
	}

	private String printAllArguments(Object[] args) {
		StringBuilder sb = new StringBuilder();
		Stream.of(args).forEach(s -> sb.append(s).append(' '));
		return sb.toString();
	}

	/**
	 * applies for each method from all repositories
	 */
	@Pointcut("execution(* me.mikholsky.repositories.*.*(..))")
	public void forAllMethodsPointCut() {
	}

	@Pointcut("!execution(public void me.mikholsky.repositories.*.set*(..))")
	public void excludeSetterMethodsPointcut() {
	}

	@After("forAllMethodsPointCut() && excludeSetterMethodsPointcut()")
	public void afterAllMethodsAdvice(JoinPoint joinPoint) {
		System.out.println(getCurrentTime() + " Execution of [" + joinPoint.getSignature().toLongString() +
							   "] with such arguments {" + printAllArguments(joinPoint.getArgs()) + "}");
	}

	@AfterReturning(value = "execution(public !void me.mikholsky.repositories.*.*(..))",
					returning = "result")
	public void afterReturningMethodAdvice(JoinPoint joinPoint, Object result) {
		System.out.println(getCurrentTime() + " Method [" + joinPoint.getSignature().toLongString() + "] returned {" + result + "}");

	}
}
