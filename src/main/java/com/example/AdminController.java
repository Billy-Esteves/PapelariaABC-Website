package com.example;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/status")
    public String status() {
        return "✅ Website is running.";
    }

    @PostMapping("/shutdown")
    public void shutdown() {
        System.exit(0);  // Alternatively, use ApplicationContext.close()
    }
}
