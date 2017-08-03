package labinterface;

public class TaskStatus {
	
	private int progress;
	private String description;
	
	public TaskStatus(int progress, String description) {
		super();
		this.progress = progress;
		this.description = description;
	}

	public int getProgress() {
		return progress;
	}

	public String getDescription() {
		return description;
	}
	
	
	
	

}
