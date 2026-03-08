package com.example.reports;

/**
 * TODO (student):
 * Implement Proxy responsibilities here:
 * - access check
 * - lazy loading
 * - caching of RealReport within the same proxy
 */
public class ReportProxy implements Report {
    //act as a proxy on RealReport for lazy loading and access control and caching
    private final String reportId;
    private final String title;
    private final String classification;
    private final AccessControl accessControl = new AccessControl();
    private RealReport realReport;// cashed instance

    public ReportProxy(String reportId, String title, String classification) {
        this.reportId = reportId;
        this.title = title;
        this.classification = classification;
    }

    @Override
    public void display(User user) {
       //access control
        if(!accessControl.canAccess(user,classification)){
            System.out.println("Access denied for user: " + user.getName() + " to report: " + reportId);
            return;
        }
        //lazy loading
        if(realReport == null){
            realReport = new RealReport(reportId, title, classification);
        }
        //caching
        realReport.display(user);
    }
}
