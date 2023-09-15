package ru.sidey383.ozon.api;

import java.util.List;

public record AnswerList<T>(List<T> list, long total) {}
