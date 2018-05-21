package kubernetes.client.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kubernetes.client.mapper.CustomerMapper;
import kubernetes.client.model.Customer;
import kubernetes.client.service.CustomerService;

@Transactional
@Service
public class CustomerServiceImpl implements CustomerService{

	@Autowired
	private CustomerMapper customerMapper;
	
	@Override
	public void createCustomer(Customer customer) {
		customerMapper.insert(customer);
	}

	@Override
	public Customer getCustomerByEmail(String email) {
		Customer customer = customerMapper.getCustomerByEmail(email);
		return customer;
	}

}
