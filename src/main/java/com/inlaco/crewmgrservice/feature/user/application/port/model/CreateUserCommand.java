package com.inlaco.crewmgrservice.feature.user.application.port.model;

public record CreateUserCommand(String username, String password, String name) {}
