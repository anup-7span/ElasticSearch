package com.example.elasticsearch.repo;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class CustomersRepository
{
	@Autowired
	RestHighLevelClient restHighLevelClient;

	public SearchResponse getAllCustomers(SearchRequest searchRequest) throws IOException
	{

		return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
	}

	public SearchResponse getCustomerAggregation(SearchRequest request) throws IOException
	{
		return restHighLevelClient.search(request, RequestOptions.DEFAULT);
	}
}
