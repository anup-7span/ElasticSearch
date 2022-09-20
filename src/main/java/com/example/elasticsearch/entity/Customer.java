package com.example.elasticsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "mydemo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer
{
	@Id
	private Integer id;

	@Field(type = FieldType.Text)
	private String firstName;

	@Field(type = FieldType.Text)
	private String lastName;

	@Field(type = FieldType.Integer)
	private int age;

	@Field(type = FieldType.Integer)
	private double amount;

	@Field(type = FieldType.Boolean)
	private Boolean productIsAvailable;

}
