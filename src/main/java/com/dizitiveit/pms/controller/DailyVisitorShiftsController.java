package com.dizitiveit.pms.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dizitiveit.pms.Dao.DailyVisitorShiftsDao;
import com.dizitiveit.pms.Dao.DailyVisitorsDao;
import com.dizitiveit.pms.Dao.FlatsDao;
import com.dizitiveit.pms.Dao.ResponsesDao;
import com.dizitiveit.pms.model.DailyVisitors;
import com.dizitiveit.pms.model.DailyVisitorsShifts;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.Responses;

@RestController
@RequestMapping("/dailyVisitorShifts")
public class DailyVisitorShiftsController {

	@Autowired
	private DailyVisitorShiftsDao dailyVisitorsShiftsDao; 
	
	@Autowired
	private FlatsDao flatsDao;
	
	@Autowired
	private ResponsesDao responsesDao;
	
	@Autowired
	private DailyVisitorsDao dailyVisitorsDao;
	
	@PostMapping("/addDailyVisitors/{flatNo}/{dailyVisitorsId}/{startDate}/{visitingDays}/{shiftTime}")
	public ResponseEntity<?> addDailyVisitors(@PathVariable String flatNo,@PathVariable long dailyVisitorsId,@PathVariable String startDate,@PathVariable String visitingDays,@PathVariable String shiftTime){
		DailyVisitorsShifts dailyvisitorsShifts = new DailyVisitorsShifts();
		Date date=null;
			try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(visitingDays.equalsIgnoreCase("One Day"))
		{	
			System.out.println("in oneday");
		DailyVisitors dailyVisitors = dailyVisitorsDao.findById(dailyVisitorsId);
		Flats flats = flatsDao.findByflatNo(flatNo);
		dailyvisitorsShifts.setFlats(flats);
		dailyvisitorsShifts.setDailyVisitors(dailyVisitors);
		dailyvisitorsShifts.setCreatedAt(new Date());
		dailyvisitorsShifts.setStartDate(date);
		dailyvisitorsShifts.setEndDate(date);
		dailyvisitorsShifts.setShiftTime(shiftTime);
		dailyvisitorsShifts.setActive(true);
		dailyVisitorsShiftsDao.save(dailyvisitorsShifts);
		}
		else if(visitingDays == "One Week")
		{
			DailyVisitors dailyVisitors = dailyVisitorsDao.findById(dailyVisitorsId);
			Flats flats = flatsDao.findByflatNo(flatNo);
			dailyvisitorsShifts.setFlats(flats);
			dailyvisitorsShifts.setDailyVisitors(dailyVisitors);
			dailyvisitorsShifts.setCreatedAt(new Date());
			dailyvisitorsShifts.setStartDate(date);
			Date endDate = new Date();
			endDate=DateUtils.addDays(dailyvisitorsShifts.getStartDate(), +7);
				 System.out.println(endDate);
				 dailyvisitorsShifts.setEndDate(endDate);
				 dailyvisitorsShifts.setShiftTime(shiftTime);
				 dailyvisitorsShifts.setActive(true);
			dailyVisitorsShiftsDao.save(dailyvisitorsShifts);
		}
		else if(visitingDays.equalsIgnoreCase("15 Days"))
		{
			DailyVisitors dailyVisitors = dailyVisitorsDao.findById(dailyVisitorsId);
			Flats flats = flatsDao.findByflatNo(flatNo);
			dailyvisitorsShifts.setFlats(flats);
			dailyvisitorsShifts.setDailyVisitors(dailyVisitors);
			dailyvisitorsShifts.setCreatedAt(new Date());
			dailyvisitorsShifts.setStartDate(date);
			Date endDate = new Date();
			endDate=DateUtils.addDays(dailyvisitorsShifts.getStartDate(), +15);
				 System.out.println(endDate);
				 dailyvisitorsShifts.setEndDate(endDate);
				 dailyvisitorsShifts.setShiftTime(shiftTime);
				 dailyvisitorsShifts.setActive(true);
			dailyVisitorsShiftsDao.save(dailyvisitorsShifts);
		}
		else if(visitingDays.equalsIgnoreCase("30 Days"))
		{
			DailyVisitors dailyVisitors = dailyVisitorsDao.findById(dailyVisitorsId);
			Flats flats = flatsDao.findByflatNo(flatNo);
			dailyvisitorsShifts.setFlats(flats);
			dailyvisitorsShifts.setDailyVisitors(dailyVisitors);
			dailyvisitorsShifts.setCreatedAt(new Date());
			dailyvisitorsShifts.setStartDate(date);
			Date endDate = new Date();
			endDate=DateUtils.addDays(dailyvisitorsShifts.getStartDate(), +30);
				 System.out.println(endDate);
				 dailyvisitorsShifts.setEndDate(endDate);
				 dailyvisitorsShifts.setShiftTime(shiftTime);
				 dailyvisitorsShifts.setActive(true);
			dailyVisitorsShiftsDao.save(dailyvisitorsShifts);
		}
		
		Responses responses = responsesDao.findById(89);
		System.out.println("responseId" + responses.getResponsesId());
		System.out.println("resName" + responses.getResName());
		return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName())); 

		
	}
}
