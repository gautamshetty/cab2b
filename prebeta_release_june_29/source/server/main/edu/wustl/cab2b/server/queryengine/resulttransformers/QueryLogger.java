package edu.wustl.cab2b.server.queryengine.resulttransformers;

import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.dcql.DCQLQuery;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.namespace.QName;

public class QueryLogger {
    private static final String LOG_BASE_DIR = System.getProperty("user.home") + "/cab2bQueries";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy hh_mm_ss a");

    private static final String LOG_FILE_NAME_PREFIX = "query_";

    private final String logFolderName;

    private int i = 1;

    static {
        new File(LOG_BASE_DIR).mkdir();
    }

    public QueryLogger() {
        logFolderName = LOG_BASE_DIR + "/" + getCurrentTime();
        new File(logFolderName).mkdir();
    }

    private String getCurrentTime() {
        Date currDate = new Date(System.currentTimeMillis());
        return dateFormat.format(currDate);
    }

    private boolean isLogEnabled() {
        return Logger.out.isInfoEnabled();
    }

    public void log(DCQLQuery dcqlQuery) {
        if (!isLogEnabled()) {
            return;
        }
        try {
            Utils.serializeDocument(getLogFilePath(), dcqlQuery, new QName(
                    "http://caGrid.caBIG/1.0/gov.nih.nci.cagrid.dcql", "DCQLQuery"));
        } catch (Exception e) {
            Logger.out.info("Could not log dcql.");
        }

    }

    public void log(CQLQuery cqlQuery) {
        if (!isLogEnabled()) {
            return;
        }
        try {
            Utils.serializeDocument(getLogFilePath(), cqlQuery, new QName(
                    "http://caGrid.caBIG/1.0/gov.nih.nci.cagrid.cqlquery", "CQLQuery"));
        } catch (Exception e) {
            Logger.out.info("Could not log cql.");
        }

    }

    private synchronized String getLogFilePath() {

        return logFolderName + "/" + LOG_FILE_NAME_PREFIX + i++ + ".xml";
    }
}
