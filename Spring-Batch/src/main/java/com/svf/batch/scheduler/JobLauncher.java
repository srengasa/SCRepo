package com.syf.batch.scheduler;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component("myJobLauncher")
public class JobLauncher extends QuartzJobBean {

	private static final String JOBName = "jobName";
	
	@Autowired
	private JobLocator jobLocator;
	
	@Autowired
	private org.springframework.batch.core.launch.JobLauncher jobLauncher;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		Map<String, Object> jobDataMap = context.getMergedJobDataMap();
		
		String jobName = (String) jobDataMap.get(JOBName);
		
		JobParameters jobParams = getParamsFromMap(jobDataMap);
		
		try {
			jobLauncher.run(jobLocator.getJob(jobName), jobParams);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Exiting from Job");
		}
	}

	private JobParameters getParamsFromMap(Map<String, Object> jobDataMap) {
		JobParametersBuilder jpb = new JobParametersBuilder();
		for (Entry<String, Object> entry : jobDataMap.entrySet()) {
			Object value = entry.getValue();
			String key = entry.getKey();
			if (value instanceof String && !key.equals(JOBName)) {
				jpb.addString(key, (String) value);
			}
		}
		jpb.addDate("run Date", new Date());
		return jpb.toJobParameters();
	}

}
