package Controller;

public class Document {
	int dataid;
	int topicid;

	public Document(int dataid, int topicid) {
		super();
		this.dataid = dataid;
		this.topicid = topicid;
	}

	public int getDataid() {
		return dataid;
	}

	public void setDataid(int dataid) {
		this.dataid = dataid;
	}

	public int getTopicid() {
		return topicid;
	}

	public void setTopicid(int topicid) {
		this.topicid = topicid;
	}

}
