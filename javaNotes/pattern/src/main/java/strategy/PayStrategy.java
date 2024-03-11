package strategy;

/**
 *策略接口
 */

public interface PayStrategy {
    boolean pay(int paymentAmount);
    void collectPaymentDetails();
}
