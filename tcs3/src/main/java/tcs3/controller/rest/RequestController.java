package tcs3.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tcs3.model.lab.RequestTracker;
import tcs3.service.EntityService;
import tcs3.webUI.ResponseJSend;

@RestController
@RequestMapping("/REST/Request")
public class RequestController {
public static Logger logger = LoggerFactory.getLogger(RequestController.class);
	
	@Autowired
	EntityService entityService;
	
	@RequestMapping(value = "/{reqId}/trakCode/{trackingCode}", method = {RequestMethod.GET}) 
	public ResponseJSend<RequestTracker> findRequest(
			@PathVariable Long reqId, @PathVariable String trackingCode){
		
		return this.entityService.findReqeustTracker(reqId, trackingCode);
	}
	
}
