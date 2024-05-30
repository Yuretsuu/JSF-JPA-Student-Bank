/**
 * 
 */
package databank.ejb;

import java.io.Serializable;
import java.util.List;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import databank.model.StudentPojo;

/**
 * 
 */
@Singleton
public class StudentService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Add Logger Class for logging purposes
	private static final Logger LOG = LogManager.getLogger();
	
	@PersistenceContext( name = "PU_DataBank")
	protected EntityManager em;
	
	//Default constructor
	public StudentService(){
		
	}

    public List<StudentPojo> findAll() {
        LOG.debug("reading all students");
        TypedQuery<StudentPojo> allStudentsQuery = em.createNamedQuery(StudentPojo.STUDENT_FIND_ALL, StudentPojo.class);
        return allStudentsQuery.getResultList();
    }
    
    @Transactional
    public StudentPojo persistStudent(StudentPojo student) {
        LOG.debug("creating a student = {}", student);
        em.persist(student);
        return student;
    }
    
    @Transactional
    public StudentPojo findById(int studentId) {
        LOG.debug("read a specific student = {}", studentId);
        return em.find(StudentPojo.class, studentId);
    }
    

    @Transactional
    public StudentPojo mergeStudent(StudentPojo studentWithUpdates) {
        try {
            LOG.debug("updating a specific student = {}", studentWithUpdates);
            return em.merge(studentWithUpdates);
        } catch (OptimisticLockException ole) {
            LOG.error("Concurrency conflict when updating student: {}", studentWithUpdates, ole);
            
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                "The record was updated by another transaction. Please refresh and try again.", null));
            
            // Since the transaction has failed, you may need to either:
            // a) Return null or the unmodified entity to indicate no update was performed
            // b) Handle the rollback if necessary (e.g. mark the transaction for rollback)
            context.getExternalContext().getFlash().setKeepMessages(true);
            return null; // Or the original studentWithUpdates object
        }
    }

    
    @Transactional
    public void removeStudent(int studentId) {
        LOG.debug("deleting a specific studentID = {}", studentId);
        StudentPojo student = findById(studentId);
        
        LOG.debug("Now deleting a specific student = {}", student);
        if (student != null) {
            em.remove(em.contains(student) ? student : em.merge(student));
        }
    } 
}
