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
/*******************************************************************************
 * © Indra Sistemas, S.A.
 * 2013 - 2018  SPAIN
 *
 * All rights reserved
 ******************************************************************************/

package com.indracompany.sofia2.config.repository;

import java.util.Calendar;
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

import com.indracompany.sofia2.config.model.ClientConnection;
import com.indracompany.sofia2.config.model.ClientPlatform;

import lombok.extern.slf4j.Slf4j;


/**
 *
 * @author Javier Gomez-Cornejo
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j

public class ClientConnectionIntegrationTest {

	@Autowired 
	ClientConnectionRepository repository;
	@Autowired 
	ClientPlatformRepository clientRep;

	@Before
	public void setUp() {
		List<ClientConnection> clients= this.repository.findAll();
		if (clients.isEmpty()) {
			log.info("No clients ...");
			ClientConnection con= new ClientConnection();
			ClientPlatform client= clientRep.findById("1");
			con.setClientPlatformId(clientRep.findById("1"));			
			con.setIdentification(client.getIdentification()+"-1");
			con.setIpStrict(true);
			con.setStaticIp(false);
			con.setLastIp("192.168.1.89");
			Calendar date = Calendar.getInstance();
			con.setLastConnection(date);
			repository.save(con);
		}
	}
	@Test
	public void test_CountByClientPlatformId() { 
		ClientConnection con=this.repository.findAll().get(0);

		Assert.assertTrue(this.repository.countByClientPlatformId(con.getClientPlatformId())>0);
	}

	@Test
	public void test_FindByUserId(){ 

		ClientConnection con=this.repository.findAll().get(0);

		Assert.assertTrue(this.repository.findByUserId(con.getClientPlatformId().getUserId().getUserId()).size()>0);
	}

	@Test
	public void test_FindByClientPlatformId(){ 
		ClientConnection con=this.repository.findAll().get(0);

		Assert.assertTrue(this.repository.findByClientPlatformId(con.getClientPlatformId()).size()>0);
	}

	@Test
	public void test_FindByClientPlatformIdAndIdentification(){ 
		ClientConnection con=this.repository.findAll().get(0);
		Assert.assertTrue(this.repository.findByClientPlatformIdAndIdentification(con.getClientPlatformId(),con.getIdentification()).size()>0);
	}

}



