package com.example.orchestrator.workflows;

import java.time.Duration;
import java.util.Map;

import com.example.Main;
import com.example.orchestrator.activities.PaymentActivities;
import com.example.orchestrator.model.PaymentResponse;
import com.example.orchestrator.model.PersistenceResponse;
import com.example.orchestrator.model.ChargerResponse;
import com.example.orchestrator.model.ValidatorResponse;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;

public class PaymentWorkflowImpl implements PaymentWorkflow{

    private static final RetryOptions RETRY_OPTIONS = RetryOptions.newBuilder()
            .setInitialInterval(Duration.ofSeconds(1))
            .setBackoffCoefficient(2.0)
            .setMaximumInterval(Duration.ofSeconds(20))
            .setMaximumAttempts(4)
            .setDoNotRetry("java.lang.IllegalArgumentException.class")
            .build();
    private static final ActivityOptions ACTIVITY_OPTIONS = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(80))
            .setRetryOptions(RETRY_OPTIONS)
            .build();
    private final PaymentActivities activities = Workflow.newActivityStub(
                                    PaymentActivities.class,
                                    ACTIVITY_OPTIONS);

    @Override
    public PaymentResponse startPaymentWorkflow(Map<String, String> request) {
        PaymentResponse response = new PaymentResponse();
        //String processId = UUID.randomUUID().toString().substring(0, 13);

        String clientNumber = request.get("clientNumber");
        Double chargeAmount = Double.parseDouble(request.get("chargeAmount"));
        String processId = request.get("processId");
        String processIdMessage = "[ProcessID:" + processId + "] ";

        response.setProcessId(processId);

        Workflow.getLogger(Main.class).info("Starting payment process with ID: " + processId);

        //1.- Call to the Account Validation Activity Method
        Workflow.getLogger(PaymentWorkflowImpl.class).info( processIdMessage + "Executing validation activity for account " + clientNumber);
        ValidatorResponse validatorResponse = activities.validateAccount(Map.of("accountNumber:", clientNumber));
        
        if(!validatorResponse.getStatus().equals("VALIDATION_OK"))
            manageException(processIdMessage + "validateAccount with problems. Status Code: " + validatorResponse.getStatus());
        
        response.setAccountNumber(clientNumber);
        response.setChargedAmount(chargeAmount);
        Workflow.getLogger(PaymentWorkflowImpl.class).info(processIdMessage + "Account " + clientNumber + " validated successfully");

        //2.- Call to the Charge Card Activity Method
        Workflow.getLogger(PaymentWorkflowImpl.class).info( processIdMessage + "Executing charge activity for account  " + clientNumber + " with amount " + chargeAmount);
        ChargerResponse chargerResponse = activities.chargeCard(Map.of("accountNumber:",clientNumber, 
                                                              "amount", chargeAmount));
        if(!chargerResponse.getStatus().equals("CHARGE_SUCCESS"))
            manageException(processIdMessage + "chargeCard with problems. Status Code: " + validatorResponse.getStatus());
        
        Workflow.getLogger(PaymentWorkflowImpl.class).info( processIdMessage + "Charge successful");

        //3.- Call to the save Activity Method
        Workflow.getLogger(PaymentWorkflowImpl.class).info( processIdMessage + "Executing persistence activity");
        PersistenceResponse persistenceResponse = activities.save(response);
        
        if(!persistenceResponse.getStatus().equals("SAVED"))
            manageException(processIdMessage + "Save payment with problems. Status Code: " + validatorResponse.getStatus());
        
        Workflow.getLogger(PaymentWorkflowImpl.class).info( processIdMessage + "Process completed and saved to DB");

        return response;
    }

    public void manageException(String message){
        Workflow.getLogger(PaymentWorkflowImpl.class).warn(message);
        throw Workflow.wrap(new Exception(message));
    }
}