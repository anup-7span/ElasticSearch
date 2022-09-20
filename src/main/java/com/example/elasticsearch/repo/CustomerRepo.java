package com.example.elasticsearch.repo;

import com.example.elasticsearch.entity.Customer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CustomerRepo extends ElasticsearchRepository<Customer,Integer>
{
}
