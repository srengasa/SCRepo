package com.myproj.netty.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.jboss.logging.NDC;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.myproj.netty.model.Customer;
import com.myproj.netty.model.User;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpContent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.http.client.HttpClient;
import reactor.ipc.netty.http.client.HttpClientResponse;

@RestController
public class NettyController {

	private static Logger logger = Logger.getLogger(NettyController.class);
	@PostMapping("getUsers")
	public @ResponseBody User getUser(@RequestBody Customer customer) {
		final String url = "http://localhost:8081/Services/getUsers";
		Flux<User> flux = Flux.just(customer).flatMap(request -> callSimpleRest(request, url)).flatMap(this::convertResponse).flatMap(this::convertFinal);
		User user = flux.blockFirst();
		return user;
	}

	private Mono<HttpClientResponse> callSimpleRest(Customer request, String url) {
		logger.info("Sample Logger************");
		ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
		Mono<HttpClientResponse> mono = HttpClient.create().post(url, handler -> {
			handler.addHeader("Content-Type", "application/json");
			String reqStr = null;
			try {
				reqStr = writer.writeValueAsString(request);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return handler.sendString(Mono.just(reqStr));
		});
		return mono;
	}

	private Flux<HttpContent> convertResponse(HttpClientResponse response) {
		return response.receiveContent();
	}
	
	private Mono<User> convertFinal(HttpContent content) {
		ObjectMapper mapper = new ObjectMapper();
		byte[] bytes = new byte[content.content().capacity()];
		System.out.println("contents " + content.content().capacity());
		ByteBuf buffer = content.content();
		for (int i = 0; i < buffer.capacity(); i++) {
			bytes[i] = buffer.getByte(i);
		}
		byte[] sample = new byte[bytes.length];
		buffer.getBytes(0, sample);
		String str = new String(bytes);
		System.out.println("Response Content*********" + str);
		String sampleStr = new String(sample);
		System.out.println("Response Content222**************" + sampleStr);
		//User user = mapper.convertValue(str, User.class);
		User user = new User();
		try {
			user = mapper.readValue(bytes, User.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Mono.just(user);
	}
}
