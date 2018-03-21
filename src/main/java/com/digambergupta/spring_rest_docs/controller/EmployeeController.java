package com.digambergupta.spring_rest_docs.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


/**
 * EmployeeController class for employee details
 * @author Digamber Gupta
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@GetMapping
	public List<Employees> getEmpoyeeDetails(@RequestBody @Valid Employees employees) {
		List<Employees> returnList = new ArrayList<>();
		returnList.add(employees);
		return returnList;
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public HttpHeaders save(@RequestBody @Valid Employees employees) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(linkTo(EmployeeController.class).slash(employees.getId()).toUri());
		return httpHeaders;
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	HttpHeaders delete(@PathVariable("id") Integer id) {
		return new HttpHeaders();
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	void put(@PathVariable("id") Integer id, @RequestBody Employees employees) {

	}

	@PatchMapping("/{id}")
	public List<Employees> patch(@PathVariable("id") Integer id, @RequestBody Employees employees) {
		List<Employees> returnList = new ArrayList<>();
		employees.setId(id);
		returnList.add(employees);
		return returnList;
	}
}