package com.example.orchestrator.activities;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.orchestrator.model.PaymentResponse;
import com.example.orchestrator.model.PersistenceResponse;
import com.example.orchestrator.model.ChargerResponse;
import com.example.orchestrator.model.ValidatorResponse;

@Component
public class PaymentActivitiesImpl implements PaymentActivities{
    
    private final RestTemplate restTemplate;


    public  PaymentActivitiesImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ValidatorResponse validateAccount(Map<String, String> request) {
        String url = "http://ms-validator-charger/api/v1/payment/validate";
        return restTemplate.postForObject(url, request, ValidatorResponse.class);
    }

    @Override
    public ChargerResponse chargeCard(Map<String, Object> request) {
        String url = "http://ms-validator-charger/api/v1/payment/charge";
        return restTemplate.postForObject(url, request, ChargerResponse.class);
    }

    @Override
    public PersistenceResponse save(PaymentResponse paymentResponse) {
        String url = "http://ms-persistence/api/v1/transaction/save";
        return restTemplate.postForObject(url, paymentResponse, PersistenceResponse.class);
    }

}
