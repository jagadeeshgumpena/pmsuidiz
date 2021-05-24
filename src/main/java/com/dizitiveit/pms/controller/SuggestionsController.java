package com.dizitiveit.pms.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.pms.Dao.FlatOwnersDao;
import com.dizitiveit.pms.Dao.FlatResidenciesDao;
import com.dizitiveit.pms.Dao.FlatsDao;
import com.dizitiveit.pms.Dao.ResponsesDao;
import com.dizitiveit.pms.Dao.SuggestionsDao;
import com.dizitiveit.pms.Dao.UsersDao;
import com.dizitiveit.pms.model.FlatOwners;
import com.dizitiveit.pms.model.FlatResidencies;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.PushNotificationRequest;
import com.dizitiveit.pms.model.Responses;
import com.dizitiveit.pms.model.Suggestions;
import com.dizitiveit.pms.model.Users;
import com.dizitiveit.pms.pojo.SlotsPojo;
import com.dizitiveit.pms.pojo.SuggestionsPojo;
import com.dizitiveit.pms.service.OtpSenderService;

@RequestMapping("/suggestion")
@RestController
public class SuggestionsController {

	@Autowired
	private SuggestionsDao suggestionsDao;
	 @Autowired
	 private OtpSenderService otpService;
	 
	 @Autowired
	 private UsersDao usersDao;
	 
	 @Autowired
	 private FlatOwnersDao flatOwnersDao;
	 
	 @Autowired
	 private FlatResidenciesDao flatResidenciesDao;
	 
	 @Autowired
	 private FlatsDao flatsDao;
	 
	 @Autowired
	    private PushNotificationController pushNotification;
	 
	 @Autowired
	 private ResponsesDao responsesDao;
	
	@PostMapping("/sendSuggestion/{suggestion}")
	public ResponseEntity<?> sendSuggestion(@PathVariable String suggestion,@RequestParam (name = "flatNo",required = false) String flatNo ){	
		if(flatNo=="")
		{	
		List<Flats> flats = flatsDao.findAll();
		for(Flats f : flats)
		{
		 FlatOwners flatOwners = flatOwnersDao.findByownersActive(f.getFlatId(),true);
		 if(flatOwners!=null)
		 {
		 if(flatOwners.getFlatResidencies()==null) {
			Users users = usersDao.findByMobile(flatOwners.getPhone());
			Suggestions suggestions = new Suggestions();
			suggestions.setCreatedAt(new Date());
			suggestions.setSuggestion(suggestion);
			suggestions.setFlats(f);
			suggestionsDao.save(suggestions);
			 System.out.println(flatOwners.getPhone());
			 if(users!=null)
			 {		 
		PushNotificationRequest pushnotificationreq = new PushNotificationRequest();	
		if(users.getToken()!=null)
		{	
		pushnotificationreq.setToken(users.getToken());
		pushnotificationreq.setTitle("You had a message from Building Management ");
		pushnotificationreq.setMessage(suggestions.getSuggestion());
		pushNotification.sendTokenNotification(pushnotificationreq);
		}
		 }
		 }
		 else {
			 FlatResidencies flatResidencies = flatResidenciesDao.findBytenantsActive(f.getFlatId(),true);
			Users users = usersDao.findByMobile(flatResidencies.getPhone());
			Suggestions suggestions = new Suggestions();
			suggestions.setCreatedAt(new Date());
			suggestions.setSuggestion(suggestion);
			suggestions.setFlats(f);
			suggestionsDao.save(suggestions);
			  if(users!=null)
			  {
				PushNotificationRequest pushnotificationreq = new PushNotificationRequest();
				if(users.getToken()!=null)
				{	
				pushnotificationreq.setToken(users.getToken());
				System.out.println(users.getToken());
				pushnotificationreq.setTitle("You had a message from Building Management");
				pushnotificationreq.setMessage(suggestions.getSuggestion());
				pushNotification.sendTokenNotification(pushnotificationreq);
				}
			 otpService.sendSms(flatResidencies.getPhone(), suggestion);
			  }
		 }
	}
		}
		 Responses responses = responsesDao.findById(74);
 		  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));
		}
		else {
			Flats flats= flatsDao.findByflatNo(flatNo);
			FlatOwners flatOwners = flatOwnersDao.findByownersActive(flats.getFlatId(),true);
			 if(flatOwners!=null)
			 {
			 if(flatOwners.getFlatResidencies()==null) {
				Users users = usersDao.findByMobile(flatOwners.getPhone());
				 System.out.println(users.toString());
				 System.out.println(flatOwners.getPhone());
			System.out.println(users.getToken());
			Suggestions suggestions = new Suggestions();
			suggestions.setCreatedAt(new Date());
			suggestions.setSuggestion(suggestion);
			suggestions.setFlats(flats);
			suggestionsDao.save(suggestions);
			PushNotificationRequest pushnotificationreq = new PushNotificationRequest();
			pushnotificationreq.setToken(users.getToken());
			System.out.println(users.getToken());
			pushnotificationreq.setTitle("You had a message from Building Management ");
			pushnotificationreq.setMessage(suggestions.getSuggestion());
			pushNotification.sendTokenNotification(pushnotificationreq);
			 }
			 else {
				 FlatResidencies flatResidencies = flatResidenciesDao.findBytenantsActive(flats.getFlatId(),true);
				Users users = usersDao.findByMobile(flatResidencies.getPhone());
				 System.out.println(users.getToken());
					Suggestions suggestions = new Suggestions();
					suggestions.setCreatedAt(new Date());
					suggestions.setSuggestion(suggestion);
					suggestions.setFlats(flats);
					suggestionsDao.save(suggestions);
					PushNotificationRequest pushnotificationreq = new PushNotificationRequest();
					pushnotificationreq.setToken(users.getToken());
					System.out.println(users.getToken());
					pushnotificationreq.setTitle("You had a message from Building Management");
					pushnotificationreq.setMessage(suggestions.getSuggestion());
					pushNotification.sendTokenNotification(pushnotificationreq);
				 otpService.sendSms(flatResidencies.getPhone(), suggestion);
			 }
		}
		  Responses responses = responsesDao.findById(74);
  		  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));
}
	
	}

	@GetMapping("getSuggestion/{flatNo}")
	public ResponseEntity<?> getSuggestion(@PathVariable String flatNo){
		Flats flats= flatsDao.findByflatNo(flatNo);
		List<Suggestions> suggestions = suggestionsDao.getFlats(flats.getFlatId());
		HashMap<String, List<Suggestions>> response = new HashMap<String,List<Suggestions>>();
		response.put("suggestions", suggestions);
	 return ResponseEntity.ok(response);	
		
	}
	
	@PostMapping("/readingSuggestion/{suggestinId}")
	public ResponseEntity<?> readingSuggestion(@PathVariable long suggestinId )
	{
		Suggestions suggestions = suggestionsDao.findBysuggestinId(suggestinId);
		suggestions.setSuggestionRead(true);
		suggestionsDao.save(suggestions);
		Responses responses = responsesDao.findById(75);
		  return ResponseEntity.ok(new  Responses(responses.getResponsesId(),responses.getResName()));
	}
	
	
	  @GetMapping("/getSuggesyionList/{flatNo}")
	  public ResponseEntity<?> getSuggesyionList(@PathVariable String flatNo) { 
		  Flats flats= flatsDao.findByflatNo(flatNo); 
		  List<Suggestions> suggestionsList = suggestionsDao.findByFlats(flats);
		  List<SuggestionsPojo> suggestionsListPojo = new ArrayList();
	  for(Suggestions suggestion :suggestionsList ) 
	  {
	     SuggestionsPojo suggestionsPojo = new SuggestionsPojo();
	     suggestionsPojo.setSuggestinId(suggestion.getSuggestinId());
	     suggestionsPojo.setSuggestion(suggestion.getSuggestion());
	     suggestionsPojo.setSuggestionRead(suggestion.isSuggestionRead());
	     suggestionsPojo.setFlatNo(suggestion.getFlats().getFlatNo());
	     DateFormat dfCreatedFlat = new SimpleDateFormat("yyyy-MM-dd");
			if(suggestion.getCreatedAt()!=null) {
				suggestionsPojo.setCreatedAt((dfCreatedFlat.format(suggestion.getCreatedAt())));
			}
	     suggestionsListPojo.add(suggestionsPojo);
	  }
	  
	  HashMap<String, List<SuggestionsPojo>> response = new HashMap<String,List<SuggestionsPojo>>();
		response.put("suggestionsListPojo", suggestionsListPojo);
	 return ResponseEntity.ok(response);
 }
	 
	
	
}
