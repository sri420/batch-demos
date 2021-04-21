package com.example.batchprocessing.config;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.batchprocessing.model.Person;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

		@Bean
		public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
			return new JdbcBatchItemWriterBuilder<Person>()
					.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
					.sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
					.dataSource(dataSource)
			
		.build();
							}
}

