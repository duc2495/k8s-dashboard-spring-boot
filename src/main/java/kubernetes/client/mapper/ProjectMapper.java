package kubernetes.client.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kubernetes.client.model.Project;

public interface ProjectMapper {

	@Insert("INSERT INTO project(project_name, description, customer_id) VALUES(#{project.projectName}, #{project.description}, #{customer_id})")
	void insert(@Param("project") Project project, @Param("customer_id") int customer_id);
	
	@Select("SELECT * FROM project WHERE project_id =#{projectId}")
	@Results({
		@Result(property = "projectId",  column = "project_id"),
		@Result(property = "projectName", column = "project_name"),
		@Result(property = "description", column = "description")
	})
	Project getProjectById(int projectId);
	
	@Select("SELECT * FROM project WHERE project_id =#{projectId} AND customer_id =#{customerId}")
	@Results({
		@Result(property = "projectId",  column = "project_id"),
		@Result(property = "projectName", column = "project_name"),
		@Result(property = "description", column = "description")
	})
	Project getProjectByIdAndUserId(@Param("projectId") int projectId, @Param("customerId")int customerId);
	
	@Select("SELECT * FROM project WHERE project_name =#{projectName}")
	@Results({
		@Result(property = "projectId",  column = "project_id"),
		@Result(property = "projectName", column = "project_name"),
		@Result(property = "description", column = "description")
	})
	Project getProjectByName(String projectName);
	
	@Select("SELECT * FROM project WHERE project_name =#{projectName} AND customer_id =#{customerId}")
	@Results({
		@Result(property = "projectId",  column = "project_id"),
		@Result(property = "projectName", column = "project_name"),
		@Result(property = "description", column = "description")
	})
	Project getProjectByNameAndUserId(@Param("projectName")String projectName, @Param("customerId")int customerId);
	
	@Select("SELECT * FROM project WHERE customer_id = #{customer_id}")
	@Results({ 
			@Result(property = "projectId", column = "project_id"),
			@Result(property = "projectName", column = "project_name"),
			@Result(property = "descrition", column = "descrition")})
	List<Project> getProjectsByUserId(int customer_id);

	
	@Update("UPDATE project SET description =#{description} WHERE project_name =#{projectName}")
	void update(Project project);
	
	@Delete("DELETE FROM project WHERE project_id =#{projectId}")
	void delete(int projectId);
}
