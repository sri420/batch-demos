package com.example.batchprocessing.web;

import java.util.List;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.batchprocessing.model.Person;
import com.example.batchprocessing.service.JobService;

@RestController
public class BatchController {
	
	
	@Autowired
	public JobService jobService;
	
	
	@PostMapping("/batch")
	public BatchStatus doBatch(@RequestBody List<Person> personList) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		//personList.forEach(name->System.out.println(name));
		return jobService.getJobStatus(personList);
	}
	
}

