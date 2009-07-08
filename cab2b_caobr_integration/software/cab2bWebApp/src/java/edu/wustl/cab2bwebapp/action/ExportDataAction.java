package edu.wustl.cab2bwebapp.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.cab2bwebapp.bizlogic.executequery.ExecuteQueryBizLogic;
import edu.wustl.cab2bwebapp.constants.Constants;

public class ExportDataAction extends Action {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(ExportDataAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        String filePath = getServlet().getServletContext().getRealPath("/");
        HttpSession session = request.getSession();
        ExecuteQueryBizLogic executeQueryBizLogic =
            (ExecuteQueryBizLogic) session.getAttribute(Constants.EXECUTE_QUERY_BIZ_LOGIC_OBJECT);
        Long queryId = (Long) session.getAttribute(Constants.QUERY_ID);
        if(queryId!=null)
        {
            String filename = executeQueryBizLogic.exportToCSV(queryId,filePath);
            sendFileToClient(response, filePath + filename , "ExportedData.csv", "application/download");
        }
        return null;
    }

    public static void sendFileToClient(HttpServletResponse response, String filePath, String fileName,
                                        String contentType) {
        if (filePath != null && (filePath.length() != 0)) {
            File file = new File(filePath);
            if (file.exists()) {
                response.setContentType(contentType);
                response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\";");
                response.setContentLength((int) file.length());
                writeToStream(response, file);
            } else {
                logger.error("Sorry Cannot Download as fileName is null");
            }
        }
    }

    private static void writeToStream(HttpServletResponse response, File file) {
        BufferedInputStream bis = null;
        try {
            OutputStream opstream = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(file));

            byte[] buf = new byte[Constants.FOUR_KILO_BYTES];
            int count = bis.read(buf);
            while (count > -1) {
                opstream.write(buf, 0, count);
                count = bis.read(buf);
            }
            opstream.flush();
            bis.close();
            boolean isDeleted = file.delete();
            if (!isDeleted) {
                logger.info("Not able to delete file " + file.getName());
            }
        } catch (FileNotFoundException ex) {
            logger.error("Exception in method sendFileToClient:" + ex.getMessage(), ex);
        } catch (IOException ex) {
            logger.error("Exception in method sendFileToClient:" + ex.getMessage(), ex);
        }
    }
}