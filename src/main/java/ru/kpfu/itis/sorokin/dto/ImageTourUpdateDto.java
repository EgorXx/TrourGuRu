package ru.kpfu.itis.sorokin.dto;

import java.io.InputStream;

public record ImageTourUpdateDto(InputStream inputStream, Boolean isMain) {}
