package com.icontrolesi.envity.controller;

import com.icontrolesi.envity.data.dao.Impl.AdminDAOImpl;
import com.icontrolesi.envity.data.model.*;
import com.icontrolesi.envity.util.Constants;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

@Controller
public class AdminController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdminDAOImpl adminDaoImpl;


    @InitBinder
    protected void initBinder(ServletRequestDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/landing")
    public ModelAndView landingPage(HttpServletRequest request, @RequestParam(value = "action", required = false) String action) {
        System.out.println("----------------- tar/landing ---------------");
        ModelAndView model = new ModelAndView("tar/landing");
        return model;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/generalConfiguration")
    public String generalConfigurationView(Model model) {
        logger.info("********* generalConfiguration Controller ************");

        return "admin/generalConfiguration";
    }

    @RequestMapping(value = "/admin/generalConfiguration", method = RequestMethod.POST)
    public String generalConfigPost(HttpServletRequest request) {
        logger.info("********* generalConfiguration POST Controller ************");
        return "redirect:/admin/generalConfiguration";
    }

    @RequestMapping(value = "/admin/getRelativityInstances", method = RequestMethod.GET)
    public
    @ResponseBody
    DataModelBean getRelativityInstances(HttpServletRequest request) throws Exception {
        logger.info(":: Get Relativity Instances List Ajax Controller ::");
        DataModelBean dataModelBean = new DataModelBean();
         /* this params is for dataTables */
        String[] tableColumns = "name,url,username,status,version".split(",");
        int start = request.getParameter(Constants.IDISPLAY_START) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_START)) : 0;
        int length = request.getParameter(Constants.IDISPLAY_LENGTH) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_LENGTH)) : 5;
        int sEcho = request.getParameter(Constants.sEcho) != null ? Integer.parseInt(request.getParameter(Constants.sEcho)) : 0;
        int iSortColIndex = request.getParameter(Constants.iSortCOL) != null ? Integer.parseInt(request.getParameter(Constants.iSortCOL)) : 0;
        String searchKey = request.getParameter(Constants.sSearch) != null ? request.getParameter(Constants.sSearch) : "";
        String sortType = request.getParameter(Constants.sortType) != null ? request.getParameter(Constants.sortType) : "asc";
        String sortColName = "";

        sortColName = tableColumns[iSortColIndex];
        Map<String, Object> userDataMap;

        try {
            /*int totalRecords = Math.toIntExact(envityDaoImpl.getRelativityInstancesDataSize());
            logger.info("totalRecords:" + totalRecords + " length:" + length);
            if (length < 0) {
                userDataMap = envityDaoImpl.getRelativityInstances(start, totalRecords + 1, sortColName, sortType, searchKey);
            } else {
                userDataMap = envityDaoImpl.getRelativityInstances(start, length, sortColName, sortType, searchKey);
            }
                *//*
                * DataModelBean is a bean of Data table to
                * handle data Table search, paginatin operation very simply
                *//*
            logger.debug("dataModelBean::" + dataModelBean);
            dataModelBean.setAaData((List) userDataMap.get("data"));
            if (!StringUtils.isEmpty(searchKey)) {
                dataModelBean.setiTotalDisplayRecords((Integer) userDataMap.get("total"));
            } else {
                dataModelBean.setiTotalDisplayRecords(totalRecords);
            }
            dataModelBean.setiTotalRecords(totalRecords);
            dataModelBean.setsEcho(sEcho);
            dataModelBean.setiDisplayStart(start);
            dataModelBean.setiDisplayLength(totalRecords);*/

        } catch (Exception ex) {
            logger.error(":: ERROR:: Failed to load relativity instance details data:: " + ex);
        }

        return dataModelBean;
    }
}
