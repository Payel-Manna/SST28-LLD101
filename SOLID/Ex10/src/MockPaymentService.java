//To implement a mock payment service for testing without changing the booking logic
public class MockPaymentService implements PaymentService {
    @Override
    public String charge(String studentId, double amount) {
        // Simulate a successful payment
        return "MOCK-TXN-1234";
    }
}
