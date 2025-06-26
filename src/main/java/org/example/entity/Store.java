package org.example.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "store")
public class Store {

    @Id
    @Column(name = "store_id")
    private Integer storeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "manager_staff_id", nullable = false)
    private Staff managerStaff;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private Set<Staff> staff = new HashSet<>();

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private Set<Customer> customers = new HashSet<>();

    public Store() {}

    public Integer getStoreId() { return storeId; }
    public void setStoreId(Integer storeId) { this.storeId = storeId; }

    public Staff getManagerStaff() { return managerStaff; }
    public void setManagerStaff(Staff managerStaff) { this.managerStaff = managerStaff; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public Set<Staff> getStaff() { return staff; }
    public void setStaff(Set<Staff> staff) { this.staff = staff; }

    public Set<Customer> getCustomers() { return customers; }
    public void setCustomers(Set<Customer> customers) { this.customers = customers; }
}
