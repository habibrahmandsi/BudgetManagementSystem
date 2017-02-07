package com.icontrolesi.envity.controller;

import com.icontrolesi.envity.util.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {
	@RequestMapping(method= RequestMethod.GET, value="/")
	public String index() {
		return "redirect:/admin/landing";
	}
}
