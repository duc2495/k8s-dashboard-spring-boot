package kubernetes.client.model;

import java.util.ArrayList;
import java.util.List;

import io.fabric8.kubernetes.api.model.EnvVar;

public class MysqlTemplate extends Template {

	public MysqlTemplate() {
		this.setImage("mysql");
		this.setTag("latest");
		this.setPort(3306);
		this.setMountPath("/var/lib/mysql");
	}

	@Override
	public List<EnvVar> getEnvs() {
		List<EnvVar> envs = new ArrayList<EnvVar>();
		envs.add(new EnvVar("MYSQL_USER", this.getUsername(), null));
		envs.add(new EnvVar("MYSQL_PASSWORD", this.getPassword(), null));
		envs.add(new EnvVar("MYSQL_DATABASE", this.getDbname(), null));
		envs.add(new EnvVar("MYSQL_ROOT_PASSWORD", this.getRootPassword(), null));
		return envs;
	}
}