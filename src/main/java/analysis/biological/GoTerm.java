package analysis.biological;

public class GoTerm {
	
	private String Id;
	
	private String Name;
	
	private double p;
	
	private double pAdjusted;
	
	private double pMin;
	
	private int popTerm;
	
	private int studyTerm;
		
	private int ps;

	
	public GoTerm(String id, String name, double p, double pAdjusted, double pMin, int popTerm, int studyTerm) {
		Id = id;
		Name = name;
		this.p = p;
		this.pAdjusted = pAdjusted;
		this.pMin = pMin;
		this.popTerm = popTerm;
		this.studyTerm = studyTerm;
		this.ps = popTerm - studyTerm;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return Id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}

	/**
	 * @return the p
	 */
	public double getP() {
		return p;
	}

	/**
	 * @return the pAdjusted
	 */
	public double getpAdjusted() {
		return pAdjusted;
	}

	/**
	 * @return the pMin
	 */
	public double getpMin() {
		return pMin;
	}

	/**
	 * @return the popTerm
	 */
	public int getPopTerm() {
		return popTerm;
	}

	/**
	 * @return the studyTerm
	 */
	public int getStudyTerm() {
		return studyTerm;
	}
	
	/**
	 * @return the ps
	 */
	public int getPs() {
		return ps;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Id+"\t"+pAdjusted+"\t"+p+"\t"+Name+"\t"+pMin+"\t"+popTerm+"\t"+studyTerm+"\t"+ps;
	}



	
	

}
