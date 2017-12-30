package com.myproj.securedcard.controller;

import org.apache.log4j.Logger;
import org.jboss.logging.NDC;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.myproj.securedcard.model.ESFAccntMgmtResponse;
import com.myproj.securedcard.model.ESFAcctMgmtRequest;
import com.myproj.securedcard.model.FDAcctMgmtRequest;
import com.myproj.securedcard.model.SecuredCardRequest;
import com.myproj.securedcard.model.SecuredCardResponse;
import com.myproj.securedcard.model.Status;
import com.myproj.securedcard.service.SecuredCardHelper;
import com.myproj.securedcard.util.CommonUtil;

@RestController
public class SecuredCardController {

	private static Logger logger = Logger.getLogger(SecuredCardController.class);
	@PostMapping("processApp")
	public @ResponseBody SecuredCardResponse processApp(@RequestBody SecuredCardRequest secCardRequest) {
		SecuredCardHelper helper = new SecuredCardHelper();
		SecuredCardResponse secResponse = null;
		try {
			ESFAcctMgmtRequest esfAccntRequest = helper.getESFAccntMgmtRequest(secCardRequest);
			FDAcctMgmtRequest fdAccntMgmtRequest = helper.getFDAccntMgmtRequest(secCardRequest);
			ESFAccntMgmtResponse esfResponse = helper.processSecuredCard(fdAccntMgmtRequest, esfAccntRequest);
			secResponse = helper.getSecuredCardResponse(esfResponse);
		} catch(Exception e) {
			Status status = CommonUtil.getErrorStatus();
			secResponse = new SecuredCardResponse();
			secResponse.setStatus(status);
			return secResponse;
		}
		return secResponse;
	}
	
	
	
}
