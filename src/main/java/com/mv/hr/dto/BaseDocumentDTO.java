package com.mv.hr.dto;

import java.io.Serializable;

public class BaseDocumentDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	public String ParentType;
	public String InvestigationReference;
	public String CaseNumber;
	public String TaskReference;
	public String TaskNumber;
	public String DocumentReference;
	public String DocumentType;
	public String Name;
	public String MimeType;
	public String FileType;
	public int FileSize;
}
