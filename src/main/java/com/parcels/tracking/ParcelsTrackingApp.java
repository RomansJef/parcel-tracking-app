package com.parcels.tracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Parcels Tracking App.
 */
@SpringBootApplication
@EnableSwagger2
public class ParcelsTrackingApp {
    public static void main(String[] args) {
        SpringApplication.run(ParcelsTrackingApp.class, args);
    }
}
