package tema4;

import java.sql.Date;

public class Order {
    private int id;
    private Date orderDate;
    private Date shippingDate;
    private String status;
    private String comments;
    private int customerId;

    public Order(int id, Date orderDate, Date shippingDate, String status, String comments, int customerId) {
        this.id = id;
        this.orderDate = orderDate;
        this.shippingDate = shippingDate;
        this.status = status;
        this.comments = comments;
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", shippingDate=" + shippingDate +
                ", status='" + status + '\'' +
                ", comments='" + comments + '\'' +
                ", customerId=" + customerId +
                '}';
    }
}
