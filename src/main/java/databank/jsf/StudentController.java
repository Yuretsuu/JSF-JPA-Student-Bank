/********************************************************************************************************2*4*w*
 * File:  StudentController.java Course materials CST8277
 *
 * @author Teddy Yap
 * @author Shahriar (Shawn) Emami
 * @author (original) Mike Norman
 */
package databank.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.enterprise.context.SessionScoped;
import javax.faces.annotation.ManagedProperty;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import databank.dao.ListDataDao;
import databank.dao.StudentDao;
import databank.model.StudentPojo;

/**
 * <p>
 * Description:  Responsible for collection of StudentPojo's in XHTML (list) <h:dataTable> </br>
 * Delegates all C-R-U-D behavior to DAO
 * </p>
 * 
 * <p>
 * This class is complete.
 * </p>
 */
@Named("studentController")
@SessionScoped
public class StudentController implements Serializable {
	private static final long serialVersionUID = 1L;

	//Get the log4j2 logger for this class
	private static final Logger LOG = LogManager.getLogger();

	public static final String UICONSTS_BUNDLE_EXPR = "#{uiconsts}";
	public static final String STUDENT_MISSING_REFRESH_BUNDLE_MSG = "refresh";
	public static final String STUDENT_OUTOFDATE_REFRESH_BUNDLE_MSG = "outOfDate";

	@Inject
	protected FacesContext facesContext;

	@Inject
	protected StudentDao studentDao;

	@Inject
	protected ListDataDao listDataDao;

	@Inject
	@ManagedProperty(UICONSTS_BUNDLE_EXPR)
	protected ResourceBundle uiconsts;

	protected List<StudentPojo> students;
	//Boolean used for toggling the rendering of add student in index.xhtml
	protected boolean adding;

	public void loadStudents() {
		LOG.debug("loadStudents");
		students = studentDao.readAllStudents();
	}

	public List<StudentPojo> getStudents() {
		return this.students;
	}

	public void setStudents(List<StudentPojo> students) {
		this.students = students;
	}

	public boolean isAdding() {
		return adding;
	}

	public void setAdding(boolean adding) {
		this.adding = adding;
	}

	/**
	 * Toggles the add student mode which determines whether the addStudent form is rendered
	 */
	public void toggleAdding() {
		this.adding = !this.adding;
	}

	public String editStudent(StudentPojo student) {
		LOG.debug("editStudent = {}", student);
		student.setEditable(true);
		return null; //Stay on current page
	}

	public String updateStudent(StudentPojo studentWithEdits) {
	    LOG.debug("updateStudent = {}", studentWithEdits);
	    try {
	        StudentPojo studentToBeUpdated = studentDao.readStudentById(studentWithEdits.getId());
	        if (studentToBeUpdated == null) {
	            LOG.debug("FAILED update student, does not exist = {}", studentWithEdits);
	            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                "Student no longer exists.", null));
	            return null; // Stay on the current page
	        }
	        
	        studentDao.updateStudent(studentWithEdits);
	        studentToBeUpdated.setEditable(false);
	        // Update the list of students to reflect the changes
	        // ... your code to update the students list ...
	    } catch (OptimisticLockException ole) {
	        LOG.error("Concurrency conflict when updating student: {}", studentWithEdits, ole);
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
	            "The record was updated by another transaction. Please refresh and try again.", null));
	        // Optionally, trigger a redirect to refresh the page
	        return "index.xhtml?faces-redirect=true";
	    }
	    return null; // Stay on the current page
	}

	public String cancelUpdate(StudentPojo student) {
		LOG.debug("cancelUpdate = {}", student);
		student.setEditable(false);
		return null; //Stay on current page
	}

	public void deleteStudent(int studentId) {
		LOG.debug("deleteStudent = {}", studentId);
		StudentPojo studentToBeRemoved = studentDao.readStudentById(studentId);
		if (studentToBeRemoved == null) {
			LOG.debug("failed deleteStudent does not exists = {}", studentId);
			return;
		}
		studentDao.deleteStudentById(studentId);
		students.remove(studentToBeRemoved);
	}

	public void addNewStudent(StudentPojo student) {
		LOG.debug("adding new student = {}", student);
		StudentPojo newStudent = studentDao.createStudent(student);
		students.add(newStudent);
	}

	public String refreshStudentForm() {
		LOG.debug("refreshStudentForm");
		//Clear all messaged in facesContext first
		Iterator<FacesMessage> facesMessageIterator = facesContext.getMessages();
		while (facesMessageIterator.hasNext()) {
			facesMessageIterator.remove();
		}
		return "index.xhtml?faces-redirect=true";
	}

	public List<String> getPrograms() {
		return listDataDao.readAllPrograms();
	}
	
}
