package com.dizitiveit.pms.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.dizitiveit.pms.Dao.FlatsDao;
import com.dizitiveit.pms.Dao.TransactionsDao;
import com.dizitiveit.pms.Dao.UsersDao;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.Order;
import com.dizitiveit.pms.model.Transactions;
import com.dizitiveit.pms.model.Users;
import com.dizitiveit.pms.service.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

//@RequestMapping("/paypal")
@RestController
public class PaypalController {
	
	@Autowired
	PaypalService service;
	
	@Autowired
	private UsersDao usersDao;
	
	@Autowired
	private TransactionsDao transactionsDao;
	
	@Autowired
	private FlatsDao flatsDao;
	
	public static final String SUCCESS_URL = "pay/success";
	public static final String CANCEL_URL = "pay/cancel";

	@GetMapping("/")
	public String home() {
		return "home";
	}

	@GetMapping(value = "/pay")
	public ModelAndView payment(@RequestParam(name = "currency") String currency,@RequestParam(name = "price") Double price,@RequestParam(name="mobile") String mobile,@RequestParam(name="flatNo") int flatNo) {
		try {
			System.out.println(mobile);
			Users users = usersDao.findByMobile(mobile);
			System.out.println(users.getMobile());
			System.out.println(users.getFirstName());
			System.out.println(users.getLastName());
			System.out.println(users.getEmail());
			Flats flats = flatsDao.findByflatNo(flatNo);
			ModelAndView modelAndView= new ModelAndView();
			
			Payment payment = service.createPayment(users,price, currency,"paypal",
					"sale", "payment", "http://103.50.161.240:8080/pms-test/" + CANCEL_URL,
					"http://103.50.161.240:8080/pms-test/" + SUCCESS_URL);
			
			for(Links link:payment.getLinks()) {
				if(link.getRel().equals("approval_url")) {
			System.out.println(payment.toJSON());
			System.out.println(payment.getId());
					Transactions transactions = new Transactions();
					transactions.setPaymentId(payment.getId());
					transactions.setUsers(users);
					transactions.setFlats(flats);
					transactions.setCurrency(currency);
					transactions.setAmount(price);
					transactions.setStatus(payment.getState());
					transactions.setCreatedAt(new Date());
					transactionsDao.save(transactions);
					modelAndView = new ModelAndView("redirect:" +link.getHref());
					
				}
			}
			return modelAndView;
		} catch (PayPalRESTException e) {
		
			return null;
		}
		
	}
	
	/*
	 * @GetMapping(value = "/pgredirect") public ModelAndView getRedirect(
	 * 
	 * @RequestParam(name = "TXN_AMOUNT") String transactionAmount,
	 * 
	 * @RequestParam(name = "currency") String currency) throws Exception {
	 * 
	 * System.out.println("PayTM start"); //EhcCustomers user =
	 * userService.getAuthUser(); ModelAndView modelAndView = new
	 * ModelAndView("redirect:" + payTMDetails.getPaytmUrl());
	 * 
	 * EhcUsers user = userRepository.findByMobile(mobile);
	 * 
	 * if (Class.forName("com.dizitiveit.ehc.model.EhcUsers").isInstance(user)) {
	 * 
	 * // ModelAndView modelAndView = new ModelAndView("redirect:" + //
	 * paytmDetails.getPaytmUrl());
	 * 
	 * System.out.println("redirection URL:" + payTMDetails.getPaytmUrl());
	 * 
	 * 
	 * System.out.println("user found");
	 * 
	 * EhcOrders order = new EhcOrders(); EhcSpSlots spSlot =
	 * spSlotsDao.findByIdSpSlots(Integer.parseInt(slotID));
	 * 
	 * order.setIdOrders(EhcUtils.getOrderId());
	 * order.setIdSpSlots(spSlot.getIdSpSlots()); order.setIdSp(spSlot.getIdSp());
	 * order.setTxnamount(Double.parseDouble(transactionAmount));
	 * order.setIdCustomers(user.getIdCustomers()); // order.setIdSp(idSp);
	 * 
	 * order.setStatus("INITIATED"); order = orderDao.save(order);
	 * 
	 * TreeMap<String, String> parameters = new TreeMap<>();
	 * payTMDetails.getDetails().forEach((k, v) -> parameters.put(k, v));
	 * 
	 * parameters.put("MOBILE_NO", user.getMobile()); parameters.put("EMAIL",
	 * user.getEmail()); parameters.put("ORDER_ID", order.getIdOrders());
	 * parameters.put("TXN_AMOUNT", transactionAmount); parameters.put("CUST_ID",
	 * "CUST" + user.getIdCustomers()); parameters.put("CALLBACK_URL",
	 * payTMDetails.getCallbackUrl()+"?MOBILE="+user.getMobile());
	 * 
	 * String checkSum = getCheckSum(parameters); parameters.put("CHECKSUMHASH",
	 * checkSum);
	 * 
	 * System.out.println("CHECKSUMHASH:" + checkSum);
	 * 
	 * modelAndView.addAllObjects(parameters);
	 * 
	 * System.out.println("modelAndView data:" + modelAndView.toString());
	 * 
	 * 
	 * }
	 * 
	 * return modelAndView; }
	 */
	
	 @GetMapping(value = CANCEL_URL)
	    public ModelAndView cancelPay(ModelAndView modelAndView) {
		 modelAndView.setViewName("cancel");
		 return modelAndView;
	    }

	    @GetMapping(value = SUCCESS_URL)
	    public ModelAndView successPay(ModelAndView modelAndView, @RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
	        try {
	        	System.out.println("in success");
	            Payment payment = service.executePayment(paymentId, payerId);
	            Transactions transactions = transactionsDao.findBypaymentId(paymentId);
	            System.out.println(payment.toJSON());
	            if (payment.getState().equals("approved")) {
	            	transactions.setStatus(payment.getState());
	            	transactions.setUpdatedAt(new Date());
	            	transactionsDao.save(transactions);
	            	modelAndView.setViewName("success");
	            	
	            }
	        } catch (PayPalRESTException e) {
	         System.out.println(e.getMessage());
	        }
	        return modelAndView;
	    }

}
