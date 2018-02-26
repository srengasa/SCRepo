package com.syf.batch.steps;

import org.springframework.batch.item.file.BufferedReaderFactory;
import org.springframework.batch.item.file.DefaultBufferedReaderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.syf.batch.model.Customer;

public class CustomerFileReader extends FlatFileItemReader<Customer>{

	public CustomerFileReader() {
		setResource(new FileSystemResource("c:/sampleText.txt"));
		setEncoding("UTF-8");
		setBufferedReaderFactory(new DefaultBufferedReaderFactory());
	}
}
