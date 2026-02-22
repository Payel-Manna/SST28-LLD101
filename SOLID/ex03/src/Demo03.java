import java.util.*;
class StudentProfile {
    public final String rollNo;
    public final String name;
    public final double cgr;
    public final int attendancePct;
    public final int earnedCredits;
    public final int disciplinaryFlag;

    public StudentProfile(String rollNo, String name, double cgr, int attendancePct, int earnedCredits, int disciplinaryFlag) {
        this.rollNo = rollNo; this.name = name; this.cgr = cgr;
        this.attendancePct = attendancePct; this.earnedCredits = earnedCredits;
        this.disciplinaryFlag = disciplinaryFlag;
    }
}
class LegacyFlags {
    public static final int NONE = 0;
    public static final int WARNING = 1;
    public static final int SUSPENDED = 2;

    public static String nameOf(int f) {
        return switch (f) {
            case WARNING -> "WARNING";
            case SUSPENDED -> "SUSPENDED";
            default -> "NONE";
        };
    }
}
class EligibilityEngineResult {

    public final String status;

    public final List<String> reasons;

    public EligibilityEngineResult(String status, List<String> reasons) {

        this.status = status;

        this.reasons = reasons;
    }
}
class Numbers {
    public static double clamp(double x, double a, double b) {
        return Math.max(a, Math.min(b, x));
    }
}
interface EligibilityRule {

    boolean isEligible(StudentProfile student);

    String reason();
}
class DisciplinaryRule implements EligibilityRule {

    public boolean isEligible(StudentProfile student) {

        return student.disciplinaryFlag == LegacyFlags.NONE;
    }

    public String reason() {

        return "disciplinary flag present";
    }
}
class CgrRule implements EligibilityRule {

    private final double minCgr;

    public CgrRule(double minCgr) {

        this.minCgr = minCgr;
    }

    public boolean isEligible(StudentProfile student) {

        return student.cgr >= minCgr;
    }

    public String reason() {

        return "CGR below " + minCgr;
    }
}
class AttendanceRule implements EligibilityRule {

    private final int minAttendance;

    public AttendanceRule(int minAttendance) {

        this.minAttendance = minAttendance;
    }

    public boolean isEligible(StudentProfile student) {

        return student.attendancePct >= minAttendance;
    }

    public String reason() {

        return "attendance below " + minAttendance;
    }
}
class CreditsRule implements EligibilityRule {

    private final int minCredits;

    public CreditsRule(int minCredits) {

        this.minCredits = minCredits;
    }

    public boolean isEligible(StudentProfile student) {

        return student.earnedCredits >= minCredits;
    }

    public String reason() {

        return "credits below " + minCredits;
    }
}

class FakeEligibilityStore {
    public void save(String roll, String status) {
        System.out.println("Saved evaluation for roll=" + roll);
    }
}
class RuleInput {
    public double minCgr = 8.0;
    public int minAttendance = 75;
    public int minCredits = 20;
}
class ReportPrinter {
    public void print(StudentProfile s, EligibilityEngineResult r) {
        System.out.println("Student: " + s.name + " (CGR=" + String.format("%.2f", s.cgr)
                + ", attendance=" + s.attendancePct + ", credits=" + s.earnedCredits
                + ", flag=" + LegacyFlags.nameOf(s.disciplinaryFlag) + ")");
        System.out.println("RESULT: " + r.status);
        for (String reason : r.reasons) System.out.println("- " + reason);
        if (r.reasons.isEmpty() && "ELIGIBLE".equals(r.status)) {
            // keep behavior stable even if empty
        }
    }
}
class EligibilityEngine {

    private final FakeEligibilityStore store;

    private final List<EligibilityRule> rules;

    public EligibilityEngine(FakeEligibilityStore store) {

        this.store = store;

        RuleInput config = new RuleInput();

        rules = List.of(

                new DisciplinaryRule(),

                new CgrRule(config.minCgr),

                new AttendanceRule(config.minAttendance),

                new CreditsRule(config.minCredits)
        );
    }

    public void runAndPrint(StudentProfile s) {

        ReportPrinter p = new ReportPrinter();

        EligibilityEngineResult r = evaluate(s);

        p.print(s, r);

        store.save(s.rollNo, r.status);
    }

    public EligibilityEngineResult evaluate(StudentProfile s) {

        List<String> reasons = new ArrayList<>();

        String status = "ELIGIBLE";


        for (EligibilityRule rule : rules) {

            if (!rule.isEligible(s)) {

                status = "NOT_ELIGIBLE";

                reasons.add(rule.reason());

                break;
            }
        }

        return new EligibilityEngineResult(status, reasons);
    }
}
public class Demo03 {

    public static void main(String[] args) {

        System.out.println("=== Placement Eligibility ===");

        StudentProfile s =
                new StudentProfile(
                        "23BCS1001",
                        "Ayaan",
                        8.10,
                        72,
                        18,
                        LegacyFlags.NONE
                );

        EligibilityEngine engine =
                new EligibilityEngine(new FakeEligibilityStore());

        engine.runAndPrint(s);
    }
}
