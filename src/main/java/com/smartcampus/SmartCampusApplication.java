package com.smartcampus;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

// This sets the base path for ALL your API endpoints
// So every endpoint starts with /api/v1
@ApplicationPath("/api/v1")
public class SmartCampusApplication extends Application {
    // JAX-RS automatically finds all @Path and @Provider classes
    // No code needed here
}