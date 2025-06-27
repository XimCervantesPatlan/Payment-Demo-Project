package com.example.orchestrator.controller;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.orchestrator.model.PaymentResponse;
import com.example.orchestrator.workflows.PaymentWorkflow;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.RetryOptions;

@RestController
@RequestMapping("/api/v1/orchestrator")
public class TransactionController {

    private final WorkflowClient workflowClient;

    public TransactionController(WorkflowClient workflowClient){
        this.workflowClient = workflowClient;
    }

    @PostMapping(value="process-payment")
    public PaymentResponse generateTransaction(@RequestBody Map<String, String> request){
        String processId = UUID.randomUUID().toString().substring(0, 13);

        
        WorkflowOptions options = WorkflowOptions.newBuilder()
                                .setTaskQueue("PROCESS_PAYMENT_QUEUE")
                                .setWorkflowId(processId)
                                .setWorkflowExecutionTimeout(Duration.ofMinutes(2))
                                .setRetryOptions(RetryOptions.newBuilder()
                                    .setInitialInterval(Duration.ofSeconds(1))
                                    .setBackoffCoefficient(2.0)
                                    .setMaximumAttempts(3)
                        .build())
                                .build();

        PaymentWorkflow workflow = workflowClient.newWorkflowStub(PaymentWorkflow.class, options);
        request.put("processId", processId);
        return workflow.startPaymentWorkflow(request);
    }

}
