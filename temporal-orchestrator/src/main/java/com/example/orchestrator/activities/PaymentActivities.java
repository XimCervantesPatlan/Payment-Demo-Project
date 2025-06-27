package com.example.orchestrator.activities;

import java.util.Map;

import com.example.orchestrator.model.PaymentResponse;
import com.example.orchestrator.model.PersistenceResponse;
import com.example.orchestrator.model.ChargerResponse;
import com.example.orchestrator.model.ValidatorResponse;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface PaymentActivities {

    @ActivityMethod
    ValidatorResponse validateAccount(Map<String, String> request);

    @ActivityMethod
    ChargerResponse chargeCard(Map<String, Object> request);

    @ActivityMethod
    PersistenceResponse save(PaymentResponse paymentResponse);
}
