/********************************************************************************************************2*4*w*
 * File:  StudentDaoImpl.java Course materials CST8277
 *
 * @author Teddy Yap
 * @author Shahriar (Shawn) Emami
 * @author (original) Mike Norman
 */
package databank.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import databank.ejb.StudentService;
import databank.model.StudentPojo;

/**
 * Description:  Implements the C-R-U-D API for the database
 * 
 * TODO 01 - Some components are managed by CDI.<br>
 * TODO 02 - Methods which perform DML need @Transactional annotation.<br>
 * TODO 03 - Fix the syntax errors to correct methods. <br>
 * TODO 04 Complete - Refactor this class.  Move all the method bodies and EntityManager to a new service class (e.g. StudentService) which is a
 * singleton (EJB).<br>
 * TODO Complete 05 - Inject the service class using EJB.<br>
 * TODO 06 Complete - Call all the methods of service class from each appropriate method here.
 */
@Named
@ApplicationScoped
public class StudentDaoImpl implements StudentDao, Serializable {
	/** explicitly set serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	@EJB
	protected StudentService service;
	protected EntityManager em;

	@Override
	public List<StudentPojo> readAllStudents() {
		return service.findAll();
	}

	@Override
	public StudentPojo createStudent(StudentPojo student) {
		return service.persistStudent(student);
	}

	@Override
	public StudentPojo readStudentById(int studentId) {
		return service.findById(studentId);
	}

	@Override
	public StudentPojo updateStudent(StudentPojo studentWithUpdates) {
		return service.mergeStudent(studentWithUpdates);
	}

	@Override
	public void deleteStudentById(int studentId) {
		service.removeStudent(studentId);
	}
}
