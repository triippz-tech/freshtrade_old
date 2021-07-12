package com.triippztech.freshtrade.service.dto.reservation;

import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

public class CancelReservationDTO {

    private UUID id;

    @Lob
    @NotNull
    private String cancellationNote;

    public CancelReservationDTO() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCancellationNote() {
        return cancellationNote;
    }

    public void setCancellationNote(String cancellationNote) {
        this.cancellationNote = cancellationNote;
    }
}
