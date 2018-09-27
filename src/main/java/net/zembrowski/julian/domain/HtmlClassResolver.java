package net.zembrowski.julian.domain;

public interface HtmlClassResolver {
    boolean IsNumberInRange(int number);

    String resolveClass(int repetesNumber);

    String getName();
}
