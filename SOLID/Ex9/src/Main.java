public class Main {
    //Object creation was moved to Main, effectively inverting the dependency direction.
    public static void main(String[] args) {
        System.out.println("=== Evaluation Pipeline ===");
        Submission sub = new Submission("23BCS1007", "public class A{}", "A.java");
        PlagiarismService pc = new PlagiarismChecker();
        GradingService grader = new CodeGrader(new Rubric());
        ReportService writer = new ReportWriter();
       //stretch goal=second grader
       GradingService strictGrader = new StrictCodeGrader();
        EvaluationPipeline pipeline = new EvaluationPipeline(pc, grader, writer);

        pipeline.evaluate(sub);
    }
}
