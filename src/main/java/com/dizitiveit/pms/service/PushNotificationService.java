package com.dizitiveit.pms.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.dizitiveit.pms.firebase.FCMService;
import com.dizitiveit.pms.model.PushNotificationRequest;

@Service
public class PushNotificationService {

	  private Logger logger = LoggerFactory.getLogger(PushNotificationService.class);
	    private FCMService fcmService;

	    public PushNotificationService(FCMService fcmService) {
	        this.fcmService = fcmService;
	    }

	    @Scheduled(initialDelay = 60000, fixedDelay = 60000)
	    public void sendSamplePushNotification() {
	        try {
	            fcmService.sendMessageWithoutData(getSamplePushNotificationRequest());
	        } catch (InterruptedException | ExecutionException e) {
	            logger.error(e.getMessage());
	        }
	    }

	    public void sendPushNotification(PushNotificationRequest request) {
	        try {
	            fcmService.sendMessage(getSamplePayloadData(), request);
	        } catch (InterruptedException | ExecutionException e) {
	            logger.error(e.getMessage());
	        }
	    }

	    public void sendPushNotificationWithoutData(PushNotificationRequest request) {
	        try {
	            fcmService.sendMessageWithoutData(request);
	        } catch (InterruptedException | ExecutionException e) {
	            logger.error(e.getMessage());
	        }
	    }


	    public void sendPushNotificationToToken(PushNotificationRequest request) {
	            try {
					fcmService.sendMessageToToken(request);
					System.out.println(request.toString());
				} catch (InterruptedException  | ExecutionException e) {
					 logger.error(e.getMessage());
				}
	    }


	    private Map<String, String> getSamplePayloadData() {
	        Map<String, String> pushData = new HashMap<>();
	        pushData.put("messageId", "123");
	        pushData.put("text", "Hello. This is payload content" + " " + LocalDateTime.now());
	        return pushData;
	    }


	    private PushNotificationRequest getSamplePushNotificationRequest() {
	        PushNotificationRequest request = new PushNotificationRequest("Common topic - Hello","Sending test message \\uD83D\\uDE42","common");
	        return request;
	    }
}
