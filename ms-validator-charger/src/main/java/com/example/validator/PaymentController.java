
package com.example.validator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @PostMapping("/validate")
    public ResponseEntity<?> validateAccount(@RequestBody Map<String, String> request) {
        System.out.println(request);
        return ResponseEntity.ok(Map.of(
            "status", "VALIDATION_OK",
            "message", "Cuenta validada exitosamente"
        ));
    }

    @PostMapping("/charge")
    public ResponseEntity<?> chargeCard(@RequestBody Map<String, Object> request) {
        System.out.println(request);
        return ResponseEntity.ok(Map.of(
            "status", "CHARGE_SUCCESS",
            "transactionId", UUID.randomUUID().toString()
        ));
    }
}
