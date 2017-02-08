package com.macrosoft.bms.controller;

import com.macrosoft.bms.data.dao.Impl.UserDaoImpl;
import com.macrosoft.bms.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Properties;

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
