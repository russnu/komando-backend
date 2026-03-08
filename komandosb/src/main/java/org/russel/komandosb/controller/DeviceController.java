package org.russel.komandosb.controller;

import org.russel.komandosb.data.entity.UserData;
import org.russel.komandosb.data.entity.UserDeviceData;
import org.russel.komandosb.data.model.request.FcmTokenRequest;
import org.russel.komandosb.data.repository.UserDeviceRepository;
import org.russel.komandosb.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    private UserDeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerDevice(@AuthenticationPrincipal UserData user, @RequestBody FcmTokenRequest request){
        Optional<UserDeviceData> existing =
                deviceRepository.findByFcmToken(request.getToken());

        if (existing.isPresent()) {
            return ResponseEntity.ok("Token already exists");
        }

        UserDeviceData device = new UserDeviceData();
        device.setFcmToken(request.getToken());
        device.setUser(user);

        deviceRepository.save(device);

        return ResponseEntity.ok("Device registered");

    }

    @DeleteMapping("/unregister")
    public ResponseEntity<?> unregisterDevice(@AuthenticationPrincipal UserData user,
                                              @RequestBody FcmTokenRequest request) {
        Optional<UserDeviceData> existing = deviceRepository.findByFcmToken(request.getToken());

        existing.ifPresent(deviceRepository::delete);

        return ResponseEntity.ok("Device unregistered");
    }

}
