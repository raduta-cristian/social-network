package org.example.social_network.utils.events;


import org.example.social_network.domain.Cerere;

public class CerereEntityChangeEvent implements Event {
    private ChangeEventType type;
    private Cerere data, oldData;

    public CerereEntityChangeEvent(ChangeEventType type, Cerere data) {
        this.type = type;
        this.data = data;
    }
    public CerereEntityChangeEvent(ChangeEventType type, Cerere data, Cerere oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Cerere getData() {
        return data;
    }

    public Cerere getOldData() {
        return oldData;
    }
}