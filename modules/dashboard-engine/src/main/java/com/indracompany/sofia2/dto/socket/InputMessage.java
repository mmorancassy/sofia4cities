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
package com.indracompany.sofia2.dto.socket;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputMessage {

    private String ds;
    private List<String> group;
    private List<FilterStt> filter;
    private List<ProjectStt> project;
    
    public InputMessage() {};
    
    public InputMessage (String ds, List<String> group, List<FilterStt> filter, List<ProjectStt> project) {
    	this.ds = ds;
    	this.group = group;
    	this.filter = filter;
    	this.project = project;
    }
}
