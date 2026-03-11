//package org.russel.komandosb.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;;
//
//
//@RequestMapping("/api/test")
//@RestController
//public class TestController {
//
//    @Autowired
//    private FcmService fcmService;
//
//    @PostMapping("/notification")
//    public ResponseEntity<?> sendTokenTest(@RequestParam String token){
//        try {
//            fcmService.sendTestNotification(token);
//            return ResponseEntity.ok("Test notification sent to token!");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(e.getMessage());
//        }
//    }
//}
