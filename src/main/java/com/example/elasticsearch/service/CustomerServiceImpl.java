package com.example.elasticsearch.service;

import com.example.elasticsearch.model.CustomerModel;
import com.example.elasticsearch.entity.Customer;
import com.example.elasticsearch.repo.CustomerRepo;
import com.example.elasticsearch.repo.CustomersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Sum;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.SumAggregator;
import org.elasticsearch.search.aggregations.metrics.ValueCount;
import org.elasticsearch.search.aggregations.metrics.ValueCountAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService
{

	private final String indexName = "mydemo";
	@Autowired
	CustomerRepo customerRepo;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	CustomersRepository customersRepository;

	@Override
	public void create(Customer customer)
	{
		customerRepo.save(customer);
		//return "add customer"+customer.getId();
	}

	@Override
	public Iterable<Customer> getCustomer()
	{
		return customerRepo.findAll();
	}

	@Override
	public String update(Customer customer)
	{
		Optional<Customer> customerOptional = customerRepo.findById(customer.getId());
		if (customerOptional.isPresent())
		{
			customerRepo.save(customer);
			return "User updated successfully.";
		}
		return "no customer";
	}

	@Override
	public String deleteById(Integer id)
	{
		if (customerRepo.existsById(id))
		{
			customerRepo.deleteById(id);
			return "User deleted successfully.";
		}
		return "There is no such user.";
	}

	@Override
	public List<Customer> fetchCustomSearchCustomer(
			Integer min_age, Integer max_age, Double min_amount, Double max_amount, Boolean productIsAvailable) throws IOException
	{

		BoolQueryBuilder query1 = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("age").gte(min_age).lte(max_age)).must(
				QueryBuilders.rangeQuery("amount").gte(min_amount).lte(max_amount));
		BoolQueryBuilder query2 = null;
		if (productIsAvailable != null)
		{
			query2 = QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("productIsAvailable", productIsAvailable));
		}
		SearchRequest searchRequest = new SearchRequest(indexName);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(query1).query(query2);
		searchRequest.source(searchSourceBuilder);
		SearchResponse response = customersRepository.getAllCustomers(searchRequest);

		List<Customer> userList = new ArrayList<>();

		if (response.getHits().getTotalHits().value > 0)
		{
			SearchHit[] searchHit = response.getHits().getHits();
			for (SearchHit hit : searchHit)
			{
				Map<String, Object> map = hit.getSourceAsMap();
				userList.add(objectMapper.convertValue(map, Customer.class));
			}
		}
		return userList;
	}



	@Override
	public CustomerModel getCustomerAggregation() throws IOException
	{
		CustomerModel customerModel = new CustomerModel();

		AvgAggregationBuilder avgAggregationBuilder = AggregationBuilders.avg("avg_age").field("age");
		ValueCountAggregationBuilder valueCountAggregationBuilder = AggregationBuilders.count("count_id").field("id");
		SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("sum_amount").field("amount");

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
				.aggregation(avgAggregationBuilder)
				.aggregation(valueCountAggregationBuilder)
				.aggregation(sumAggregationBuilder);
		SearchRequest searchRequest = new SearchRequest(indexName);
		searchRequest.source(searchSourceBuilder);

		SearchResponse response = customersRepository.getCustomerAggregation(searchRequest);
		ValueCount count = response.getAggregations().get("count_id");
		Avg avg = response.getAggregations().get("avg_age");
		Sum sum = response.getAggregations().get("sum_amount");

		customerModel.setAvg(avg.getValue());
		customerModel.setCount(count.getValue());
		customerModel.setSum(sum.getValue());

		return customerModel;

	}

}
