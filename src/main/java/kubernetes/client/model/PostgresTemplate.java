package kubernetes.client.model;

import java.util.ArrayList;
import java.util.List;

import io.fabric8.kubernetes.api.model.EnvVar;

public class PostgresTemplate extends Template {
	public PostgresTemplate() {
		super();
		this.setImage("postgres");
		this.setTag("9.6");
		this.setPort(5432);
		this.setMountPath("/var/lib/postgresql/data");
	}

	@Override
	public List<EnvVar> getEnvs() {
		List<EnvVar> envs = new ArrayList<EnvVar>();
		envs.add(new EnvVar("POSTGRES_USER", this.getUsername(), null));
		envs.add(new EnvVar("POSTGRES_PASSWORD", this.getPassword(), null));
		envs.add(new EnvVar("POSTGRES_DB", this.getDbname(), null));
		return envs;
	}
}
