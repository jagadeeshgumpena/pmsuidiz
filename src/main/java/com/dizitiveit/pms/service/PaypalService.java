package com.dizitiveit.pms.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dizitiveit.pms.model.Users;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Service
public class PaypalService {
	
	@Autowired
	private APIContext apiContext;
	
	
	 private static final String CLIENT_ID = "AZkfp0vnBu-mdUMH_k3f8xkw9SEJ0PuXqNWSFMherNf-Ypr5A4-EoLRNFA4o3EunbBj7JzjqEXA1geSR";
	    private static final String CLIENT_SECRET = "EFo7sJdIslKVYhioegnge6yqZriXzBhCd3IKEQ2QQ5z-5QvBpX2xeaq8q5BWph_t0_1p-veo0U0XUCxb";
	    private static final String MODE = "sandbox";
	
	
	public Payment createPayment(
			Users users,
			Double total, 
			String currency, 
			String method,
			String intent,
			String description, 
			String cancelUrl, 
			String successUrl) throws PayPalRESTException{
		Amount amount = new Amount();
		amount.setCurrency(currency);
		total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
		amount.setTotal(String.format("%.2f", total));

		Transaction transaction = new Transaction();
		transaction.setDescription(description);
		transaction.setAmount(amount);

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction);

		Payer payer = new Payer();
		payer.setPaymentMethod(method.toString());
		

		 PayerInfo payerInfo = new PayerInfo();
		    payerInfo.setFirstName(users.getFirstName())
		             .setLastName(users.getLastName())
		             .setEmail(users.getEmail());
		     
		    payer.setPayerInfo(payerInfo);
		    
		Payment payment = new Payment();
		payment.setIntent(intent.toString());
		payment.setPayer(payer);  
		payment.setTransactions(transactions);
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl(cancelUrl);
		redirectUrls.setReturnUrl(successUrl);
		payment.setRedirectUrls(redirectUrls);
		apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);

		return payment.create(apiContext);
	}
	
	public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException{
		Payment payment = new Payment();
		payment.setId(paymentId);
		PaymentExecution paymentExecute = new PaymentExecution();
		paymentExecute.setPayerId(payerId);
		return payment.execute(apiContext, paymentExecute);
	}

}
