package com.batch.TransactionScheduling.Controller;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/schedule")
public class SchedulerController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    Job loadJob;

    @GetMapping("sf")
    public String sf()
    {
        return "helo";
    }

/*   @Autowired
   Job exportUserJob ;*/

//  @Scheduled(fixedRate = 20000)
    @GetMapping("/hello")
    public BatchStatus load() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {


        Map<String, JobParameter> maps = new HashMap<>();
        maps.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters parameters = new JobParameters(maps);
        JobExecution jobExecution = jobLauncher.run(loadJob, parameters);



        System.out.println("Batch is Running...");
        while (jobExecution.isRunning()) {
        }

        return jobExecution.getStatus();
    }
}
