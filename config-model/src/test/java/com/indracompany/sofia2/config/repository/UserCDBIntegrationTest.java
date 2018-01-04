package com.indracompany.sofia2.config.repository;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.indracompany.sofia2.config.model.UserCDB;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Luis Miguel Gracia
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class UserCDBIntegrationTest {
	
	@Autowired
	UserCDBRepository repository;
		
	@Before
	public void setUp() {
		List<UserCDB> types = this.repository.findAll();
		if (types.isEmpty()) {
			log.info("No types en tabla.Adding...");
			throw new RuntimeException("No types en tabla.Adding...");
		}
	}


	@Test
	public void test1_Count() { 
		Assert.assertTrue(this.repository.count()==6);		
	}

	@Test
	public void test3_FindUserNoAdmin() { 
		Assert.assertTrue(this.repository.findUsersNoAdmin().size()==5L);		
	}

	@Test
	public void test4_FindByEmail() { 
		Assert.assertTrue(this.repository.findByEmail("administrator@sofia2.com").size()==1L);		
	}


}
