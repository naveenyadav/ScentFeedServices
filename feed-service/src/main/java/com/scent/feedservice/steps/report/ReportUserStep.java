package com.scent.feedservice.steps.report;

import com.scent.feedservice.Util.DateUtil;
import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.data.ResponseData;
import com.scent.feedservice.data.profile.Report;
import com.scent.feedservice.data.profile.Reportee;
import com.scent.feedservice.repositories.ReportRepository;
import com.scent.feedservice.steps.IAction;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Map;

import static com.scent.feedservice.Util.Constants.*;

@Component
public class ReportUserStep implements IAction {

    private ReportRepository reportRepository;
    public ReportUserStep(ReportRepository reportRepository){
        this.reportRepository = reportRepository;
    }
    public ResponseData perFormAction(EventData eventData) {
        final RequestData requestData = eventData.getRequestData();
        Map<String, String> paramMap = getRequestParamsCopy(requestData.getDataMap());
        return eventData.getResponseData();
    }

    private void getReport(Map<String, String> paramMap){
        final String reporterUserId = paramMap.get(REPORTER_USER_ID);
        String reporteeUserId = paramMap.get(USER_ID);
        final Date date = DateUtil.getFormatDate(paramMap.get(DATE), POST_TIME_PATTERN, POST_TIME_PATTERN);
        Mono<Report> reportMono = reportRepository.getReportByUserId(reporteeUserId).flatMap(report -> {
            Reportee reportee = new Reportee();
            reportee.setUserId(reporterUserId);
            reportee.setReportedOn(date);
            report.addReport(reportee);
            return Mono.just(report);
        });

    }
}
