package com.epam.esm.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class OrderDTO extends RepresentationModel<OrderDTO> {

    private long id;
    private long idUser;
    private long idCertificate;
    private String timeOfRelease;
    private boolean isClosed;

    public OrderDTO(long id) {
        this.id = id;
    }

    public OrderDTO(long id, long idUser, long idCertificate) {
        this.id = id;
        this.idUser = idUser;
        this.idCertificate = idCertificate;
    }

    public OrderDTO() {
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public long getIdCertificate() {
        return idCertificate;
    }

    public void setIdCertificate(long idCertificate) {
        this.idCertificate = idCertificate;
    }

    public String getTimeOfRelease() {
        return timeOfRelease;
    }

    public void setTimeOfRelease(String timeOfRelease) {
        this.timeOfRelease = timeOfRelease;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDTO orderDTO = (OrderDTO) o;
        return id == orderDTO.id && idUser == orderDTO.idUser
                && idCertificate == orderDTO.idCertificate && isClosed == orderDTO.isClosed
                && Objects.equals(timeOfRelease, orderDTO.timeOfRelease);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idUser, idCertificate, timeOfRelease, isClosed);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderDTO{");
        sb.append("id=").append(id);
        sb.append(", idUser=").append(idUser);
        sb.append(", idCertificate=").append(idCertificate);
        sb.append(", timeOfRelease='").append(timeOfRelease).append('\'');
        sb.append(", isClosed=").append(isClosed);
        sb.append('}');
        return sb.toString();
    }
}
