package com.company.rest;

import com.company.dto.LoginDto;
import com.company.dto.MeetingDto;
import com.company.dto.PaymentDto;
import com.company.dto.RegisterDto;
import com.company.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/ud-server/")
public class Controller {
    private static final Logger LOGGER  = LoggerFactory.getLogger(Controller.class);

    @Autowired
    UserService userService;

    @CrossOrigin
    @GetMapping(value = "/start")
    public ResponseEntity workingHttp(){
        return new ResponseEntity("dzialam", HttpStatus.OK);
    }
    @GetMapping(value = "/users")
    public ResponseEntity getUsersJson(){
        return ResponseEntity.ok(userService.getUsers());
    }
    @GetMapping(value = "/meetings")
    public ResponseEntity getMeetingsJson(){
        return ResponseEntity.ok(userService.getMeetings());
    }
    @GetMapping(value = "/meeting_details/{id_meeting}")
    public ResponseEntity getPeopleMeetingJson(@PathVariable Integer id_meeting){
        return ResponseEntity.ok(userService.getPersonMeetingListDto(id_meeting));
    }
    @GetMapping(value = "/meeting_details_code/{code}")
    public ResponseEntity getPeopleMeetingJson(@PathVariable String code){
        return ResponseEntity.ok(userService.getMeetingDetailsCode(code));
    }
    @CrossOrigin
    @PostMapping(value = "/login")
    public ResponseEntity loginUserJson(@RequestBody LoginDto loginDto){
        if(userService.logIn(loginDto)){
            LOGGER.info("---- Logged user: " + userService.getLoggedUser().getName() + " " + userService.getLoggedUser().getSurname()+" ----");
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
    @CrossOrigin
    @PostMapping(value = "/register")
    public ResponseEntity registerUserJson(@RequestBody RegisterDto registerDto){
        if(userService.registerUser(registerDto)){
            LOGGER.info("---- Registered user: " + registerDto.getEmail() +" ----");
            return ResponseEntity.ok().body("Registered user: " + registerDto.getEmail());
        }
        else{
            LOGGER.info("---- User with email: " + registerDto.getEmail() + " already exists in database ----");
            return ResponseEntity.badRequest().body(registerDto.getEmail() + " already exists in database");
        }
    }
    @CrossOrigin
    @PostMapping(value = "/join_meeting/{code}")
    public ResponseEntity joinMeeting(@PathVariable String code){
        if(userService.joinThruCode(code)){
            LOGGER.info("---- User: " + userService.getLoggedUser().getNick() + " joined meeting ----");
            return ResponseEntity.ok().body("---- User: " + userService.getLoggedUser().getNick() + " joined meeting ----");
        }
        else{
            LOGGER.info("---- There is no meeting with code: "+ code +" ----");
            return ResponseEntity.badRequest().body("---- There is no meeting with code: "+ code +" ----");
        }
    }
    @CrossOrigin
    @PostMapping(value = "/payment")
    public ResponseEntity insertPayment(@RequestBody PaymentDto paymentDto){
        if(userService.insertPayment(paymentDto)){
            LOGGER.info("---- Payment registered successfully ----");
            return ResponseEntity.ok().body("Payment registered successfully");
        }
        else{
            LOGGER.info("---- Error occurred ----");
            return ResponseEntity.badRequest().body("Error occurred");
        }
    }
    @GetMapping(value = "/payments_meeting/{id_meeting}")
    public ResponseEntity getPaymentByMeeting(@PathVariable Integer id_meeting){
        return ResponseEntity.ok(userService.getPayments(id_meeting,null,"Meeting"));
    }
    @GetMapping(value = "/payments_person/{id_person}")
    public ResponseEntity getPaymentByPerson(@PathVariable Integer id_person){
        return ResponseEntity.ok(userService.getPayments(null,id_person,"Person"));
    }
//    @CrossOrigin
//    @PostMapping(value = "/registerUser")
//    public ResponseEntity registerUser(@RequestBody AccountData accountData){
//
//
//        return loggingService.register(accountData) ? new ResponseEntity(true,HttpStatus.OK) : new ResponseEntity(HttpStatus.FORBIDDEN);
//    }
//
//    @CrossOrigin
//    @PutMapping(value = "/forgetPasswordIsUser/{nick}")
//    public ResponseEntity forgetPasswordIsUser(@PathVariable String nick){
//
//        Map<String,String> message = loggingService.isUser(nick);
//        return message != null ? new ResponseEntity(message,HttpStatus.OK) : new ResponseEntity(HttpStatus.FORBIDDEN);
//    }
//
//    @CrossOrigin
//    @PutMapping(value = "/forgetPasswordChange")
//    public ResponseEntity forgetPasswordChange(@RequestBody LogginData fpChangeData){
//        loggingService.fpChange(fpChangeData);
//        return  new ResponseEntity(true,HttpStatus.OK);
//    }
//
//    @CrossOrigin
//    @PutMapping(value = "/changePassword")
//    public ResponseEntity changePassword(@RequestBody ChangePasswordDto changePasswordDto){
//        System.out.println("########################################################################################");
//        return loggingService.userChangingPassword(changePasswordDto) ? new ResponseEntity(HttpStatus.OK):new ResponseEntity("Bad password",HttpStatus.FORBIDDEN);
//    }
}
