package kubernetes.client.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kubernetes.client.model.Application;

public interface ApplicationMapper {
	@Insert("INSERT INTO application(app_name, description, project_id) "
			+ "VALUES(#{app.name}, #{app.description}, #{project_id})")
	void insert(@Param("app") Application app, @Param("project_id") int project_id);

	@Select("SELECT * FROM application WHERE project_id = #{project_id} ORDER BY app_id")
	@Results({ @Result(property = "id", column = "app_id"), @Result(property = "name", column = "app_name"),
			@Result(property = "descrition", column = "descrition"),
			@Result(property = "proAutoscaler", column = "pro_autoscaler") })
	List<Application> getApplicationsByProjectId(int project_id);

	@Select("SELECT * FROM application WHERE app_id = #{app_id}")
	@Results({ @Result(property = "id", column = "app_id"), @Result(property = "name", column = "app_name"),
			@Result(property = "descrition", column = "descrition"),
			@Result(property = "proAutoscaler", column = "pro_autoscaler") })
	Application getApplicationById(int app_id);

	@Select("SELECT * FROM application WHERE app_name = #{app_name}")
	@Results({ @Result(property = "id", column = "app_id"), @Result(property = "name", column = "app_name"),
			@Result(property = "descrition", column = "descrition"),
			@Result(property = "proAutoscaler", column = "pro_autoscaler") })
	Application getApplicationByName(String app_name);

	@Update("UPDATE application SET description =#{description} WHERE app_id =#{id}")
	void update(Application app);

	@Update("UPDATE application SET pro_autoscaler =#{proAutoscaler}  WHERE app_id =#{id}")
	void updateAutoscaler(Application app);

	@Delete("DELETE FROM application WHERE app_id =#{app_id}")
	void delete(int app_id);

	@Delete("DELETE FROM application WHERE project_id =#{id}")
	void deleteByProjectId(int id);
}
