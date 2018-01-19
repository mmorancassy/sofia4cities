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
package com.indracompany.sofia2.controlpanel.controller.ontologies;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.indracompany.sofia2.config.model.Ontology;

import com.indracompany.sofia2.config.repository.OntologyRepository;



@RequestMapping("/ontologies")
@Controller
public class OntologyController {
	
	@Autowired
	private OntologyRepository ontologyRepository;
	
	
	@RequestMapping(value = "/list" , produces = "text/html")
	public String list(Model uiModel,HttpServletRequest request)
	{
		//Get params
		String identification = request.getParameter("identification");
		String description = request.getParameter("description");
		//Scaping "" string values for parameters 
		if(identification!=null){if(identification.equals("")) identification=null;}
		if(description!=null){if(description.equals("")) description=null;}
		
		List<Ontology> ontologies;
		Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
		String userRole=authentication.getAuthorities().toArray()[0].toString();
		if(userRole.equals("ROLE_ADMINISTRATOR"))
		{
			if(description!=null && identification!=null){
				
				ontologies=this.ontologyRepository.findByIdentificationContainingAndDescriptionContaining(identification, description);
			
			}else if(description==null && identification!=null){
				
				ontologies=this.ontologyRepository.findByIdentificationContaining(identification);
				
			}else if(description!=null && identification==null){	
				
				ontologies=this.ontologyRepository.findByDescriptionContaining(description);
				
			}else{
				
				ontologies=this.ontologyRepository.findAll();
			}
		}else
		{
			if(description!=null && identification!=null){
				
				ontologies=this.ontologyRepository.findByUserIdAndIdentificationContainingAndDescriptionContaining(authentication.getName(),identification, description);
			
			}else if(description==null && identification!=null){
				
				ontologies=this.ontologyRepository.findByUserIdAndIdentificationContaining(authentication.getName(),identification);
				
			}else if(description!=null && identification==null){	
				
				ontologies=this.ontologyRepository.findByUserIdAndDescriptionContaining(authentication.getName(),description);
				
			}else{
				
				ontologies=this.ontologyRepository.findAll();
			}
		}
		uiModel.addAttribute("ontologies",ontologies);
		return "/ontologies/list";
	}
	
	

}
