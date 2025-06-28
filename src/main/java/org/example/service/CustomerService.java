package org.example.service;

import org.example.entity.Address;
import org.example.entity.Customer;
import org.example.entity.Rental;
import org.example.entity.Store;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;

public class CustomerService {
    public Customer createCustomer(String firstName,
                                   String lastName,
                                   String email,
                                   Address address,
                                   Store store) {
        Transaction tx = null;
        Customer customer = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            session.persist(address);

            customer = new Customer();
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setEmail(email);
            customer.setActive(true);
            customer.setAddress(address);
            customer.setStore(store);

            session.persist(customer);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
        return customer;
    }

    public void returnRental(int rentalId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Rental rental = session.get(Rental.class, rentalId);
            if (rental == null) {
                throw new IllegalArgumentException("Rental с id=" + rentalId + " не найден");
            }

            rental.setReturnDate(LocalDateTime.now());
            session.merge(rental);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
}
