package utez.edu.mx.unidad3.moduls.warehouse;

import jakarta.persistence.*;
import utez.edu.mx.unidad3.moduls.cede.Cede;
import utez.edu.mx.unidad3.moduls.clients.Client;

@Entity
@Table(name = "warehouse")
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "clave", nullable = false)
    private String clave;

    @Column(name = "sell_price", nullable = false)
    private Double sellPrice;

    @Column(name = "rent_price", nullable = false)
    private Double rentPrice;

    @ManyToOne
    @JoinColumn(name = "id_cede")
    private Cede cede;

    @ManyToOne
    @JoinColumn(name = "id_client")
    private Client client;

    public Warehouse() {}

    public Warehouse(Long id, String clave, Double sellPrice, Double rentPrice, Cede cede, Client client) {
        this.id = id;
        this.clave = clave;
        this.sellPrice = sellPrice;
        this.rentPrice = rentPrice;
        this.cede = cede;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Double getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(Double rentPrice) {
        this.rentPrice = rentPrice;
    }

    public Cede getCede() {
        return cede;
    }

    public void setCede(Cede cede) {
        this.cede = cede;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
