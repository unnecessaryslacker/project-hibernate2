package org.example.service;

import org.example.entity.*;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RentalService {

    public Rental rentInventory(int customerId, int inventoryId, int staffId, BigDecimal amount) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Inventory inventory = session.get(Inventory.class, inventoryId);
            if (inventory == null) {
                throw new IllegalArgumentException("Inventory с id=" + inventoryId + " не найден");
            }
            Query<Rental> q = session.createQuery(
                    "FROM Rental r WHERE r.inventory = :inv ORDER BY r.rentalDate DESC", Rental.class);
            q.setParameter("inv", inventory);
            q.setMaxResults(1);
            Optional<Rental> last = q.uniqueResultOptional();
            if (last.isPresent() && last.get().getReturnDate() == null) {
                throw new IllegalStateException("Инвентарь id=" + inventoryId + " уже в аренде");
            }

            Customer customer = session.get(Customer.class, customerId);
            Staff staff         = session.get(Staff.class, staffId);
            if (customer == null || staff == null) {
                throw new IllegalArgumentException("Customer или Staff не найдены");
            }

            Rental rental = new Rental();
            rental.setRentalDate(LocalDateTime.now());
            rental.setInventory(inventory);
            rental.setCustomer(customer);
            rental.setStaff(staff);
            rental.setReturnDate(null);
            session.persist(rental);

            Payment payment = new Payment();
            payment.setAmount(amount);
            payment.setPaymentDate(LocalDateTime.now());
            payment.setCustomer(customer);
            payment.setStaff(staff);
            payment.setRental(rental);
            session.persist(payment);

            tx.commit();
            return rental;
        } catch (Exception ex) {
            if (tx != null) tx.rollback();
            throw ex;
        }
    }

    public Film addNewFilm(String title,
                           String description,
                           int languageId,
                           List<Integer> actorIds,
                           List<Integer> categoryIds,
                           int storeId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Language language = session.get(Language.class, languageId);
            Store store       = session.get(Store.class, storeId);
            if (language == null || store == null) {
                throw new IllegalArgumentException("Language или Store не найдены");
            }

            Film film = new Film();
            film.setTitle(title);
            film.setDescription(description);
            film.setLanguage(language);
            session.persist(film);

            FilmText ft = new FilmText();
            ft.setTitle(title);
            ft.setDescription(description);
            ft.setFilm(film);
            session.persist(ft);

            for (Integer aid : actorIds) {
                Actor actor = session.get(Actor.class, aid);
                if (actor != null) {
                    film.getActors().add(actor);
                    actor.getFilms().add(film);
                }
            }
            for (Integer cid : categoryIds) {
                Category cat = session.get(Category.class, cid);
                if (cat != null) {
                    film.getCategories().add(cat);
                    cat.getFilms().add(film);
                }
            }

            Inventory inv = new Inventory();
            inv.setFilm(film);
            inv.setStore(store);
            session.persist(inv);

            tx.commit();
            return film;
        } catch (Exception ex) {
            if (tx != null) tx.rollback();
            throw ex;
        }
    }
}
