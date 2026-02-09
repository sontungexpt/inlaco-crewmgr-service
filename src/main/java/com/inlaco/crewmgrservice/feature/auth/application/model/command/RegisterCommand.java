package com.inlaco.crewmgrservice.feature.auth.application.model.command;

public record RegisterCommand(
    String username, String password, String confirmPassword, String name) {}
