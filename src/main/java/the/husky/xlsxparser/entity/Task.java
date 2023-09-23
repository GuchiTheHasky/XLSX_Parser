package the.husky.xlsxparser.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "tasks")
public class Task {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "execution_date")
    private LocalDate executionDate;

    @Column(name = "map_point")
    private Integer mapPoint;

    @Column(name = "car_id")
    private Integer carId;

    @Column(name = "delivery_start")
    private LocalTime deliveryStart;

    @Column(name = "delivery_finish")
    private LocalTime deliveryFinish;

    @Column(name = "frozen")
    private Boolean frozen;

    @Column(name = "delivery_weight")
    private Double deliveryWeight;

    @Column(name = "pickup_weight")
    private Double pickupWeight;

    @Column(name = "dropped")
    private Boolean dropped;

    @Column(name = "route_point_id")
    private Long routePointId;

    @Column(name = "address_details")
    private String addressDetails;

    @Column(name = "requested_car_id")
    private Long requestedCarId;

    @PrePersist
    public void setDefaultValues() {
        this.executionDate = LocalDate.now();
        this.mapPoint = null;
        this.carId = null;
        this.deliveryStart = LocalTime.of(9, 0);
        this.deliveryFinish = LocalTime.of(18, 0);
        this.frozen = false;
        this.pickupWeight = 0.0;
        this.dropped = false;
        this.routePointId = null;
        this.requestedCarId = null;
    }
}
