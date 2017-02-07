package com.icontrolesi.envity.controller;

import com.icontrolesi.envity.data.dao.Impl.UserDaoImpl;
import com.icontrolesi.envity.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

@Controller
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserDaoImpl userDaoImpl;
    private ResourceLoader resourceLoader;

    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public String loginView(Model model) {
        System.out.println(":: In Login Controller ::");
        return "common/login";
    }

    public Properties getPropertiesForMail() {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", Utils.getApplicationPropertyValue("smtp.host.name"));
        props.put("mail.smtp.socketFactory.port", Utils.getApplicationPropertyValue("smtp.socketfactory.port"));
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", Utils.getApplicationPropertyValue("smtp.socketfactory.port"));
        props.put("mail.smtp.starttls.enable", "true");
        return props;
    }
}
