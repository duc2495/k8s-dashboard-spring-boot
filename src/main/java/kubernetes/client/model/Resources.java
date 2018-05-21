package kubernetes.client.model;

import java.util.ArrayList;
import java.util.List;

public class Resources {
	private List<List<Long>> actualCPU = new ArrayList<List<Long>>();
	private List<List<Long>> predictCPU = new ArrayList<List<Long>>();

	public List<List<Long>> getActualCPU() {
		return actualCPU;
	}

	public void setActualCPU(List<List<Long>> actualCPU) {
		this.actualCPU = actualCPU;
	}

	public List<List<Long>> getPredictCPU() {
		return predictCPU;
	}

	public void setPredictCPU(List<List<Long>> predictCPU) {
		this.predictCPU = predictCPU;
	}

}
