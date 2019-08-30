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

public class DoiStatus {
	private String doi; 
	private boolean status; 
	
	public DoiStatus(String doi, boolean status) {
		this.doi = doi; 
		this.status = status; 
	}

	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
