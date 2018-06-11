package net.zembrowski.julian.domain;

public enum Status {
    NIESPRAWDZONE(1),UMIEM(2),NIEUMIEM(3);
    public int id;
    Status(int i)
    {
        id=i;
    }
}
