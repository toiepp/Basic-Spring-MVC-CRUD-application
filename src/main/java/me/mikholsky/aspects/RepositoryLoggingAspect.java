package me.mikholsky.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;

@Aspect
@Component
@Order(1)
public class RepositoryLoggingAspect {
	private final static Logger logger = Logger.getLogger(RepositoryLoggingAspect.class.getName());

	private String mergeArgsToString(JoinPoint joinPoint) {
		StringBuilder sb = new StringBuilder();

		return String.join(", ",
						   Arrays.stream(joinPoint.getArgs()).map(Objects::toString).toList());
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
		logger.info(String.format("Execution of [%s] with such arguments {%s}",
								  joinPoint.getSignature().toShortString(),
								  mergeArgsToString(joinPoint)));
	}

	@AfterReturning(value = "execution(public !void me.mikholsky.repositories.*.*(..))",
					returning = "result")
	public void afterReturningMethodAdvice(JoinPoint joinPoint, Object result) {
		logger.info(String.format("Method [%s] returned {%s}",
								  joinPoint.getSignature().toShortString(),
								  result));
	}

	@Around(value = "execution(* me.mikholsky.repositories.*.*(..)) && excludeSetterMethodsPointcut()",
			argNames = "proceedingJoinPoint")
	public Object aroundSaveAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		long s = System.currentTimeMillis();

		Object result = proceedingJoinPoint.proceed();

		long e = System.currentTimeMillis();

		logger.info(String.format(
			"Execution duration of [%s] is %dms",
			proceedingJoinPoint.getSignature().toShortString(),
			e - s
		));

		return result;
	}
}
