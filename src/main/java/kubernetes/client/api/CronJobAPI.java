package kubernetes.client.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.BatchV2alpha1Api;
import io.kubernetes.client.models.V1Container;
import io.kubernetes.client.models.V1DeleteOptions;
import io.kubernetes.client.models.V1EnvVar;
import io.kubernetes.client.models.V1JobSpec;
import io.kubernetes.client.models.V1ObjectMeta;
import io.kubernetes.client.models.V1PodSpec;
import io.kubernetes.client.models.V1PodTemplateSpec;
import io.kubernetes.client.models.V2alpha1CronJob;
import io.kubernetes.client.models.V2alpha1CronJobSpec;
import io.kubernetes.client.models.V2alpha1JobTemplateSpec;
import io.kubernetes.client.util.Config;
import kubernetes.client.model.Application;

@Repository
public class CronJobAPI {

	public void create(Application app) throws IOException {
		ApiClient client;
		client = Config.defaultClient();
		Configuration.setDefaultApiClient(client);

		BatchV2alpha1Api apiInstance = new BatchV2alpha1Api();
		V2alpha1CronJob body = new V2alpha1CronJob();

		Map<String, String> podLabels = app.getListPod().get(0).getMetadata().getLabels();
		String labels = "app:" + podLabels.get("app") + ",pod-template-hash:" + podLabels.get("pod-template-hash");
		body.setApiVersion("batch/v2alpha1");
		body.setKind("CronJob");

		V1ObjectMeta metadata = new V1ObjectMeta();
		metadata.setName(app.getDeployment().getMetadata().getName() + "-job-autoscaler");
		metadata.setNamespace(app.getDeployment().getMetadata().getNamespace());
		body.setMetadata(metadata);

		V2alpha1CronJobSpec spec = new V2alpha1CronJobSpec();
		spec.setSchedule("* * * * *");
		V2alpha1JobTemplateSpec jobTemplate = new V2alpha1JobTemplateSpec();
		V1JobSpec jobSpec = new V1JobSpec();
		V1PodTemplateSpec podTemplateSpec = new V1PodTemplateSpec();
		V1PodSpec podSpec = new V1PodSpec();
		V1Container container = new V1Container();
		container.setName(app.getDeployment().getMetadata().getName() + "-job-autoscaler");
		container.setImage("duc2495/tf-serving-client:latest");
		List<V1EnvVar> env = new ArrayList<V1EnvVar>();
		V1EnvVar e1 = new V1EnvVar();
		e1.setName("TF_SERVER");
		e1.setValue("192.168.5.10:30900");
		env.add(e1);
		V1EnvVar e2 = new V1EnvVar();
		e2.setName("INFLUX_SERVER");
		e2.setValue("192.168.5.10:30086");
		env.add(e2);
		V1EnvVar e3 = new V1EnvVar();
		e3.setName("WEB_SERVER");
		e3.setValue("192.168.5.10:8080");
		env.add(e3);
		V1EnvVar e4 = new V1EnvVar();
		e4.setName("LABELS");
		e4.setValue(labels);
		env.add(e4);
		V1EnvVar e5 = new V1EnvVar();
		e5.setName("NAMESPACE");
		e5.setValue(app.getDeployment().getMetadata().getNamespace());
		env.add(e5);
		V1EnvVar e6 = new V1EnvVar();
		e6.setName("APP_ID");
		e6.setValue(String.valueOf(app.getId()));
		env.add(e6);
		V1EnvVar e7 = new V1EnvVar();
		e7.setName("MODEL_NAME");
		e7.setValue("cpu");
		env.add(e7);
		container.setEnv(env);
		List<String> command = new ArrayList<String>();
		command.add("bin/bash");
		container.setCommand(command);
		List<String> argsContainer = new ArrayList<String>();
		argsContainer.add("-c");
		argsContainer.add("/run_client.sh");
		container.setArgs(argsContainer);
		podSpec.setRestartPolicy("Never");
		List<V1Container> containers = new ArrayList<V1Container>();
		containers.add(0, container);
		podSpec.setContainers(containers);
		podTemplateSpec.setSpec(podSpec);
		jobSpec.setTemplate(podTemplateSpec);
		jobTemplate.setSpec(jobSpec);
		spec.setJobTemplate(jobTemplate);
		body.setSpec(spec);

		try {
			apiInstance.createNamespacedCronJob(app.getDeployment().getMetadata().getNamespace(), body, null);
			System.out.println("Created CronJob");
		} catch (ApiException e) {
			System.err.println("Exception when calling BatchV2alpha1Api#createNamespacedCronJob");
			e.printStackTrace();
		}
	}

	public V2alpha1CronJob get(String name, String namespace) throws IOException {
		ApiClient client;
		client = Config.defaultClient();
		Configuration.setDefaultApiClient(client);

		BatchV2alpha1Api apiInstance = new BatchV2alpha1Api();
		String jobName = name + "-job-autoscaler";
		String pretty = "pretty_example";
		try {
			V2alpha1CronJob result = apiInstance.readNamespacedCronJob(jobName, namespace, pretty, true, true);
			return result;
		} catch (ApiException e) {
			System.err.println("Exception when calling BatchV2alpha1Api#patchNamespacedCronJob");
			e.printStackTrace();
		}
		return null;
	}

	public void delete(String name, String namespace) throws IOException {
		ApiClient client;
		client = Config.defaultClient();
		Configuration.setDefaultApiClient(client);

		BatchV2alpha1Api apiInstance = new BatchV2alpha1Api();
		String jobName = name + "-job-autoscaler";
		V1DeleteOptions body = new V1DeleteOptions();
		String pretty = "pretty_example";
		Integer gracePeriodSeconds = 1;
		Boolean orphanDependents = true;
		String propagationPolicy = "propagationPolicy_example";
		try {
			apiInstance.deleteNamespacedCronJob(jobName, namespace, body, pretty, gracePeriodSeconds, orphanDependents,
					propagationPolicy);
		} catch (ApiException e) {
			System.err.println("Exception when calling BatchV2alpha1Api#deleteNamespacedCronJob");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Deleted CronJob");
		}

	}

	public void update(V2alpha1CronJob cronJob) throws IOException {
		ApiClient client;
		client = Config.defaultClient();
		Configuration.setDefaultApiClient(client);

		BatchV2alpha1Api apiInstance = new BatchV2alpha1Api();
		String pretty = "pretty_example";
		try {
			V2alpha1CronJob result = apiInstance.replaceNamespacedCronJob(cronJob.getMetadata().getName(),
					cronJob.getMetadata().getNamespace(), cronJob, pretty);
			System.out.println(result);
		} catch (ApiException e) {
			System.err.println("Exception when calling BatchV2alpha1Api#replaceNamespacedCronJob");
			e.printStackTrace();
		}

	}
}
