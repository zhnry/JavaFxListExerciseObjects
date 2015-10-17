/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package populatelist;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import populatelist.exceptions.IllegalOrphanException;
import populatelist.exceptions.NonexistentEntityException;
import populatelist.exceptions.PreexistingEntityException;

/**
 *
 * @author Phil O'Connell <pxo4@psu.edu>
 */
public class TimeEntryJpaController implements Serializable {

  public TimeEntryJpaController(EntityManagerFactory emf) {
    this.emf = emf;
  }
  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(TimeEntry timeEntry) throws IllegalOrphanException, PreexistingEntityException, Exception {
    List<String> illegalOrphanMessages = null;
    TimeEntry timeEntry1OrphanCheck = timeEntry.getTimeEntry1();
    if (timeEntry1OrphanCheck != null) {
      TimeEntry oldTimeEntryOfTimeEntry1 = timeEntry1OrphanCheck.getTimeEntry();
      if (oldTimeEntryOfTimeEntry1 != null) {
        if (illegalOrphanMessages == null) {
          illegalOrphanMessages = new ArrayList<String>();
        }
        illegalOrphanMessages.add("The TimeEntry " + timeEntry1OrphanCheck + " already has an item of type TimeEntry whose timeEntry1 column cannot be null. Please make another selection for the timeEntry1 field.");
      }
    }
    if (illegalOrphanMessages != null) {
      throw new IllegalOrphanException(illegalOrphanMessages);
    }
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      TimeEntry timeEntryRel = timeEntry.getTimeEntry();
      if (timeEntryRel != null) {
        timeEntryRel = em.getReference(timeEntryRel.getClass(), timeEntryRel.getTimeEntryId());
        timeEntry.setTimeEntry(timeEntryRel);
      }
      TimeEntry timeEntry1 = timeEntry.getTimeEntry1();
      if (timeEntry1 != null) {
        timeEntry1 = em.getReference(timeEntry1.getClass(), timeEntry1.getTimeEntryId());
        timeEntry.setTimeEntry1(timeEntry1);
      }
      em.persist(timeEntry);
      if (timeEntryRel != null) {
        TimeEntry oldTimeEntry1OfTimeEntryRel = timeEntryRel.getTimeEntry1();
        if (oldTimeEntry1OfTimeEntryRel != null) {
          oldTimeEntry1OfTimeEntryRel.setTimeEntry(null);
          oldTimeEntry1OfTimeEntryRel = em.merge(oldTimeEntry1OfTimeEntryRel);
        }
        timeEntryRel.setTimeEntry1(timeEntry);
        timeEntryRel = em.merge(timeEntryRel);
      }
      if (timeEntry1 != null) {
        timeEntry1.setTimeEntry(timeEntry);
        timeEntry1 = em.merge(timeEntry1);
      }
      em.getTransaction().commit();
    } catch (Exception ex) {
      if (findTimeEntry(timeEntry.getTimeEntryId()) != null) {
        throw new PreexistingEntityException("TimeEntry " + timeEntry + " already exists.", ex);
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void edit(TimeEntry timeEntry) throws IllegalOrphanException, NonexistentEntityException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      TimeEntry persistentTimeEntry = em.find(TimeEntry.class, timeEntry.getTimeEntryId());
      TimeEntry timeEntryRelOld = persistentTimeEntry.getTimeEntry();
      TimeEntry timeEntryRelNew = timeEntry.getTimeEntry();
      TimeEntry timeEntry1Old = persistentTimeEntry.getTimeEntry1();
      TimeEntry timeEntry1New = timeEntry.getTimeEntry1();
      List<String> illegalOrphanMessages = null;
      if (timeEntryRelOld != null && !timeEntryRelOld.equals(timeEntryRelNew)) {
        if (illegalOrphanMessages == null) {
          illegalOrphanMessages = new ArrayList<String>();
        }
        illegalOrphanMessages.add("You must retain TimeEntry " + timeEntryRelOld + " since its timeEntry1 field is not nullable.");
      }
      if (timeEntry1New != null && !timeEntry1New.equals(timeEntry1Old)) {
        TimeEntry oldTimeEntryOfTimeEntry1 = timeEntry1New.getTimeEntry();
        if (oldTimeEntryOfTimeEntry1 != null) {
          if (illegalOrphanMessages == null) {
            illegalOrphanMessages = new ArrayList<String>();
          }
          illegalOrphanMessages.add("The TimeEntry " + timeEntry1New + " already has an item of type TimeEntry whose timeEntry1 column cannot be null. Please make another selection for the timeEntry1 field.");
        }
      }
      if (illegalOrphanMessages != null) {
        throw new IllegalOrphanException(illegalOrphanMessages);
      }
      if (timeEntryRelNew != null) {
        timeEntryRelNew = em.getReference(timeEntryRelNew.getClass(), timeEntryRelNew.getTimeEntryId());
        timeEntry.setTimeEntry(timeEntryRelNew);
      }
      if (timeEntry1New != null) {
        timeEntry1New = em.getReference(timeEntry1New.getClass(), timeEntry1New.getTimeEntryId());
        timeEntry.setTimeEntry1(timeEntry1New);
      }
      timeEntry = em.merge(timeEntry);
      if (timeEntryRelNew != null && !timeEntryRelNew.equals(timeEntryRelOld)) {
        TimeEntry oldTimeEntry1OfTimeEntryRel = timeEntryRelNew.getTimeEntry1();
        if (oldTimeEntry1OfTimeEntryRel != null) {
          oldTimeEntry1OfTimeEntryRel.setTimeEntry(null);
          oldTimeEntry1OfTimeEntryRel = em.merge(oldTimeEntry1OfTimeEntryRel);
        }
        timeEntryRelNew.setTimeEntry1(timeEntry);
        timeEntryRelNew = em.merge(timeEntryRelNew);
      }
      if (timeEntry1Old != null && !timeEntry1Old.equals(timeEntry1New)) {
        timeEntry1Old.setTimeEntry(null);
        timeEntry1Old = em.merge(timeEntry1Old);
      }
      if (timeEntry1New != null && !timeEntry1New.equals(timeEntry1Old)) {
        timeEntry1New.setTimeEntry(timeEntry);
        timeEntry1New = em.merge(timeEntry1New);
      }
      em.getTransaction().commit();
    } catch (Exception ex) {
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        Long id = timeEntry.getTimeEntryId();
        if (findTimeEntry(id) == null) {
          throw new NonexistentEntityException("The timeEntry with id " + id + " no longer exists.");
        }
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      TimeEntry timeEntry;
      try {
        timeEntry = em.getReference(TimeEntry.class, id);
        timeEntry.getTimeEntryId();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException("The timeEntry with id " + id + " no longer exists.", enfe);
      }
      List<String> illegalOrphanMessages = null;
      TimeEntry timeEntryOrphanCheck = timeEntry.getTimeEntry();
      if (timeEntryOrphanCheck != null) {
        if (illegalOrphanMessages == null) {
          illegalOrphanMessages = new ArrayList<String>();
        }
        illegalOrphanMessages.add("This TimeEntry (" + timeEntry + ") cannot be destroyed since the TimeEntry " + timeEntryOrphanCheck + " in its timeEntry field has a non-nullable timeEntry1 field.");
      }
      if (illegalOrphanMessages != null) {
        throw new IllegalOrphanException(illegalOrphanMessages);
      }
      TimeEntry timeEntry1 = timeEntry.getTimeEntry1();
      if (timeEntry1 != null) {
        timeEntry1.setTimeEntry(null);
        timeEntry1 = em.merge(timeEntry1);
      }
      em.remove(timeEntry);
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public List<TimeEntry> findTimeEntryEntities() {
    return findTimeEntryEntities(true, -1, -1);
  }

  public List<TimeEntry> findTimeEntryEntities(int maxResults, int firstResult) {
    return findTimeEntryEntities(false, maxResults, firstResult);
  }

  private List<TimeEntry> findTimeEntryEntities(boolean all, int maxResults, int firstResult) {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(TimeEntry.class));
      Query q = em.createQuery(cq);
      if (!all) {
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
      }
      return q.getResultList();
    } finally {
      em.close();
    }
  }

  public TimeEntry findTimeEntry(Long id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(TimeEntry.class, id);
    } finally {
      em.close();
    }
  }

  public int getTimeEntryCount() {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      Root<TimeEntry> rt = cq.from(TimeEntry.class);
      cq.select(em.getCriteriaBuilder().count(rt));
      Query q = em.createQuery(cq);
      return ((Long) q.getSingleResult()).intValue();
    } finally {
      em.close();
    }
  }
  
}
