package com.syf.caching.serializer;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.Declarable;
import com.gemstone.gemfire.pdx.PdxReader;
import com.gemstone.gemfire.pdx.PdxSerializer;
import com.gemstone.gemfire.pdx.PdxWriter;
import com.syf.caching.domain.Customer;

@Component
public class GemfireSerializer implements PdxSerializer, Declarable {

	private static final Logger logger = LogManager.getLogger(GemfireSerializer.class);
	
	@Override
	public void init(Properties arg0) {
		logger.info("inside init method " + arg0);
	}

	@Override
	public Object fromData(Class<?> clazz, PdxReader reader) {
		logger.info("inside fromData method : " + clazz.getName());
		if (!clazz.equals(Customer.class)) return null;
		Customer c = new Customer();
		c.setAccountNumber(reader.readLong("id"));
		c.setFirstName(reader.readString("firstName"));
		c.setLastName(reader.readString("lastName"));
		return c;
	}

	@Override
	public boolean toData(Object o, PdxWriter writer) {
		logger.info("inside toData for serialization? " + o);
		if (!(o instanceof Customer)) return false;
		Customer c = (Customer) o;
		writer.writeLong("id", c.getAccountNumber())
		.markIdentityField("id")
		.writeString("firstName", c.getFirstName())
		.writeString("lastName", c.getLastName());
		return true;
	}

}
