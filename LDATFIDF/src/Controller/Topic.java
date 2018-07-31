package Controller;

public class Topic {
	int topicId;
	String clustertitle;
	double score;

	public Topic(int topicId, String clustertitle, double score) {
		super();
		this.topicId = topicId;
		this.clustertitle = clustertitle;
		this.score = score;
	}

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public String getclustertitle() {
		return clustertitle;
	}

	public void setclustertitle(String clustertitle) {
		this.clustertitle = clustertitle;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

}
