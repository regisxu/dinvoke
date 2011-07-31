package regis.dinvoke.weave;

import regis.dinvoke.BootstrapUtils;

public class MethodDescription {

	private String owner;

	private String name;

	private String desc;

	private MethodDescription bootstrap = BootstrapUtils.DEFAULT_BOOTSTRAP;

	public MethodDescription() {

	}

	public MethodDescription(String owner, String name, String desc) {
		this.owner = owner;
		this.name = name;
		this.desc = desc;
	}

	public MethodDescription getBootstrap() {
		return bootstrap;
	}

	void setOwner(String owner) {
		this.owner = owner;
	}

	void setName(String name) {
		this.name = name;
	}

	void setDesc(String desc) {
		this.desc = desc;
	}

	void setBootstrap(MethodDescription bootstrap) {
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
		if (o instanceof MethodDescription) {
			MethodDescription entry = (MethodDescription) o;
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
