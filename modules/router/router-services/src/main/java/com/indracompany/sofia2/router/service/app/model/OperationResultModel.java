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
package com.indracompany.sofia2.router.service.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


public class OperationResultModel implements Serializable{
	
	
	@Getter
	@Setter
	private String result;

	@Getter
	@Setter
	private String message;
	
	@Getter
	@Setter
	private String errorCode;
	
	@Getter
	@Setter
	private String operation;
	
	@Getter
	@Setter
	private boolean status;

	@Override
	public String toString() {
		return "OperationResultModel [result=" + result + ", message=" + message + ", errorCode=" + errorCode
				+ ", operation=" + operation + "]";
	}

	
	
}