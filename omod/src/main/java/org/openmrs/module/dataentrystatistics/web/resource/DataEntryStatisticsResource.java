package org.openmrs.module.dataentrystatistics.web.resource;


import org.openmrs.Person;
import org.openmrs.api.APIAuthenticationException;
import org.openmrs.api.context.Context;
import org.openmrs.module.dataentrystatistics.DataEntryStatistic;
import org.openmrs.module.dataentrystatistics.DataEntryStatisticService;
import org.openmrs.module.dataentrystatistics.web.resource.mapper.DataEntryStatisticMapper;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/dataentrystatistics")
public class DataEntryStatisticsResource {
    @ExceptionHandler(APIAuthenticationException.class)
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Object get(HttpServletRequest request, HttpServletResponse response) {
        String fromDateString = request.getParameter("fromDate");
        String toDateString = request.getParameter("toDate");
        String encUserColumn = request.getParameter("encUserColumn");
        String orderUserColumn = request.getParameter("orderUserColumn");
        String groupBy = request.getParameter("groupBy");


        if (fromDateString == null || toDateString == null) {
            return new ResponseEntity<Object>("Bad Request", HttpStatus.BAD_REQUEST);
        } else {
            Date fromDate = parseYmd(fromDateString);
            Date toDate = parseYmd(toDateString);

            DataEntryStatisticService svc =  Context.getService(DataEntryStatisticService.class);

            List<DataEntryStatistic> stats = svc.getDataEntryStatistics(fromDate,
                    toDate, encUserColumn, orderUserColumn, groupBy);

//        this is a hack to avoid recursive error on jackson conversion
            List<DataEntryStatisticMapper> dataEntryStatisticMapperList = convertDataEntryStaticsForJsonMapping(stats);

            return new ResponseEntity<List<DataEntryStatisticMapper>>(dataEntryStatisticMapperList, HttpStatus.OK);

        }
    }

    public static Date parseDate(String s, String format) {
        DateFormat df = new SimpleDateFormat(format);

        try {
            return df.parse(s);
        } catch (Exception var4) {
            throw new RuntimeException("Cannot parse " + s + " into a date using format " + format);
        }
    }

    public static Date parseYmd(String date) {
        return parseDate(date, "yyyy-MM-dd");
    }

    public List<DataEntryStatisticMapper> convertDataEntryStaticsForJsonMapping(List<DataEntryStatistic> dataEntryStatistics) {

        List<DataEntryStatisticMapper> dataList = new ArrayList<DataEntryStatisticMapper>();
        for (DataEntryStatistic dataEntryStatistic : dataEntryStatistics) {
            Person person = dataEntryStatistic.getUser();
            String fullName ="";
            String person_uuid ="";
            if(person.getPersonName()!=null){
               fullName = person.getPersonName().getFullName();
               person_uuid = person.getUuid();
            }
            String entryType = dataEntryStatistic.getEntryType();
            int numberOfEntries = dataEntryStatistic.getNumberOfEntries();
            int numberOfObs = dataEntryStatistic.getNumberOfObs();
            Object groupedBy = dataEntryStatistic.getGroupBy();
            String groupBy = null;
            if (groupedBy != null){
                groupBy = groupedBy.toString();
            }

            DataEntryStatisticMapper mapper = new DataEntryStatisticMapper(fullName,person_uuid, entryType, numberOfEntries, numberOfObs, groupBy);
            dataList.add(mapper);

        }

        return dataList;
    }

}
