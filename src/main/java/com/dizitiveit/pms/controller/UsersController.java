
package com.dizitiveit.pms.controller;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import javax.crypto.SecretKey;

import org.bouncycastle.jcajce.provider.symmetric.AES;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.dizitiveit.pms.Dao.BuildingSecurityDao;
import com.dizitiveit.pms.Dao.ConfirmationTokenDao;
import com.dizitiveit.pms.Dao.EmailLinkDao;
import com.dizitiveit.pms.Dao.FlatDetailsDao;
import com.dizitiveit.pms.Dao.FlatOwnersDao;
import com.dizitiveit.pms.Dao.FlatResidenciesDao;
import com.dizitiveit.pms.Dao.FlatsDao;
import com.dizitiveit.pms.Dao.OtpValidationDao;
import com.dizitiveit.pms.Dao.ResponsesDao;
import com.dizitiveit.pms.Dao.SecurityLoginDetailsDao;
import com.dizitiveit.pms.Dao.SecurityShiftsDao;
import com.dizitiveit.pms.Dao.SlotsDao;
import com.dizitiveit.pms.Dao.UsersDao;
import com.dizitiveit.pms.Dao.VehicleDetailsDao;
import com.dizitiveit.pms.model.AuthenticationRequest;
import com.dizitiveit.pms.model.AuthenticationResponse;
import com.dizitiveit.pms.model.BuildingSecurity;
import com.dizitiveit.pms.model.ConfirmationToken;
import com.dizitiveit.pms.model.EmailLink;
import com.dizitiveit.pms.model.FlatDetails;
import com.dizitiveit.pms.model.FlatOwners;
import com.dizitiveit.pms.model.FlatResidencies;
import com.dizitiveit.pms.model.Flats;
import com.dizitiveit.pms.model.OtpValidation;
import com.dizitiveit.pms.model.Residents;
import com.dizitiveit.pms.model.ResidentsDetails;
import com.dizitiveit.pms.model.Responses;
import com.dizitiveit.pms.model.SecurityAuthenticationResponse;
import com.dizitiveit.pms.model.SecurityLoginDetails;
import com.dizitiveit.pms.model.SecurityShifts;
import com.dizitiveit.pms.model.Slots;
import com.dizitiveit.pms.model.Transactions;
import com.dizitiveit.pms.model.Users;
import com.dizitiveit.pms.model.VehicleDetails;
import com.dizitiveit.pms.pojo.OwnerFlatDetails;
import com.dizitiveit.pms.pojo.ResidenciesFlatDetails;
import com.dizitiveit.pms.pojo.VehicleDetailsList;
import com.dizitiveit.pms.pojo.VehicleDetailsPojo;
import com.dizitiveit.pms.service.EmailServiceImpl;
import com.dizitiveit.pms.service.MyUserDetailsService;
import com.dizitiveit.pms.service.OtpSenderService;
import com.dizitiveit.pms.util.JwtUtil;
//import com.dizitiveit.ipay.util.AESDecrypt;
//import com.dizitiveit.ipay.util.SecretKeyAES;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.LoadingCache;

@RestController
@RequestMapping("user")
public class UsersController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MyUserDetailsService userDetailsService;

	@Autowired
	private UsersDao usersDao;

	@Autowired
	private FlatsDao flatsDao;

	@Autowired
	private OtpValidationDao otpValidationDao;

	@Autowired
	private ConfirmationTokenDao confirmationTokenDao;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private OtpSenderService otpService;

	@Autowired
	private EmailServiceImpl emailServiceImpl;

	@Autowired
	private EmailLinkDao emailLinkDao;

	@Autowired
	private ResponsesDao responsesDao;

	@Autowired
	private FlatResidenciesDao flatResidenciesDao;

	@Autowired
	private FlatOwnersDao flatOwnersDao;

	@Autowired
	private BuildingSecurityDao buildingSecurityDao;

	@Autowired
	private FlatDetailsDao flatDetailsDao;

	@Autowired
	private SecurityLoginDetailsDao securityLoginDetailsDao;

	@Autowired
	private SecurityShiftsDao securityShiftsDao;

	@Autowired
	private VehicleDetailsDao vehicleDetailsDao;

	@Autowired
	private SlotsDao slotsDao;

	private LoadingCache<String, Integer> otpCache;

	/*
	 * @Autowired private ConfirmationTokenDao confirmationTokenDao;
	 */
	private static final Charset UTF_8 = StandardCharsets.UTF_8;

	@Transactional

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {
		Authentication authentication = null;

		try {
			System.out.println(authenticationRequest.getUsername());
			System.out.println(authenticationRequest.getPassword());
			// String encryptedString =
			// aesEcbService.encrypt(authenticationRequest.getUsername(), secretKey) ;
			// System.out.println("encrypted is"+encryptedString);
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));

			System.out.println("text");
			final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
			System.out.println(userDetails.toString());
			SecurityContextHolder.getContext().setAuthentication(authentication);
			Users user = usersDao.findByEmail(authenticationRequest.getUsername());
			if (user == null) {

				user = usersDao.findByMobile(authenticationRequest.getUsername());
			}

			// System.out.println(userDetails.toString());
			if (user != null && user.getMobileVerification() == true) {
				final String jwt = jwtTokenUtil.generateToken(userDetails);

				System.out.println(jwt);

				return ResponseEntity.ok(
						new AuthenticationResponse(jwt, user.getRoles(), user.getFirstName(), user.getLastName(), ""));
			}

			else {
				Responses responses = responsesDao.findById(9);
				System.out.println("responseId" + responses.getResponsesId());
				System.out.println("resName" + responses.getResName());

				return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
				// return ResponseEntity.status(200).body("Please verify the mobile number");
			}

		}

		catch (BadCredentialsException e) {
			System.out.println("error in checking password");
			// throw new Exception("Incorrect username or password",e);
			Responses responses = responsesDao.findById(8);
			System.out.println("responseId" + responses.getResponsesId());
			System.out.println("resName" + responses.getResName());

			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
			// return ResponseEntity.badRequest().body("Incorrect username or password");

		} catch (Exception E) {
			// String
			// System.out.println(E.printStackTrace());
			E.printStackTrace();
			// System.out.println("error in checking password2");
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			Responses responses = responsesDao.findById(16);
			// System.out.println("responseId" + responses.getResponsesId());
			// System.out.println("resName" + responses.getResName());
			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
			// return ResponseEntity.status(200).body("Please verify the mobile number ");
		}

	}

	@PostMapping("/managerRegistration")
	public ResponseEntity<?> managerRegistration(@RequestBody Users users) {
		String email = users.getEmail();
		String mobile = users.getMobile();
		String firstName = users.getFirstName();
		String lastName = users.getLastName();
		String roles = users.getRoles();
		String password = users.getPassword();
		Users users1 = new Users();
		users1 = usersDao.findByEmail(email);

		if (users1 != null) {
			Responses responses = responsesDao.findById(13);
			// System.out.println("responseId"+responses.getResponsesId());
			// System.out.println("resName"+responses.getResName());

			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		}
		users1 = usersDao.findByMobile(mobile);
		// System.out.println("Inside sendOtp mobile:"+user.toString());

		if (users1 != null) {

			Responses responses = responsesDao.findById(14);
			// System.out.println("responseId"+responses.getResponsesId());
			// System.out.println("resName"+responses.getResName());

			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		}
		users1 = new Users();
		users1.setEmail(email);
		users1.setFirstName(firstName);
		users1.setLastName(lastName);
		users1.setMobile(mobile);
		users1.setRoles(roles);
		users1.setMobileVerification(true);
		users1.setActive(true);
		users1.setPassword(password);
		long userId = userDetailsService.registerNewUser(users1);
		Responses responses = responsesDao.findById(4);
		return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
	}

	@PostMapping(value = "/validatesignup/{mobile}/{otpNum}")
	public ResponseEntity<?> validateOtp(@PathVariable String mobile, @PathVariable String otpNum) {
		int serverOtp = 999999;
		// int serverOtp = otpService.getOtp(mobile);
		// System.out.println("Pathvariable otp is" + otpNum);
		// System.out.println("Server otp is" + serverOtp);
		int intOtp = Integer.parseInt(otpNum);
		// System.out.println("intotp is"+intOtp);
		if (serverOtp > 0) {
			if (intOtp == serverOtp) {
				otpService.clearOTP(mobile);
				Users usersNew = new Users();
				usersNew.setMobile(mobile);
				usersNew.setMobileVerification(true);
				usersNew.setActive(true);
				usersDao.save(usersNew);
				Responses responses = responsesDao.findById(5);
				// System.out.println("responseId"+responses.getResponsesId());
				// System.out.println("resName"+responses.getResName());

				return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));

			} else {
				Responses responses = responsesDao.findById(6);
				System.out.println("responseId" + responses.getResponsesId());
				System.out.println("resName" + responses.getResName());

				return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
				// "invalid otp"

			}
		} else {
			Responses responses = responsesDao.findById(7);
			System.out.println("responseId" + responses.getResponsesId());
			System.out.println("resName" + responses.getResName());

			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
			// return ResponseEntity.status(200).body("Otp Time Expired");

		}
	}

	@PostMapping(value = "validateotp/{mobile}/{otpNum}/{reqType}/{token}")
	public ResponseEntity<?> validateOtp(@PathVariable String mobile, @PathVariable String otpNum,
			@PathVariable String reqType, @PathVariable String token,
			@RequestParam(name = "purpose", required = false) String purpose) {
		Users users = usersDao.findByMobile(mobile);
		// int serverOtp = otpService.getOtp(mobile);
		int serverOtp = 999999;
		System.out.println("Pathvariable otp is" + otpNum);
		System.out.println("Server otp is" + serverOtp);
		int intOtp = Integer.parseInt(otpNum);
		System.out.println("intotp is" + intOtp);
		if (serverOtp > 0) {
			if (intOtp == serverOtp) {
				otpService.clearOTP(mobile);

				if (reqType.equalsIgnoreCase("Login") && users.isActive() == true) {
					users.setToken(token);
					usersDao.save(users);
					System.out.println("In Login if condition");
					// users.setActive(true);
					// users.setLoginVerification(true);
					// usersDao.save(users);

					final UserDetails userDetails = userDetailsService.loadUserByUsername(users.getMobile());
					final String jwt = jwtTokenUtil.generateToken(userDetails);
					System.out.println(jwt);
					System.out.println(users.getRoles());
					if (users.getRoles().equalsIgnoreCase("ROLE_SECURITY")) {
						BuildingSecurity buildingSecurity = buildingSecurityDao.findByMobile(mobile);
						// Date date= new Date();
						// SecurityShifts securityShift =
						// securityShiftsDao.findByLoginDate(buildingSecurity.getSecurityId(),date,date);
						if (mobile != null) {
							/*
							 * SecurityLoginDetails securityLogin =
							 * securityLoginDetailsDao.findByShiftSlotBySecurityId(buildingSecurity.
							 * getSecurityId(),"OUT") securityLogin.setInTime(new Date());
							 * securityLogin.setBuildingSecurity(buildingSecurity);
							 * securityLogin.setPurpose(purpose);
							 * securityLoginDetailsDao.save(securityLogin);
							 */
							return ResponseEntity.ok(new AuthenticationResponse(jwt, users.getRoles(),
									users.getFirstName(), users.getLastName(), ""));
						}
					} else if (users.getRoles().equalsIgnoreCase("ROLE_OWNER")) {
						FlatOwners flatOwners = flatOwnersDao.findByPhone(users.getMobile());
						System.out.println(users.getMobile());
						return ResponseEntity.ok(new AuthenticationResponse(jwt, users.getRoles(), users.getFirstName(),
								users.getLastName(), flatOwners.getFlats().getFlatNo()));
					} else if (users.getRoles().equalsIgnoreCase("ROLE_TENANT")) {
						System.out.println(users.getRoles());
						FlatResidencies flatResidencies = flatResidenciesDao.findByPhone(users.getMobile());
						return ResponseEntity.ok(new AuthenticationResponse(jwt, users.getRoles(), users.getFirstName(),
								users.getLastName(), flatResidencies.getFlats().getFlatNo()));
					}
					return null;
				}

				else {
					// users.setActive(true);
					// usersDao.save(users);
					Responses responses = responsesDao.findById(31);
					System.out.println("responseId" + responses.getResponsesId());
					System.out.println("resName" + responses.getResName());

					return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
					// return ResponseEntity.status(200).body("Your Account was locked you cant
					// login");

				}

			} else {
				Responses responses = responsesDao.findById(6);
				System.out.println("responseId" + responses.getResponsesId());
				System.out.println("resName" + responses.getResName());

				return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
				// return ResponseEntity.status(200).body("Enter valid otp");

			}
		}

		else {
			Responses responses = responsesDao.findById(7);
			System.out.println("responseId" + responses.getResponsesId());
			System.out.println("resName" + responses.getResName());

			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
			// return ResponseEntity.status(200).body("Otp Time Expired");
		}

	}

	@GetMapping(value = "/signupOtp/{mobile}")
	public ResponseEntity<?> signupOtp(@PathVariable String mobile) {
		Users users = usersDao.findByMobile(mobile);
		if (users == null) {
			// String otpNumber = "-1";
			// String message = "Your One Time Registration code is: ";
			// otpNumber = otpService.sendOtp(mobile, message);
			String otpNumber = "999999";
			int intOtp = Integer.parseInt(otpNumber);
			otpCache.put(mobile, intOtp);
			OtpValidation otpval = otpValidationDao.findBymobile(mobile);
			if (otpval != null) {
				otpval.setOtp(otpNumber);
				otpval.setMobile(mobile);
				otpval.setStatus("Otp Sent");
				otpval = otpValidationDao.save(otpval);
			} else {
				OtpValidation otpval1 = new OtpValidation();
				otpval1.setOtp(otpNumber);
				/// System.out.println("current otp is" + otpNumber);
				otpval1.setMobile(mobile);
				// System.out.println("mobile number is" + mobile);
				otpval1.setStatus("Otp Sent");
				// System.out.println("mobile number is" + mobile);
				otpval1 = otpValidationDao.save(otpval1);
			}
			Responses responses = responsesDao.findById(2);
			System.out.println("responseId" + responses.getResponsesId());
			System.out.println("resName" + responses.getResName());

			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		}

		else {

			Responses responses = responsesDao.findById(14);
			System.out.println("responseId" + responses.getResponsesId());
			System.out.println("resName" + responses.getResName());

			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));

			// return ResponseEntity.status(400).body("Mobile Already present");
			// "mobile already exists"
		}

	}

	@GetMapping(value = "/sendOtp/{mobile}/{signature}")
	public ResponseEntity<?> sendOtp(@PathVariable String mobile, @PathVariable String signature) {
		System.out.println("Inside method");
		String otpNumber = "-1";
		// String message = "<#> Your PMS OTP Code is:" ;
		String message = signature;

		try {

			// check for mobile and return if found
			Users user = usersDao.findByMobile(mobile);
			// send OTP thru SMS
			 //otpNumber = otpService.sendOtp(mobile, message);

			otpNumber = "999999";
			//otpCache.put(mobile,999999);
			if (user != null) {
				if (user.isActive() == true) {

					// send OTP thru SMS
					// otpNumber = otpService.sendOtp(mobile, message);

					OtpValidation otpval = otpValidationDao.findBymobile(mobile);
					if (otpval != null) {
						otpval.setOtp(otpNumber);
						otpval.setMobile(mobile);
						otpval.setStatus("Otp Sent");
						otpval.setSignature(signature);
						otpval = otpValidationDao.save(otpval);
					} else {
						OtpValidation otpval1 = new OtpValidation();
						otpval1.setOtp(otpNumber);
						System.out.println("current otp is" + otpNumber);
						otpval1.setMobile(mobile);
						System.out.println("mobile number is" + mobile);
						otpval1.setStatus("Otp Sent");
						System.out.println("mobile number is" + mobile);
						otpval1.setSignature(signature);
						otpval1 = otpValidationDao.save(otpval1);
					}
					Responses responses = responsesDao.findById(2);
					System.out.println("responseId" + responses.getResponsesId());
					System.out.println("resName" + responses.getResName());

					return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
				} else {
					Responses responses = responsesDao.findById(31);
					System.out.println("responseId" + responses.getResponsesId());
					System.out.println("resName" + responses.getResName());
					return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
					// return ResponseEntity.status(200).body("Your Account was locked you cant
					// login");
				}
			} else {
				Responses responses = responsesDao.findById(3);
				System.out.println("responseId" + responses.getResponsesId());
				System.out.println("resName" + responses.getResName());
				return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
				// return ResponseEntity.badRequest().body("Mobile Number Doesnt Exist");
			}

		} catch (Exception E) {

			E.printStackTrace();
			return ResponseEntity.badRequest().body("Internal Server Error");
		}

	}

	@Transactional
	@GetMapping(value = "/updatePass/{auth}")
	public ResponseEntity<?> updatePass(@PathVariable String auth) {
		byte[] dataBytes = Base64.getDecoder().decode(auth.getBytes());
		String decoded = new String(dataBytes);
		String[] strArr = decoded.split(":");
		String phonecountrycode = strArr[0];
		String mobile = strArr[1];
		String password = strArr[2];

		Users users = usersDao.findByMobile(mobile);
		System.out.println("password is" + password);
		String newpassword = passwordEncoder.encode(password);
		System.out.println(" new password is" + newpassword);
		usersDao.save(users);
		Responses responses = responsesDao.findById(10);
		System.out.println("responseId" + responses.getResponsesId());
		System.out.println("resName" + responses.getResName());

		return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		// return ResponseEntity.status(200).body("Password Sucessfully Updated ");

	}

	@PostMapping(value = "/sendEmail/{email}")
	public ResponseEntity<?> sendEmail(@PathVariable String email) {
		System.out.println(email);
		Users users = usersDao.findByEmail(email);
		Date date = new Date();
		if ((users != null)) {
			ConfirmationToken confirmationToken = new ConfirmationToken(users);
			String token = confirmationToken.getConfirmationToken();
			confirmationToken.setUsers(users);
			confirmationToken.setCreatedDate(date);
			System.out.println("token is" + token);
			System.out.println("email is" + users.getEmail());
			System.out.println("user id is" + users.getUserId());

			EmailLink link = emailLinkDao.findByvalue("Registration");
			System.out.println(link);

			String email_token = String.valueOf(users.getUserId()) + token + String.valueOf(date.getTime());
			confirmationToken.setConfirmationToken(email_token);

			confirmationTokenDao.save(confirmationToken);

			String message = "To confirm your account, please click here : " + link.getLink() + email_token;
			emailServiceImpl.sendSimpleMessage(users.getEmail(), "PinPae Confirmation", message);
			System.out.println("Email sent:" + users.getEmail());

		}
		/*
		 * ConfirmationToken confirmationToken2=confirmationTokenDao.findByusers(users);
		 * 
		 * if(confirmationToken2==null) { ConfirmationToken confirmationToken1= new
		 * ConfirmationToken(); confirmationToken1.setConfirmationToken(token);
		 * confirmationToken1.setUsers(users); confirmationToken1.setCreatedDate(new
		 * Date()); confirmationTokenDao.save(confirmationToken1); } else {
		 * confirmationToken2.setConfirmationToken(token);
		 * confirmationToken2.setCreatedDate(new Date());
		 * confirmationToken2.setUsers(users);
		 * confirmationTokenDao.save(confirmationToken2); }
		 * 
		 */
		Responses responses = responsesDao.findById(11);
		System.out.println("responseId" + responses.getResponsesId());
		System.out.println("resName" + responses.getResName());

		return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		// return ResponseEntity.status(200).body("Email Sent");
	}

	@RequestMapping(value = "/confirm-account", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token") String confirmationToken) {

		ConfirmationToken token = confirmationTokenDao.findByConfirmationToken(confirmationToken);

		if (token != null) {
			long diffInMilliSec = new Date().getTime() - token.getCreatedDate().getTime();
			long days = (diffInMilliSec / (1000 * 60 * 60 * 24)) % 365;
			long hours = (diffInMilliSec / (1000 * 60 * 60)) % 24;
			System.out.println("No of Days" + days);
			System.out.println("No of Hours" + hours);
			if (days < 1) {
				Users users1 = usersDao.findByEmail(token.getUsers().getEmail());
				users1.setActive(true);
				usersDao.save(users1);
				modelAndView.setViewName("emailVerified");
			} else {
				modelAndView.addObject("message", "The Link Is Invalid Or Broken!");
				modelAndView.setViewName("Error");

			}
		} else {
			modelAndView.addObject("message", "The Link Is Invalid Or Broken!");
			modelAndView.setViewName("Error");
		}

		return modelAndView;

	}

	@PostMapping(value = "/disableAccount/{mobile}")
	public ResponseEntity<?> disableAccount(@PathVariable String mobile) {
		Users users = usersDao.findByMobile(mobile);
		if (users != null) {
			users.setActive(false);
			this.usersDao.save(users);
			Responses responses = responsesDao.findById(16);
			System.out.println("responseId" + responses.getResponsesId());
			System.out.println("resName" + responses.getResName());

			return ResponseEntity.ok(new Responses(responses.getResponsesId(), responses.getResName()));
		}

		else

			return ResponseEntity.status(200).body("User not found");
	}

	@GetMapping(value = "/getUsers")
	public ResponseEntity<?> getUsers() {
		Users users = userDetailsService.getAuthUser();
		System.out.println(users.getMobile());
		if (users != null) {
			Users usersDetails = usersDao.findByMobile(users.getMobile());
			return ResponseEntity.status(200).body(usersDetails);
		} else {
			return ResponseEntity.status(200).body("User not found");
		}

	}

	@GetMapping("/getResidentsDetails")
   public ResponseEntity<?> getResidentsDetails(){
	   Users users = userDetailsService.getAuthUser();
	   System.out.println(users.toString());
	   System.out.println(users.getRoles());
	   Date date = new Date();
		 System.out.println(date);
		
	   if(users.getRoles().equalsIgnoreCase("ROLE_OWNER")) {
		   OwnerFlatDetails ownerFlatDetails = new OwnerFlatDetails();
	   FlatOwners flatOwners = flatOwnersDao.findByownersPhone(users.getMobile(),true);
	   System.out.println(flatOwners);
	   ownerFlatDetails.setFlatOwners(flatOwners);
	   if(flatOwners.getFlatResidencies()==null) {
		   List<Slots> slots = slotsDao.findByflatId(flatOwners.getFlats().getFlatId());
		   ownerFlatDetails.setSlots(slots);	
	   }
	   HashMap<String,OwnerFlatDetails> response = new HashMap<String,OwnerFlatDetails>();
	   response.put("ownerFlatDetails", ownerFlatDetails);
	   System.out.println(ownerFlatDetails);
	   return ResponseEntity.status(200).body(response);
	   }
	   else if(users.getRoles().equalsIgnoreCase("ROLE_TENANT")){
		   
		   ResidenciesFlatDetails flatResidenciesDetails= new ResidenciesFlatDetails();
		   FlatResidencies flatResidencies = flatResidenciesDao.findBytenantsPhone(users.getMobile(),true);	  
            FlatOwners flatOwners = flatOwnersDao.findByFlatResidencies(flatResidencies);       
 		   List<Slots> slots = slotsDao.findByflatId(flatResidencies.getFlats().getFlatId());
 		  flatResidenciesDetails.setSlots(slots);
 		 flatResidenciesDetails.setFlatResidencies(flatResidencies);
		   flatResidenciesDetails.setFlatResidencies(flatResidencies);
		 //  response.put("flatResidenciesDetails",flatResidenciesDetails);
		   HashMap<String,ResidenciesFlatDetails> response = new HashMap<String,ResidenciesFlatDetails>();
		   response.put("flatResidenciesDetails", flatResidenciesDetails);
		   System.out.println(flatResidencies);
		   return ResponseEntity.status(200).body(response);
	   }
	   else {
		   return ResponseEntity.status(200).body("User not found");
	   }
   }

	@PostMapping(value = "/saveToken/{token}")
	public ResponseEntity<?> saveToken(@PathVariable String token) {
		Users users = userDetailsService.getAuthUser();
		Users usersDetails = usersDao.findByMobile(users.getMobile());
		users.setToken(token);
		usersDao.save(users);
		return ResponseEntity.status(200).body("token saved sucessfully");
	}

	/*
	 * @GetMapping("/listOfResidents") public ResponseEntity<?>
	 * listOfResidentsByType() { List<Residents> residents= new ArrayList();
	 * 
	 * List<Flats> flats= flatsDao.findAll();
	 * 
	 * for(Flats f : flats) { Residents resident= new Residents();
	 * resident.setFlatNo(f.getFlatNo());
	 * 
	 * Users ownerUser=usersDao.GetByFlatdIdAndType(f.getFlatId(),"Owner"); Users
	 * tenantUser=usersDao.GetByFlatdIdAndType(f.getFlatId(),"Tenent");
	 * resident.setOwners(ownerUser); resident.setTenents(tenantUser);
	 * 
	 * residents.add(resident);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * HashMap<String, List<Residents>> response = new
	 * HashMap<String,List<Residents>>(); response.put("residents", residents);
	 * return ResponseEntity.ok(response);
	 * 
	 * 
	 * }
	 */

	@GetMapping(value = "/getResponses")
	public ResponseEntity<?> getResponses() {
		List<Responses> listResponses = responsesDao.findAll();

		return ResponseEntity.status(200).body(listResponses);
	}

}