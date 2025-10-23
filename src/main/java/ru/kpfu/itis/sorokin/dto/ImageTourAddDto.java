package ru.kpfu.itis.sorokin.dto;

import java.io.InputStream;

public record ImageTourAddDto(InputStream inputStream, Boolean isMain) {}
