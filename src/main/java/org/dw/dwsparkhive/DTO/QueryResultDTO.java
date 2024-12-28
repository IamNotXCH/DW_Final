package org.dw.dwsparkhive.DTO;

public class QueryResultDTO<T> {
    private T data;
    private long queryTimeMillis;

    public QueryResultDTO() {
    }

    public QueryResultDTO(T data, long queryTimeMillis) {
        this.data = data;
        this.queryTimeMillis = queryTimeMillis;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getQueryTimeMillis() {
        return queryTimeMillis;
    }

    public void setQueryTimeMillis(long queryTimeMillis) {
        this.queryTimeMillis = queryTimeMillis;
    }
}
