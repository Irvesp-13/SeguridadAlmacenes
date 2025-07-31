package utez.edu.mx.unidad3.moduls.events;

public enum EventStatus {
    PROXIMAMENTE("Próximamente"),
    EN_EJECUCION("En ejecución"),
    FINALIZADO("Finalizado");

    private final String displayName;

    EventStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
