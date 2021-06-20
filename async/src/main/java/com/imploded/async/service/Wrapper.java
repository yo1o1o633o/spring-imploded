package com.imploded.async.service;

public class Wrapper {
    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    private String params;

    private Listener listener;

    private Worker worker;

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }
}
