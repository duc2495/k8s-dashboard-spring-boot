package kubernetes.client.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Many;

import kubernetes.client.model.Customer;
import kubernetes.client.model.Project;

public interface CustomerMapper {

	@Insert("INSERT INTO customer(full_name,email,password) VALUES(#{fullName}, #{email}, #{password})")
	void insert(Customer customer);

	@Select("SELECT * FROM customer WHERE email = #{email}")
	@Results(value = { @Result(property = "id", column = "customer_id"),
			@Result(property = "fullName", column = "full_name"), @Result(property = "email", column = "email"),
			@Result(property = "password", column = "password"),
			@Result(property = "projects", javaType = List.class, column = "customer_id", many = @Many(select = "selectProjects")) })
	Customer getCustomerByEmail(String email);

	@Select("SELECT project_name FROM project WHERE customer_id = #{customer_id}")
	@Results(value = { @Result(property = "projectName", column = "project_name") })
	List<Project> selectProjects(int customer_id);
}
