package kubernetes.client.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import kubernetes.client.model.Customer;

public interface CustomerMapper {

	@Insert("INSERT INTO customer(full_name,email,password) VALUES(#{fullName}, #{email}, #{password})")
	void insert(Customer customer);

	@Select("SELECT * FROM customer WHERE email = #{email}")
	@Results(value = { 
			@Result(property = "id", column = "customer_id"), 
			@Result(property = "fullName", column = "full_name"),
			@Result(property = "email", column = "email"), 
			@Result(property = "password", column = "password") })
	Customer getCustomerByEmail(String email);
}
