package com.example.tickets;

import java.util.ArrayList;
import java.util.List;

/**
 * Service layer that creates tickets.
 *
 * CURRENT STATE (BROKEN ON PURPOSE):
 * - creates partially valid objects
 * - mutates after creation (bad for auditability)
 * - validation is scattered & incomplete
 *
 * TODO (student):
 * - After introducing immutable IncidentTicket + Builder, refactor this to stop mutating.
 */
public class TicketService {

    public IncidentTicket createTicket(String id, String reporterEmail, String title) {
        // scattered validation (incomplete on purpose)
        // if (id == null || id.trim().isEmpty()) throw new IllegalArgumentException("id required");
        // if (reporterEmail == null || !reporterEmail.contains("@")) throw new IllegalArgumentException("email invalid");
        // if (title == null || title.trim().isEmpty()) throw new IllegalArgumentException("title required");

        // IncidentTicket t = new IncidentTicket(id, reporterEmail, title);

        // // BAD: mutating after creation
        // t.setPriority("MEDIUM");
        // t.setSource("CLI");
        // t.setCustomerVisible(false);

        // List<String> tags = new ArrayList<>();
        // tags.add("NEW");
        // t.setTags(tags);

        // return t;
        return IncidentTicket.builder()
                .id(id)
                .reporterEmail(reporterEmail)
                .title(title)
                .priority("MEDIUM")
                .source("CLI")
                .customerVisible(false)
                .addTag("NEW")
                .build();
    }

    public IncidentTicket escalateToCritical(IncidentTicket t) {
        // BAD: mutating ticket after it has been "created"
        // t.setPriority("CRITICAL");
        // t.getTags().add("ESCALATED"); // list leak
        return t.toBuilder()
                .priority("CRITICAL")
                .addTag("ESCALATED")
                .build();
    }

    public IncidentTicket assign(IncidentTicket t, String assigneeEmail) {
        // scattered validation
        if (assigneeEmail != null && !assigneeEmail.contains("@")) {
            throw new IllegalArgumentException("assigneeEmail invalid");
        }
        // t.setAssigneeEmail(assigneeEmail);
        return t.toBuilder()
                .assigneeEmail(assigneeEmail)
                .build();
    }
}
