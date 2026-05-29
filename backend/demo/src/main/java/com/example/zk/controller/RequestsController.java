package com.example.zk.controller;

import com.example.zk.model.Request;
import com.example.zk.service.RequestService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestsController extends SelectorComposer<Component> {

    // ── Wired components ─────────────────────────────────────────────
    @Wire("#requestList")
    private Listbox requestList;

    @Wire("#metricsTable")
    private Listbox metricsTable;

    @Wire("#titleBox")
    private Textbox titleBox;

    @Wire("#descriptionBox")
    private Textbox descriptionBox;

    @Wire("#totalRequestsLabel")
    private Label totalRequestsLabel;

    @Wire("#successRequestsLabel")
    private Label successRequestsLabel;

    @Wire("#noResponseRequestsLabel")
    private Label noResponseRequestsLabel;

    @Wire("#greetingRequestsLabel")
    private Label greetingRequestsLabel;

    @Wire("#statusBreakdownLabel")
    private Label statusBreakdownLabel;

    // ── Service ──────────────────────────────────────────────────────
    private RequestService requestService;

    // ── Lifecycle ────────────────────────────────────────────────────
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        requestService = (RequestService) SpringUtil.getBean("requestService");

        refreshAll();
    }

    // ── Refresh all ──────────────────────────────────────────────────
    private void refreshAll() {
        Map<String, Long> counts = requestService.countByStatusMap();
        long total = counts.values().stream().mapToLong(Long::longValue).sum();

        refreshMetricsTable(counts, total);
        refreshStatCards(counts, total);
        refreshList();
    }

    // ── Metrics table ────────────────────────────────────────────────
    private void refreshMetricsTable(Map<String, Long> counts, long total) {

        List<Object[]> titleCounts = requestService.countByTitle();

        // most and least requested titles
        String mostTitle  = "—";
        String leastTitle = "—";
        long   mostCount  = 0;
        long   leastCount = 0;

        if (!titleCounts.isEmpty()) {
            Object[] mostRow  = titleCounts.get(0);
            Object[] leastRow = titleCounts.get(titleCounts.size() - 1);

            mostTitle  = mostRow[0]  != null ? (String) mostRow[0]  : "—";
            leastTitle = leastRow[0] != null ? (String) leastRow[0] : "—";
            mostCount  = (Long) mostRow[1];
            leastCount = (Long) leastRow[1];
        }

        List<String[]> rows = new ArrayList<>();

        // ── Status rows ──────────────────────────────────────────────
        rows.add(new String[]{ "Total",      String.valueOf(total),                            "100%",                                    "status" });
        rows.add(new String[]{ "Successful", String.valueOf(sumByStatus(counts, "SUCCESS")),   pct(sumByStatus(counts, "SUCCESS"),  total), "status" });
        rows.add(new String[]{ "No Result",  String.valueOf(sumByStatus(counts, "NO_RESULT")), pct(sumByStatus(counts, "NO_RESULT"), total), "status" });
        rows.add(new String[]{ "Greetings",  String.valueOf(sumByStatus(counts, "GREETING")),  pct(sumByStatus(counts, "GREETING"),  total), "status" });
        rows.add(new String[]{ "Unknown",    String.valueOf(sumByStatus(counts, "UNKNOWN")),   pct(sumByStatus(counts, "UNKNOWN"),   total), "status" });

        // ── Separator ────────────────────────────────────────────────
        rows.add(new String[]{ "— Title Frequency —", "", "", "separator" });

        // ── Title rows ───────────────────────────────────────────────
        rows.add(new String[]{ "Most Requested:  " + mostTitle,  String.valueOf(mostCount),  pct(mostCount,  total), "most"  });
        rows.add(new String[]{ "Least Requested: " + leastTitle, String.valueOf(leastCount), pct(leastCount, total), "least" });

        metricsTable.setModel(new ListModelList<>(rows));
        metricsTable.setItemRenderer((Listitem item, String[] row, int index) -> {

            String type = row[3];

            switch (type) {

                case "separator" -> {
                    Listcell sep = new Listcell(row[0]);
                    sep.setSpan(3);
                    sep.setSclass("metric-separator-cell");
                    item.appendChild(sep);
                    item.setSclass("metric-separator-row");
                }

                default -> {
                    Listcell labelCell = new Listcell(row[0]);
                    Listcell countCell = new Listcell(row[1]);
                    Listcell pctCell   = new Listcell(row[2]);

                    labelCell.setSclass("metric-label-cell");
                    countCell.setSclass("metric-count-cell");
                    pctCell.setSclass("metric-pct-cell");

                    if (type.equals("status") && index == 0) item.setSclass("metric-total-row");
                    if (type.equals("most"))  item.setSclass("metric-most-row");
                    if (type.equals("least")) item.setSclass("metric-least-row");

                    item.appendChild(labelCell);
                    item.appendChild(countCell);
                    item.appendChild(pctCell);
                }
            }
        });
    }

    // ── Stat cards ───────────────────────────────────────────────────
    private void refreshStatCards(Map<String, Long> counts, long total) {
        long success  = sumByStatus(counts, "SUCCESS");
        long noResult = sumByStatus(counts, "NO_RESULT");
        long greeting = sumByStatus(counts, "GREETING");

        totalRequestsLabel.setValue(String.valueOf(total));
        successRequestsLabel.setValue(String.valueOf(success));
        noResponseRequestsLabel.setValue(String.valueOf(noResult));
        greetingRequestsLabel.setValue(String.valueOf(greeting));

        String breakdown = counts.entrySet().stream()
                .map(e -> normalize(e.getKey()) + ": " + e.getValue())
                .collect(Collectors.joining("  |  "));

        statusBreakdownLabel.setValue(breakdown.isEmpty() ? "No data" : breakdown);
    }

    // ── Requests table ───────────────────────────────────────────────
    private void refreshList() {
        List<Request> requests = requestService.findAll();

        requestList.setModel(new ListModelList<>(requests));
        requestList.setItemRenderer((Listitem item, Request r, int index) -> {

            item.appendChild(new Listcell(String.valueOf(r.getId())));
            item.appendChild(new Listcell(r.getTitle()));
            item.appendChild(new Listcell(r.getDescription()));
            item.appendChild(new Listcell(r.getStatus() != null ? r.getStatus() : "—"));

            Button deleteBtn = new Button("Delete");
            deleteBtn.setSclass("btn-delete");
            deleteBtn.addEventListener("onClick", event -> {
                Messagebox.show(
                    "Delete \"" + r.getTitle() + "\"?",
                    "Confirm Delete",
                    Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION,
                    confirmEvent -> {
                        if (confirmEvent.getName().equals(Messagebox.ON_OK)) {
                            requestService.deleteById(r.getId());
                            refreshAll();
                        }
                    }
                );
            });

            Listcell actionCell = new Listcell();
            actionCell.appendChild(deleteBtn);
            item.appendChild(actionCell);
        });
    }

    // ── Create ───────────────────────────────────────────────────────
    @Listen("onClick = #createRequestBtn")
    public void onCreate() {
        String title       = titleBox.getValue();
        String description = descriptionBox.getValue();

        if (title == null || title.isBlank()) return;

        Request r = new Request();
        r.setTitle(title.trim());
        r.setDescription(description == null ? "" : description.trim());
        r.setStatus("SUCCESS");

        requestService.save(r);

        titleBox.setValue("");
        descriptionBox.setValue("");

        refreshAll();
    }

    // ── Helpers ──────────────────────────────────────────────────────
    private long sumByStatus(Map<String, Long> counts, String targetStatus) {
        return counts.entrySet().stream()
                .filter(e -> normalize(e.getKey()).equals(targetStatus))
                .mapToLong(Map.Entry::getValue)
                .sum();
    }

    private String normalize(String status) {
        return status == null ? "UNKNOWN" : status.trim().toUpperCase().replace(" ", "_");
    }

    private String pct(long value, long total) {
        if (total == 0) return "0%";
        return String.format("%.1f%%", (value * 100.0) / total);
    }
}