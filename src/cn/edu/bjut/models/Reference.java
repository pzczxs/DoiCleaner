package cn.edu.bjut.models;

/*
 * A cleaning method of DOI names of cited references in Web of Science.  
 * 
 * References: 
 * [1] Shuo Xu, Liyuan Hao, Xin An, Dongsheng Zhai, and Hongshen Pang, 2019. Types of 
 * DOI Errors of Cited References in Web of Science with a Cleaning Method. Scientometrics, 
 * Vol. 120, No. 3, pp. 1427-1437. DOI: https://doi.org/10.1007/s11192-019-03162-4 
 * 
 * Author: XU, Shuo (pzczxs@gmail.com)
 * */

public class Reference {
	private String text; 
	private String doiOriginal; 
	private String doi; 
	private boolean flag; // flag = true when doi is illegal or multiple dois exist. 
	
	public Reference(String text, String doiOriginal, String doi, boolean flag) {
		this.text = text; 
		this.doiOriginal = doiOriginal; 
		this.doi = doi; 
		this.flag = flag; 
	}
	
	public Reference(String text, String doiOriginal, String doi) {
		this(text, doiOriginal, doi, false); 
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDoiOriginal() {
		return doiOriginal;
	}

	public void setDoiOriginal(String doiOriginal) {
		this.doiOriginal = doiOriginal;
	}

	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(); 
		
		sb.append(text); 
		
		if (doiOriginal != null) {
			sb.append("\t" + doiOriginal + "\t" + doi); 
		}
		
		if (flag) {
			sb.append("\t" + flag); 
		}
		
		return sb.toString();
	}
}
