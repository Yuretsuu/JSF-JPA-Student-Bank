/********************************************************************************************************2*4*w*
 * File:  NewStudentView.java Course materials CST8277
 *
 * @author Teddy Yap
 * @author Shahriar (Shawn) Emami
 * @author (original) Mike Norman
 */
package databank.jsf;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.faces.annotation.ManagedProperty;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Version;

import databank.model.StudentPojo;

/**
 * This class represents the scope of adding a new student to the DB.
 * 
 * TODO 09 - This class is a managed bean.  Use the name "newStudent".<br>
 * TODO 10 - Like previous assignment where StudentPojo was view scoped, this class is?<br>
 * TODO 11 Complete - Add the missing variables and their getters and setters.  Have in mind dates and version are internal
 * only.<br>
 * 
 */
@Named("newStudent") // TODO 09 completed
@ViewScoped // TODO 10 completed
public class NewStudentView implements Serializable {
	/** explicit set serialVersionUID */
	private static final long serialVersionUID = 1L;

	protected String lastName;
	protected String firstName;
	private String email;
	private String phoneNumber;
	private String program;

	@Inject
	@ManagedProperty("#{studentController}")
	protected StudentController studentController;

	public NewStudentView() {
	}

	/**
	 * @return lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the program
	 */
	public String getProgram() {
		return program;
	}

	/**
	 * @param program the program to set
	 */
	public void setProgram(String program) {
		this.program = program;
	}

	//Add Student Method
	public void addStudent() {
		if (allNotNullOrEmpty(firstName, lastName /* TODO 11 - Don't forget to add the other variables that are not nullable */)) {
			StudentPojo theNewStudent = new StudentPojo();
			
			theNewStudent.setFirstName(getFirstName());
			theNewStudent.setLastName(getLastName());
			theNewStudent.setEmail(getEmail());
			theNewStudent.setPhoneNumber(getPhoneNumber());
			theNewStudent.setProgram(getProgram());
			
			studentController.addNewStudent(theNewStudent);
			//Clean up
			studentController.toggleAdding();
			setFirstName(null);
			setLastName(null);
			setEmail(null);
			setPhoneNumber(null);
			setProgram(null);
			
		}
	}

	static boolean allNotNullOrEmpty(final Object... values) {
		if (values == null) {
			return false;
		}
		for (final Object val : values) {
			if (val == null) {
				return false;
			}
			if (val instanceof String) {
				String str = (String) val;
				if (str.isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}
}
