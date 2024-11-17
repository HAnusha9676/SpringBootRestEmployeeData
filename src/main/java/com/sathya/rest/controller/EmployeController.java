package com.sathya.rest.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sathya.rest.exception.EmployeeNotFoundException;
import com.sathya.rest.model.Employee;
import com.sathya.rest.service.EmployeService;

import jakarta.validation.Valid;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins ="*")


public class EmployeController {
	@Autowired
	EmployeService employeeService;
	
	@GetMapping("/getnames")
	public List<String> getName(){
		return List.of("satya","ratan","anu");
	}
	 @GetMapping("/getname")
	    public List<String> getnames() {
	        return employeeService.getnames();
	    }
	 
	 
@PostMapping("/saveemp")
public ResponseEntity<Employee> saveEmployee(@Valid @RequestBody Employee employee) {
	 Employee savedEmp = employeeService.saveService(employee);
	 return ResponseEntity.status(HttpStatus.CREATED)
	                    .header("employee-status", "emp saved successfully")
	                    .body(savedEmp);
	}
	
	@PostMapping("/saveAll")
	public ResponseEntity<List<Employee>> saveAllEmployees(@RequestBody List<Employee> employees) {
	    List<Employee> emps= employeeService.saveAllService(employees);
	    return ResponseEntity.status(HttpStatus.CREATED)
	                         .header("employee", "allemps saved successfully")
	                         .body(emps);
	}
	
//	@GetMapping("/getall")
//	public ResponseEntity<List<Employee>> getAllEmployees(){
//		
//		List<Employee> allemps=employeeService.getAllEmployees();
//				return ResponseEntity.status(HttpStatus.OK)
//						.header("status", "data reading is ok")
//						.body(allemps);
//	}
	
	
	@GetMapping("/getall")
    public ResponseEntity<CollectionModel<EntityModel<Employee>>> getAllEmployees() {
        List<Employee> allemps = employeeService.getAllEmployees();

        List<EntityModel<Employee>> employeeModels = allemps.stream()
            .map(employee -> {
                EntityModel<Employee> entityModel = EntityModel.of(employee);
                
                // Add self link for each employee
                entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeController.class).getById(employee.getId())).withSelfRel());
                
                // Add link to update the employee
                entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeController.class).updateEmployeeById(employee.getId(), employee)).withRel("update"));
                
                // Add link to delete the employee
                entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeController.class).deleteEmployeeById(employee.getId())).withRel("delete"));
                
                return entityModel;
            })
            .collect(Collectors.toList());

        // Create a CollectionModel and add links
        CollectionModel<EntityModel<Employee>> collectionModel = CollectionModel.of(employeeModels);
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeController.class).getAllEmployees()).withSelfRel());
        
        return ResponseEntity.status(HttpStatus.OK)
                             .header("status", "data reading is ok")
                             .body(collectionModel);
    }


	
	
	
	
	
	
	
	//find by id
//	@GetMapping("/getbyid/{id}")
//	public ResponseEntity<?> getByID(@PathVariable Long id){
//		
//		Optional<Employee> optionalEmp=employeeService.getById(id);
//		
//		if( optionalEmp.isPresent()) {
//			return ResponseEntity.status(HttpStatus.OK)
//					.header("status by id", "data reading is ok by using id")
//					.body( optionalEmp.get());
//		}
//		else {
////			return ResponseEntity.status(HttpStatus.NOT_FOUND)
////					.body("Not found  "+id);
//			throw new EmployeeNotFoundException("data not found with id :"+id);
//			
//			
//			
//		}
//	}
	
	
	@GetMapping("/getbyid/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id)
	{	Optional<Employee> optionalEmp = employeeService.getById(id);
		if(optionalEmp.isPresent())
		{	
			Employee employee = optionalEmp.get();
			 // Create an EntityModel for the user
	        EntityModel<Employee> entityModel = EntityModel.of(employee);

	        // Add self link
	        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeController.class).getById(id)).withSelfRel());

	        // Add link to update the user
	        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeController.class).updateEmployeeById(id, employee)).withRel("update"));

	        // Add link to delete the user
	        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeController.class).deleteEmployeeById(id)).withRel("delete"));

	        // Add link to get all users
	        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeController.class).getAllEmployees()).withRel("all-users"));
			
			return ResponseEntity.status(HttpStatus.OK)
								 .body(entityModel);
		}
		else
		{	//return ResponseEntity.status(HttpStatus.NOT_FOUND)
				//				 .body("Emp is not found with Id.."+id);	
			throw new EmployeeNotFoundException("Employee not found with id "+id);
		}
	}
	
	
	
	//find by email
	
	@GetMapping("/getbyemail/{email}")
	public ResponseEntity<?> getByEmail(@PathVariable String email){
		
		Optional<Employee> optional=employeeService.getByEmail(email);
		
		if( optional.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK)
					.header("status by email", "data reading is ok by using email")
					.body( optional.get());
		}
		else {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND)
//					.body("Not found    "+email);
			
			throw new EmployeeNotFoundException("data not found with email "+email);
		}
	}
	//delete by id
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteEmployeeById(@PathVariable Long id) {
		boolean status=employeeService.deleteEmployeeById(id);
		
		if(status) {
			return ResponseEntity.noContent().build();
			
		}
		else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.header("status", "no data found")
			.body("no data found with id  "+id);
		}

	}
	
	//delete by email
	@DeleteMapping("/deletebyemail/{email}")
	public ResponseEntity<?> deleteEmployeeByEmail(@PathVariable String email){
boolean status=employeeService.deleteEmployeeByEmail(email);
		
		if(status) {
			return ResponseEntity.noContent().build();
			
		}
		else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.header("status", "no data found with give email")
			.body("no data found with email  "+email);
		}
	}
	
	//DELETE ALL
	@DeleteMapping("/deleteall")
public ResponseEntity<List<Employee>> deleteAllEmployees(){
		
		List<Employee> delEmps=employeeService.deleteAllEmployees();
		//return ResponseEntity.noContent().build();
				  return ResponseEntity.status(HttpStatus.NO_CONTENT)
						  .header("status","data deleted")
						  .body(delEmps);
	}
	
//update employee using id
	@PutMapping("/updatebyid/{id}")
	public ResponseEntity<?> updateEmployeeById(@PathVariable Long id,@RequestBody Employee newEmployee){
		Optional<Employee> updatedEmployee=employeeService.updateEmployeeById(id,newEmployee);
		if( updatedEmployee.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK)
					.header("status", "data updated succesfully")
					.body(updatedEmployee );
		}
		else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.header("status", "not avialable")
					.body(updatedEmployee );
		}
		
	}
	
	
	@PatchMapping("/partialUpdate/{id}")
	public ResponseEntity<?> partialUpdateById(@PathVariable Long id, @RequestBody  Map<String, Object> updates){
		Optional<Employee> partialUpdateEmp=employeeService.partialUpdatedById(id,updates);
		if(partialUpdateEmp.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK)
					.header("status","partial Updated successfully")
					.body(partialUpdateEmp);
		}
		else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("no data found with id :"+id);
		}
	}
	
	
		
	}



