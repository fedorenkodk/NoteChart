package com.satilianius.db;


import com.satilianius.models.Note;
import com.satilianius.models.Price;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;

public class DbAccessor {

    private SessionFactory factory;

    public DbAccessor() {
        createSessionFactory();
    }

    private void createSessionFactory() {
        try {
            this.factory = new Configuration()
                    .configure()
                    .addAnnotatedClass(Note.class)
                    .addAnnotatedClass(Price.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        System.out.println("SessionFactory created successfully");
    }

    public List<Note> getAllNotes(){
        try (Session session = factory.openSession()) {
            CriteriaQuery<Note> criteriaQuery = session
                    .getCriteriaBuilder()
                    .createQuery(Note.class);

            Root<Note> rootEntry = criteriaQuery.from(Note.class);
            CriteriaQuery<Note> allNotesCQ = criteriaQuery.select(rootEntry);

            TypedQuery<Note> getAllQuery = session.createQuery(allNotesCQ);
            return getAllQuery.getResultList();
        }
    }

    public Integer addNote(Note note){
        Transaction tx = null;
        Integer noteID = null;

        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            noteID = (Integer) session.save(note);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }
        return noteID;
    }

    @Transactional
    public void removeNote(Note note) {
        Transaction tx = null;

        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.remove(note);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }
    }

    public void addPrice(Price price) {
        Transaction tx = null;

        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.save(price);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }
    }

    public List<Price> getPrices(String symbol, ZonedDateTime startDate, ZonedDateTime endDate) {
        try (Session session = factory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Price> criteriaQuery = criteriaBuilder.createQuery(Price.class);

            Root<Price> rootEntry = criteriaQuery.from(Price.class);
            CriteriaQuery<Price> allPricesCQ = criteriaQuery
                    .select(rootEntry)
                    .where(criteriaBuilder.and(
                            criteriaBuilder.equal(rootEntry.get("symbol"), symbol),
                            criteriaBuilder.gt(rootEntry.get("timestamp"), startDate.toEpochSecond()),
                            criteriaBuilder.lt(rootEntry.get("timestamp"), endDate.toEpochSecond())));

            TypedQuery<Price> getAllQuery = session.createQuery(allPricesCQ);
            return getAllQuery.getResultList();
        }
    }
}
