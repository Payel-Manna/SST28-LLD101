import java.util.List;
enum AddOn {
    MESS, LAUNDRY, GYM
}
interface RoomPricing {
    Money monthly();

    String name();
}

class SingleRoom implements RoomPricing {

    public Money monthly() {
        return new Money(14000);
    }

    public String name() {
        return "SINGLE";
    }
}

class DoubleRoom implements RoomPricing {

    public Money monthly() {
        return new Money(15000);
    }

    public String name() {
        return "DOUBLE";
    }
}

class TripleRoom implements RoomPricing {

    public Money monthly() {
        return new Money(12000);
    }

    public String name() {
        return "TRIPLE";
    }
}

class DeluxeRoom implements RoomPricing {

    public Money monthly() {
        return new Money(16000);
    }

    public String name() {
        return "DELUXE";
    }
}

interface AddOnPricing {
    Money price();

    String name();
}

class MessAddOn implements AddOnPricing {

    public Money price() {
        return new Money(1000);
    }

    public String name() {
        return "MESS";
    }

    @Override
    public String toString() {
        return name();
    }
}

class LaundryAddOn implements AddOnPricing {

    public Money price() {
        return new Money(500);
    }

    public String name() {
        return "LAUNDRY";
    }
       
    @Override
    public String toString() {
        return name();
    }

}

class GymAddOn implements AddOnPricing {

    public Money price() {
        return new Money(300);
    }

    public String name() {
        return "GYM";
    }
       
    @Override
    public String toString() {
        return name();
    }
}

class FeeCalculator {

    public Money calculate(RoomPricing room, List<AddOnPricing> addons) {

        Money total = room.monthly();

        for (AddOnPricing a : addons)
            total = total.plus(a.price());

        return total;
    }
}

class ReceiptPrinter {

    public void print(RoomPricing room,
            List<AddOnPricing> addons,
            Money monthly,
            Money deposit) {

        System.out.println("Room: " + room.name() +" | AddOns: " + addons);
        System.out.println("Monthly: " + monthly);
        System.out.println("Deposit: " + deposit);
        System.out.println("TOTAL DUE NOW: " + monthly.plus(deposit));
    }
}
class Money {
   public final double amount;

   public Money(double var1) {
      this.amount = round2(var1);
   }

   public Money plus(Money var1) {
      return new Money(this.amount + var1.amount);
   }

   private static double round2(double var0) {
      return (double)Math.round(var0 * 100.0) / 100.0;
   }

   public String toString() {
      return String.format("%.2f", this.amount);
   }
}
 class FakeBookingRepo {
    public void save(String id, BookingRequest req, Money monthly, Money deposit) {
        System.out.println("Saved booking: " + id);
    }
}class BookingRequest {
    public final int roomType;
    public final List<AddOn> addOns;

    public BookingRequest(int roomType, List<AddOn> addOns) {
        this.roomType = roomType;
        this.addOns = addOns;
    }
}
public class Demo04 {
 public static void main(String[] args)
    {

        System.out.println(
        "=== Hostel Fee Calculator ==="
        );

        RoomPricing room = new DoubleRoom();

        List<AddOnPricing> addons =
        List.of(
            new LaundryAddOn(),
            new MessAddOn()
        );

        FeeCalculator calc = new FeeCalculator();

        Money monthly =
        calc.calculate(room, addons);

        Money deposit =new Money(5000);
        String bookingId ="H-" + (7000 + new java.util.Random(1).nextInt(1000));
        new ReceiptPrinter().print(room, addons,monthly, deposit);
        
        BookingRequest req =new BookingRequest(2,List.of(AddOn.LAUNDRY, AddOn.MESS));
        new FakeBookingRepo()
        .save(bookingId, req, monthly, deposit);
    }
}
