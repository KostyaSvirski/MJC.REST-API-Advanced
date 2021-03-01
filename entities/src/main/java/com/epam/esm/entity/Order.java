package com.epam.esm.entity;

import java.time.Instant;

public class Order {

    private long id;
    private long idCertificate;
    private long idUser;
    private Instant purchaseTime;
    private long cost;
    private boolean isClosed;

    public Order(long id) {
        this.id = id;
    }

    public Order() {
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdCertificate() {
        return idCertificate;
    }

    public void setIdCertificate(long idCertificate) {
        this.idCertificate = idCertificate;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public Instant getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(Instant purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public static class OrderBuilder {

        private Order newOrder;

        public OrderBuilder() {
            this.newOrder = new Order();
        }

        public OrderBuilder buildId(long id) {
            newOrder.id = id;
            return this;
        }

        public OrderBuilder buildCertificates(long idCertificate) {
            newOrder.idCertificate = idCertificate;
            return this;
        }

        public OrderBuilder buildUser(long idUser) {
            newOrder.idUser = idUser;
            return this;
        }

        public OrderBuilder buildPurchaseTime(Instant purchaseTime) {
            newOrder.purchaseTime = purchaseTime;
            return this;
        }

        public OrderBuilder buildIsClosed(boolean flag) {
            newOrder.isClosed = flag;
            return this;
        }

        public OrderBuilder buildCost(long cost) {
            newOrder.cost = cost;
            return this;
        }

        public Order finishBuilding() {
            return newOrder;
        }
    }

}
