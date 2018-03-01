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
package com.indracompany.sofia2.router.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.indracompany.sofia2.router.service.app.model.NotificationModel;
import com.indracompany.sofia2.router.service.app.model.OperationResultModel;
import com.indracompany.sofia2.router.service.app.service.RouterService;

import io.swagger.annotations.ApiOperation;

@RestController
@EnableAutoConfiguration
@CrossOrigin(origins = "*")
@RequestMapping("router")
public class RouterControllerImpl implements RouterControllerInterface, RouterService {
		
	@Autowired
	@Qualifier("routerServiceImpl")
	private RouterService routerService;
	
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	@ApiOperation(value = "insert")
	public OperationResultModel insert(@RequestBody NotificationModel model) throws Exception {
		return routerService.insert(model);
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	@ApiOperation(value = "update")
	public OperationResultModel update(@RequestBody NotificationModel model) throws Exception {
		return routerService.update(model);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@ApiOperation(value = "delete")
	public OperationResultModel delete(@RequestBody NotificationModel model) throws Exception {
		return routerService.delete(model);
	}

	@RequestMapping(value = "/query", method = RequestMethod.GET)
	@ApiOperation(value = "query")
	public OperationResultModel query(@RequestBody NotificationModel model) throws Exception {
		return routerService.query(model);
	}

	@RequestMapping(value = "/subscribe", method = RequestMethod.PUT)
	@ApiOperation(value = "subscribe")
	public OperationResultModel subscribe(@RequestBody NotificationModel model) throws Exception {
		return routerService.subscribe(model);
	}
	

}
