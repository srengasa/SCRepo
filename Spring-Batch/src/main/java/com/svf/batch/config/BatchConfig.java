package com.syf.batch.config;

import java.util.HashMap;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.syf.batch.scheduler.JobLauncher;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean(name="transactionManager")
	public ResourcelessTransactionManager getTransactionManager() {
		return new ResourcelessTransactionManager();
	}
	
	@Bean(name="jobRepository")
	public MapJobRepositoryFactoryBean createJobRepository(PlatformTransactionManager transactionManager) {
		MapJobRepositoryFactoryBean jobRepo = new MapJobRepositoryFactoryBean();
		jobRepo.setTransactionManager(transactionManager);
		return jobRepo;
	}
	@Bean(name="jobLauncher")
	public SimpleJobLauncher createJobLauncher(JobRepository jobRepo) {
		SimpleJobLauncher jl = new SimpleJobLauncher();
		jl.setJobRepository(jobRepo);
		return jl;
	}
	@Bean(name="jobRegistry")
	public MapJobRegistry jobRegistry() {
		MapJobRegistry jr = new MapJobRegistry();
		return jr;
	}
	@Bean(name="jobRegistryFactoryBean")
	public JobRegistryBeanPostProcessor jobRegistryBean(JobRegistry jobRegistry) {
		JobRegistryBeanPostProcessor jr = new JobRegistryBeanPostProcessor();
		jr.setJobRegistry(jobRegistry);
		return jr;
	}
	@Bean(name="jobDetail")
	public JobDetailFactoryBean getJobDetailBean(JobRegistry jobRegistry, JobLauncher jobLauncher) {
		JobDetailFactoryBean jdb = new JobDetailFactoryBean();
		jdb.setJobClass(JobLauncher.class);
		jdb.setGroup("quartz-group");
		jdb.setJobDataAsMap(new HashMap<String, Object>() {
			{
				put("jobName", "cacheJob");
				put("jobLocator", jobRegistry);
				put("jobLauncher", jobLauncher);
				put("param1", "gemfire");
				put("param2", "cache");
			}
		});
		return jdb;
	}
	@Bean(name="cronTrigger")
	public CronTriggerFactoryBean createTrigger(JobDetail jobDetail) {
		CronTriggerFactoryBean ctb = new CronTriggerFactoryBean();
		ctb.setJobDetail(jobDetail);
		ctb.setGroup("quartz-group");
		ctb.setCronExpression("*/10 * * * * ?");
		return ctb;
	}
	@Bean
	public SchedulerFactoryBean factoryBean(Trigger trigger) {
		SchedulerFactoryBean sfb = new SchedulerFactoryBean();
		sfb.setTriggers(trigger);
		return sfb;
	}
	
	@Bean(name="Step")
	public Step getStep() {
		return stepBuilderFactory.get("Extract -> Transform -> Aggregate -> Load").allowStartIfComplete(true).<>
	}
	
	@Bean
	public Job configureJob(Step step) {
		return jobBuilderFactory.get("cacheJob").flow(step).
	}
}
