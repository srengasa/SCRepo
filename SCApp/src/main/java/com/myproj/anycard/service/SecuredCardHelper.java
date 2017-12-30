package com.myproj.securedcard.service;

import com.myproj.securedcard.model.ESFAccntMgmtResponse;
import com.myproj.securedcard.model.ESFAcctMgmtRequest;
import com.myproj.securedcard.model.FDAcctMgmtRequest;
import com.myproj.securedcard.model.SecuredCardRequest;
import com.myproj.securedcard.model.SecuredCardResponse;

import reactor.core.publisher.Flux;

public class SecuredCardHelper {

	public ESFAcctMgmtRequest getESFAccntMgmtRequest(SecuredCardRequest secCardRequest) {
		ESFAcctMgmtRequest esfRequest = new ESFAcctMgmtRequest();
		//esfRequest.setHeader(secCardRequest.getHeader());
		esfRequest.setCustomer(secCardRequest.getCustomer());
		esfRequest.setUser(secCardRequest.getUser());
		return esfRequest;
	}

	public FDAcctMgmtRequest getFDAccntMgmtRequest(SecuredCardRequest secCardRequest) {
		FDAcctMgmtRequest fdAcctReq = new FDAcctMgmtRequest();
		
		return fdAcctReq;
	}

	public ESFAccntMgmtResponse processSecuredCard(FDAcctMgmtRequest fdAccntMgmtRequest,
			ESFAcctMgmtRequest esfAccntRequest) {
		Flux<ESFAccntMgmtResponse> flux = Flux.just(esfAccntRequest)
				.flatMap(request -> callSimpleRest(request, url))
				.flatMap(this::convertResponse)
				.flatMap(this::convertFinal);
		return null;
	}

	public SecuredCardResponse getSecuredCardResponse(ESFAccntMgmtResponse esfResponse) {
		// TODO Auto-generated method stub
		return null;
	}

}
