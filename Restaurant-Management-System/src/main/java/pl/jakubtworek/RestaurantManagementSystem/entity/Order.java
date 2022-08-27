package pl.jakubtworek.RestaurantManagementSystem.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Orders")
@Component
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="price")
    private double price;

    @Column(name="date")
    private String date;

    @Column(name="hour_order")
    private String hourOrder;

    @Column(name="hour_away")
    private String hourAway;

    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="type_of_order_id")
    @JsonBackReference(value="type_of_order_id")
    private TypeOfOrder typeOfOrder;

    @ManyToMany(fetch=FetchType.LAZY,
                cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name="Order_Menu_Item",
            joinColumns = @JoinColumn(name="order_id"),
            inverseJoinColumns = @JoinColumn(name="menu_item_id")
    )
    private List<MenuItem> menuItems;

    @ManyToMany(fetch=FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name="Order_Employee",
            joinColumns = @JoinColumn(name="order_id"),
            inverseJoinColumns = @JoinColumn(name="employee_id")
    )
    private List<Employee> employees;

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHourOrder() {
        return hourOrder;
    }

    public void setHourOrder(String hourOrder) {
        this.hourOrder = hourOrder;
    }

    public String getHourAway() {
        return hourAway;
    }

    public void setHourAway(String hourAway) {
        this.hourAway = hourAway;
    }

    public TypeOfOrder getTypeOfOrder() {
        return typeOfOrder;
    }

    public void setTypeOfOrder(TypeOfOrder typeOfOrder) {
        this.typeOfOrder = typeOfOrder;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public void add(MenuItem tempMenuItem) {
        if(menuItems == null) {
            menuItems = new ArrayList<>();
        }

        menuItems.add(tempMenuItem);
        tempMenuItem.add(this);
    }

    public void add(Employee tempEmployee) {
        if(employees == null) {
            employees = new ArrayList<>();
        }

        employees.add(tempEmployee);
        tempEmployee.add(this);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", price=" + price +
                ", date='" + date + '\'' +
                ", hourOrder='" + hourOrder + '\'' +
                ", hourAway='" + hourAway + '\'' +
                ", typeOfOrder=" + typeOfOrder +
                '}';
    }
}
