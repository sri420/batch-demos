package com.example.batchprocessing.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.batchprocessing.config.PersonItemProcessor;
import com.example.batchprocessing.listener.JobCompletionNotificationListener;
import com.example.batchprocessing.model.Person;

@RestController
public class BatchController {
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public JobCompletionNotificationListener listener;

	@Autowired
	PersonItemProcessor processor;

	@Autowired
	public JobLauncher jobLauncher;

	@Autowired
	public JdbcBatchItemWriter<Person> writer;

	@PostMapping("/batch")
	public BatchStatus doBatch(@RequestBody List<Person> personList) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {

		//personList.forEach(name->System.out.println(name));
		
		Step step1=stepBuilderFactory.get("step1")
		.<Person, Person> chunk(10)
		.reader(new ListItemReader<>(personList))
		.processor(processor)
		.writer(writer)
		.build();	

		Job job=jobBuilderFactory.get("importUserJob")
		.incrementer(new RunIdIncrementer())
		.listener(listener)
		.flow(step1)
		.end()
		.build();

		Map<String,JobParameter> paramMap=new HashMap<>();
		paramMap.put("time", new JobParameter(System.currentTimeMillis()));

		JobParameters jobParameters=new JobParameters(paramMap);
		JobExecution jobExecution=jobLauncher.run(job, jobParameters);
		
		return jobExecution.getStatus();
	}
}

