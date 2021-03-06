/**
 * Copyright Indra Sistemas, S.A.
 * 2013-2018 SPAIN
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.indracompany.sofia2.router.service.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Order // for many aspects order.
@Component
@Slf4j
public class MetricsAspect extends BaseAspect {
	
	 private final CounterService counterService;
	 
	 @Autowired
	 public MetricsAspect(CounterService counterService) {
		 this.counterService = counterService;
	 }

	@Around(value = "execution(* com.indracompany.sofia2.router.service.controller.*.*(..))")
	public Object processTx(ProceedingJoinPoint joinPoint) throws java.lang.Throwable {
		
		counterService.increment("counter.calls."+getMethod(joinPoint)+"."+joinPoint.getSignature());

		log.info("Controller @Around for " + getMethod(joinPoint) + " Interceptor Call : " + joinPoint.getSignature());
		
		final long start = System.currentTimeMillis();
		Object proceed = null;

		try {
			proceed = joinPoint.proceed();

		} finally {
			updateStats(getClassName(joinPoint), getMethod(joinPoint).getName(), (System.currentTimeMillis() - start));
		}

		final long executionTime = System.currentTimeMillis() - start;
		log.info("Controller @Around for " + getMethod(joinPoint) + " Interceptor Called : "	+ joinPoint.getSignature() + " executed in " + executionTime + "ms");

		return proceed;
	}

	@Before("execution(* com.indracompany.sofia2.router.service.controller.*.*(..))")
	public void beforeSampleCreation(JoinPoint joinPoint) {

		//counterService.increment("counter.calls.beforeSampleCreation");
		log.info("Controller @Before for " + getMethod(joinPoint) + " Method Invoked: "+ joinPoint.getSignature().getName());

		if (joinPoint.getArgs() != null) {
			int size = joinPoint.getArgs().length;
			if (size > 0) {
				log.info("Controller @Before for " + getMethod(joinPoint) + " Arguments Passed: "
						+ joinPoint.getArgs()[0]);
				
			}
		}
	}

	@AfterReturning(pointcut = "execution(* com.indracompany.sofia2.router.service.controller.*.*(..))", returning = "retVal")
	public void logServiceAccess(JoinPoint joinPoint, Object retVal) {
		
		//counterService.increment("counter.calls.logServiceAccess");
		
		log.info("Controller @AfterReturning for " + getMethod(joinPoint) + "  Completed: " + joinPoint);
		
		if (retVal != null) {
			log.info("Controller @AfterReturning for " + getMethod(joinPoint) + " Returned: " + retVal.toString());
		}

	}

	@AfterThrowing(pointcut = "execution(* com.indracompany.sofia2.router.service.controller.*.*(..))", throwing = "ex")
	public void doRecoveryActions(JoinPoint joinPoint, Exception ex) {
		
		//counterService.increment("counter.calls.doRecoveryActions");
		
		log.info("Controller @AfterThrowing Setter Called");
		log.info("Controller @AfterThrowing for " + getMethod(joinPoint) + " Method Invoked: "+ joinPoint.getSignature().getName());
		log.info("Controller @AfterThrowing for " + getMethod(joinPoint) + " Exception Message: " + ex.getMessage());
		log.info("Controller @AfterThrowing for " + getMethod(joinPoint) + " Exception Class: "
				+ ex.getClass().getName());

		
	}

}