package com.example.orchestrator.workflows;

import java.util.Map;

import com.example.orchestrator.model.PaymentResponse;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface PaymentWorkflow {
    @WorkflowMethod
    PaymentResponse startPaymentWorkflow(Map<String, String> request);
}
