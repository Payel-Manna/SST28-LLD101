public class UpiPaymentGateway implements PaymentService {
    @Override
    public String charge(String studentId, double amount) {
        return "UPI-TXN-2024";
    }
}