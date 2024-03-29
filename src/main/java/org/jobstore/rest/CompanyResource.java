package org.jobstore.rest;

import java.util.List;

import org.jobstore.domain.Company;
import org.jobstore.repository.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/companies")
public class CompanyResource {

	@Autowired
	private CompanyRepository companyRepository;
	
	private Logger logger = LoggerFactory.getLogger(CompanyResource.class);

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Company> createNewCompany(@RequestBody Company company) {
		logger.debug("inside createNewCompany().. creating new company {}" , company);
		Company existingCompany = companyRepository.findByName(company.getName());
		if(existingCompany != null){
			logger.debug("Company with name {} already exists : {}" , company.getName(), existingCompany);
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		company = companyRepository.save(company);
		logger.info("Created new company {}" , company);
		return new ResponseEntity<>(company,HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Company> showAll(){
		List<Company> companies = companyRepository.findAll();
		logger.info("Found {} companies" , companies.size());
		return companies;
	}
	
	@RequestMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Company showCompany(@RequestParam("id") Long id){
		return companyRepository.findOne(id);
	}
	
}
