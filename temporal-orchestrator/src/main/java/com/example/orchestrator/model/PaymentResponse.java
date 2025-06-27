package com.example.orchestrator.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private String processId;
    private String accountNumber;
    private double chargedAmount;

    public String getProcessId() {
        return processId;
    }
    public void setProcessId(String processId) {
        this.processId = processId;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public double getChargedAmount() {
        return chargedAmount;
    }
    public void setChargedAmount(double chargedAmount) {
        this.chargedAmount = chargedAmount;
    }
}
