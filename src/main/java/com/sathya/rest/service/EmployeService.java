package com.sathya.rest.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.sathya.rest.model.Employee;
import com.sathya.rest.repository.EmployeRepository;

@Service

public class EmployeService {
	@Autowired
	EmployeRepository employeRepository;
	
	//Get emp names
	  @Cacheable("names")
	    public List<String> getnames() {
	        System.out.println("Fetching the names");
	        return List.of("service1", "service2", "service3");
	    }
	
	

public Employee saveService(Employee employee) {
		
		return employeRepository.save(employee);
	}
// get list of data
	public List<Employee> saveAllService(List<Employee> employees) {
		return employeRepository.saveAll(employees);
	}


	//get the data

	public List<Employee> getAllEmployees() {
		
		return employeRepository.findAll();
	}

   //find by id 
	public Optional<Employee> getById(Long id) {
		
		return employeRepository.findById(id);
	}

//find by email
	public Optional<Employee> getByEmail(String email) {
		
		return employeRepository.findByEmail(email);
	}

	
//delete by id
	public boolean deleteEmployeeById(Long id) {
		
		 boolean status =employeRepository.existsById(id);
	        
	        if(status) {
	        	employeRepository.deleteById(id);
	            return true;
	        }
	        else {
	            
	            return false;
	        }
		
	}
//delete employee by email
	public boolean deleteEmployeeByEmail(String email) {
		 boolean stats =employeRepository.existsByEmail(email);
	        
	        if(stats) {
	        	employeRepository.deleteByEmail(email);
	            return true;
	        }
	        else {
	            
	            return false;
	        }
	}
//delete all data
	public List<Employee> deleteAllEmployees() {
	    // Retrieve all employees before deleting them
	    List<Employee> employeesToDelete = employeRepository.findAll();
	    
	    // Delete all employees
	    employeRepository.deleteAll();
	    
	    // Return the list of deleted employees
	    return employeesToDelete;
	}
	//update using id
	public Optional<Employee> updateEmployeeById(Long id, Employee newEmployee) {
		Optional<Employee> optionalemp=employeRepository.findById(id);
		if(optionalemp.isPresent()) {
			Employee existingEmp= optionalemp.get();
			existingEmp.setName(newEmployee.getName());
			existingEmp.setSalary(newEmployee.getSalary());
			existingEmp.setDepartment(newEmployee.getDepartment());
			existingEmp.setAddress(newEmployee.getAddress());
			existingEmp.setEmail(newEmployee.getEmail());
			Employee updatedEmp=employeRepository.save(existingEmp);
			return Optional.of(updatedEmp);
			
		}
		else {
			return Optional.empty();
		}
	}
	
	//partial update 
	public Optional<Employee> partialUpdatedById(Long id ,Map<String,Object> updates) {
		Optional<Employee> optionalEmp=employeRepository.findById(id);
		if(optionalEmp.isPresent()) {
			Employee existingEmp=optionalEmp.get();
			updates.forEach((key,value)-> {
					switch(key) {
					case "name":
						existingEmp.setName((String)value);
						break;
					case "salary":
						existingEmp.setSalary((Double)value);
						break;
					case "department":
						existingEmp.setDepartment((String)value);
						break;
					case "address":
						existingEmp.setAddress((String)value);
						break;
					case "email":
						existingEmp.setEmail((String)value);
						break;
					   default:
	                        throw new IllegalArgumentException("Invalid field: " + key);
						
					}
			});
			Employee updatedEmply=employeRepository.save(existingEmp);
			return Optional.of(updatedEmply);
				
		}	
			else{
				 return Optional.empty();
			}
		}
	
}
	
	

	


