package com.example.elasticsearch.Controller;

import com.example.elasticsearch.model.CustomerModel;
import com.example.elasticsearch.entity.Customer;
import com.example.elasticsearch.service.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/customer")
public class CustomerController
{
	@Autowired
	CustomerServiceImpl customerService;

	@PostMapping("/bulkInsert")
	public void bulkInsert() throws IOException
	{
		for (int i = 0; i < 50; i++)
		{
			Customer customer = new Customer();
			customer.setId(i);

			Random random = new Random();
			int randomNumber = random.nextInt(51 - 17) + 17;
			customer.setAge(randomNumber);
			customer.setProductIsAvailable(random.nextBoolean());
			customer.setFirstName("Customer_" + i);
			customer.setLastName("Cust_" + i);
			customer.setAmount(random.nextInt(55000 - 25000) + 25000);
			customerService.create(customer);
		}
	}

	@PostMapping("/create")
	public void create(@RequestBody Customer customer){
		customerService.create(customer);
	}

	@GetMapping
	public Iterable<Customer> getCustomer(){
		return customerService.getCustomer();
	}

	@PutMapping
	public String update(@RequestBody Customer customer){
		return customerService.update(customer);
	}

	@DeleteMapping("/{id}")
	public String deleteById(@PathVariable Integer id){
		return customerService.deleteById(id);
	}

	@GetMapping("/customSearch")
	public List<Customer> customCustomerSearch(@RequestParam(required = false) Integer min_age,
			@RequestParam(required = false) Integer max_age,
			@RequestParam(required = false) Double min_amount,
			@RequestParam(required = false) Double max_amount,
			@RequestParam(required = false) Boolean productIsAvailable) throws IOException {
			return customerService.fetchCustomSearchCustomer(min_age, max_age, min_amount, max_amount, productIsAvailable);
	}

	@GetMapping("/getAverageAge")
	public CustomerModel getCustomerAverageAge() throws IOException {
		return customerService.getCustomerAverageAge();
	}

}
