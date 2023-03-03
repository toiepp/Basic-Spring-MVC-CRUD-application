package me.mikholsky.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;

@Aspect
@Component
public class ServiceLoggingAspect {
	private final static Logger logger = Logger.getLogger(RepositoryLoggingAspect.class.getName());

	private String mergeArgsToString(JoinPoint joinPoint) {
		StringBuilder sb = new StringBuilder();

		return String.join(", ",
						   Arrays.stream(joinPoint.getArgs()).map(Objects::toString).toList());
	}

	/**
	 * applies for each method from all services
	 */
	@Pointcut("execution(* me.mikholsky.services.*.*(..))")
	public void forAllMethodsPointCut() {
	}

	@Pointcut("!execution(public void me.mikholsky.services.*.set*(..))")
	public void excludeSetterMethodsPointcut() {
	}

	@After("forAllMethodsPointCut() && excludeSetterMethodsPointcut()")
	public void afterAllMethodsAdvice(JoinPoint joinPoint) {
		logger.info(String.format("Execution of [%s] with such arguments {%s}",
								  joinPoint.getSignature().toShortString(),
								  mergeArgsToString(joinPoint)));
	}

	@AfterReturning(value = "execution(public !void me.mikholsky.services.*.*(..))",
					returning = "result")
	public void afterReturningMethodAdvice(JoinPoint joinPoint, Object result) {
		logger.info(String.format("Method [%s] returned {%s}",
								  joinPoint.getSignature().toShortString(),
								  result));
	}

	@Around(value = "execution(* me.mikholsky.services.*.*(..)) && excludeSetterMethodsPointcut()",
			argNames = "proceedingJoinPoint")
	public Object measureExecutionDuration(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Long start = System.currentTimeMillis();

		Object result = proceedingJoinPoint.proceed();

		Long end = System.currentTimeMillis();

		logger.info(String.format("Execution time of [%s] is %dms",
								  proceedingJoinPoint.getSignature().toShortString(),
								  (end - start)));

		return result;
	}

	@Around(value = "execution(* me.mikholsky.services.*.*(..)) && excludeSetterMethodsPointcut()")
	public Object logException(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		try {
			return proceedingJoinPoint.proceed();
		} catch (Throwable e) {
			logger.warning(String.format("Exception in [%s] has been thrown: [%s]",
										 proceedingJoinPoint.getSignature().toShortString(),
										 e));
			throw e;
		}
	}
}
