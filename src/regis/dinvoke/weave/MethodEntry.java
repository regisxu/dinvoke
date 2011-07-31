package regis.dinvoke.weave;

import regis.dinvoke.BootstrapUtils;

public class MethodEntry {

	private String owner;

	private String name;

	private String desc;

	private MethodEntry bootstrap = BootstrapUtils.DEFAULT_BOOTSTRAP;

	public MethodEntry() {

	}

	public MethodEntry(String owner, String name, String desc) {
		this.owner = owner;
		this.name = name;
		this.desc = desc;
	}

	public MethodEntry getBootstrap() {
		return bootstrap;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setBootstrap(MethodEntry bootstrap) {
		this.bootstrap = bootstrap;
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

	public String toString() {
		return owner + "." + name + desc;
	}
}
