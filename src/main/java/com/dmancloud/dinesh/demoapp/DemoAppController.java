package com.dmancloud.dinesh.demoapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoAppController {

  @GetMapping("/")
  public String hello(Model model) {
    String containerId = System.getenv("HOSTNAME"); // or use another environment variable if needed
    model.addAttribute("message", "Hello World! This is a sample application to demonstrate an end-2-end DevOps Pipeline");
    model.addAttribute("containerId", containerId); // Add container ID to the model
    return "index";
  }

  @GetMapping("/error")
  public String handleError() {
    return "error";
  }
}
