public class TransportBookingService {
    // DIP violation: direct concretes
    private final DistanceService distanceService;
    private final DriverService driverService;
    private final PaymentService paymentService;
    private final PricingPolicy pricingPolicy;

    public TransportBookingService(DistanceService distanceService, DriverService driverService, PaymentService paymentService, PricingPolicy pricingPolicy) {
        this.distanceService = distanceService;
        this.driverService = driverService;
        this.paymentService = paymentService;
        this.pricingPolicy = pricingPolicy;
    }

    public void book(TripRequest req) {
        double km = distanceService.km(req.from, req.to);
        System.out.println("DistanceKm=" + km);

        String driver = driverService.allocate(req.studentId);
        System.out.println("Driver=" + driver);

        double fare = pricingPolicy.calculate(km);

        String txn = paymentService.charge(req.studentId, fare);
        System.out.println("Payment=PAID txn=" + txn);

        //BookingReceipt is a simple data object (DTO / value object)
        //No behavior-> does not represent infrastructure and does not call external sys
        // in DIP=>High-level business logic depending on low-level infrastructure details
        BookingReceipt r = new BookingReceipt("R-501", fare);
        System.out.println("RECEIPT: " + r.id + " | fare=" + String.format("%.2f", r.fare));
    }
}
