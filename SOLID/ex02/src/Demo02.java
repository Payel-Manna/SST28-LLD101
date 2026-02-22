import java.util.*;
interface TaxRule {
    double getTaxPercent(String customerType);
}

class DefaultTaxRule implements TaxRule{
    public  double getTaxPercent(String customerType) {
        if ("student".equalsIgnoreCase(customerType)) return 5.0;
        if ("staff".equalsIgnoreCase(customerType)) return 2.0;
        return 8.0;
    }
}

interface DiscountRule {
    double getDiscount(String customerType,double subtotal,int distinctLines);
}

class DefaultDiscountRule implements DiscountRule {
    public double getDiscount(String customerType, double subtotal, int distinctLines) {
        
        if ("student".equalsIgnoreCase(customerType)) {
            if (subtotal >= 180.0) return 10.0;
            return 0.0;
        }
        if ("staff".equalsIgnoreCase(customerType)) {
            if (distinctLines >= 3) return 15.0;
            return 5.0;
        }
        return 0.0;
    }
}

class InvoiceFormatter {

    public String format(String invoiceId,
                         List<String> lines,
                         double subtotal,
                         double taxPercent,
                         double tax,
                         double discount,
                         double total)
    {

        StringBuilder out = new StringBuilder();

        out.append("Invoice# ").append(invoiceId).append("\n");

        for(String line : lines)
            out.append(line);

        out.append(String.format("Subtotal: %.2f\n", subtotal));
        out.append(String.format("Tax(%.0f%%): %.2f\n", taxPercent, tax));
        out.append(String.format("Discount: -%.2f\n", discount));
        out.append(String.format("TOTAL: %.2f\n", total));

        return out.toString();
    }
}
class OrderLine {
    public final String itemId;
    public final int qty;

    public OrderLine(String itemId, int qty) {
        this.itemId = itemId; this.qty = qty;
    }
}
class MenuItem {
    public final String id;
    public final String name;
    public final double price;

    public MenuItem(String id, String name, double price) {
        this.id = id; this.name = name; this.price = price;
    }
}


interface InvoiceRepository {

    void save(String id, String content);

    int countLines(String id);

}

class FileStore implements InvoiceRepository{
    private final Map<String, String> files = new HashMap<>();

    public void save(String name, String content) { files.put(name, content); }
    public int countLines(String name) {
        String c = files.getOrDefault(name, "");
        if (c.isEmpty()) return 0;
        return c.split("\n").length;
    }
}

class CafeteriaSystem {

    private final Map<String, MenuItem> menu = new LinkedHashMap<>();

    private final InvoiceRepository store = new FileStore();

    private final TaxRule taxRule = new DefaultTaxRule();

    private final DiscountRule discountRule = new DefaultDiscountRule();

    private final InvoiceFormatter formatter = new InvoiceFormatter();

    private int invoiceSeq = 1000;


    public void addToMenu(MenuItem item) {

        menu.put(item.id, item);

    }


    public void checkout(String customerType, List<OrderLine> orderLines) {

        String invoiceId = "INV-" + (++invoiceSeq);

        List<String> lines = new ArrayList<>();

        double subtotal = 0;


        for (OrderLine line : orderLines) {

            MenuItem item = menu.get(line.itemId);

            double lineTotal = item.price * line.qty;

            subtotal += lineTotal;

            lines.add(String.format("- %s x%d = %.2f\n",
                    item.name,
                    line.qty,
                    lineTotal));

        }


        double taxPercent = taxRule.getTaxPercent(customerType);

        double tax = subtotal * taxPercent / 100;

        double discount = discountRule.getDiscount(customerType,
                subtotal,
                orderLines.size());

        double total = subtotal + tax - discount;


        String invoice = formatter.format(
                invoiceId,
                lines,
                subtotal,
                taxPercent,
                tax,
                discount,
                total
        );


        System.out.print(invoice);

        store.save(invoiceId, invoice);

        System.out.println(
                "Saved invoice: "
                        + invoiceId
                        + " (lines="
                        + store.countLines(invoiceId)
                        + ")"
        );

    }

}
public class Demo02 {
    public static void main(String[] args) {

        System.out.println("=== Cafeteria Billing ===");


        CafeteriaSystem system = new CafeteriaSystem();


        system.addToMenu(
                new MenuItem("M1",
                        "Veg Thali",
                        80.00)
        );

        system.addToMenu(
                new MenuItem("C1",
                        "Coffee",
                        30.00)
        );

        system.addToMenu(
                new MenuItem("S1",
                        "Sandwich",
                        60.00)
        );


        List<OrderLine> order = List.of(

                new OrderLine("M1", 2),

                new OrderLine("C1", 1)

        );


        system.checkout("student", order);

    }

}

