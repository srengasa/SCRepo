package com.syf.poc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.syf.poc.mapping.MyMapper;
import com.syf.poc.model.MyRequest;
import com.syf.poc.model.WtxRequest;

@RestController
public class RequestController {

	@Autowired
	private MyMapper myMapper;
	
	@PostMapping("/v1/dynaRequest")
	public @ResponseBody WtxRequest frameDynamicRequest(@RequestBody MyRequest request) {
		return myMapper.buildWtxRequest(request);
	}
}
