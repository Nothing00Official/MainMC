package mainmc.nothing00.functions;

import java.util.ArrayList;
import java.util.List;

import mainmc.folders.UserData;

public class Mail extends UserData {

	private String sender;
	private String msg;

	public Mail(String sender, String to, String msg) {
		super(to);
		this.sender = sender;
		this.msg = msg;
	}

	public boolean hasMails() {
		return super.get().get("userdata.mail") != null;
	}

	public List<String> getMails() {
		if (hasMails())
			return super.getStringList("userdata.mail");
		return new ArrayList<String>();
	}

	public void sendMail() {
		List<String> list = new ArrayList<String>();
		if (this.hasMails())
			list = this.getMails();
		list.add(this.sender + ": " + this.msg);
		super.get().set("userdata.mail", list);
		super.save();
	}

	public void clearMails() {
		super.get().set("userdata.mail", null);
		super.save();
	}

	public void removeMailAt(int pos) {
		List<String> list = new ArrayList<String>();
		if (this.hasMails())
			list = this.getMails();
		list.remove(list.get(pos));
		if (list.isEmpty())
			super.get().set("userdata.mail", null);
		else
			super.get().set("userdata.mail", list);
		super.save();
	}

}
