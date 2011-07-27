package regis.dinvoke.weave;

public class MethodEntry {

	private String owner;

	private String name;

	private String desc;

	public MethodEntry(String owner, String name, String desc) {
		this.owner = owner;
		this.name = name;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public String getOwner() {
		return owner;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof MethodEntry) {
			MethodEntry entry = (MethodEntry) o;
			return this.desc.equals(entry.desc)
					&& this.owner.equals(entry.owner)
					&& this.name.equals(entry.name);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return owner.hashCode() + name.hashCode() + desc.hashCode();
	}
}
