package com.sathya.rest.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sathya.rest.model.Employee;

import jakarta.transaction.Transactional;
@Repository
public interface EmployeRepository extends JpaRepository<Employee,Long> {

	Optional<Employee> findByEmail(String email);  
	
	//Native class query
	@Query("SELECT CASE WHEN COUNT(e) > 0 THEN TRUE ELSE FALSE END FROM Employee e WHERE e.email = :email")
	boolean existsByEmail(@Param("email") String email);
	//boolean existsByEmail(String email);
	
	@Transactional
	void deleteByEmail(String email);

}


