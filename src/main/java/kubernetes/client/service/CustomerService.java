package kubernetes.client.service;

import kubernetes.client.model.Customer;

public interface CustomerService {
	
	void createCustomer(Customer customer);
	Customer getCustomerByEmail(String email);
}
