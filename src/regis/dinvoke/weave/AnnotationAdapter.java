package regis.dinvoke.weave;

import org.objectweb.asm.AnnotationVisitor;

import regis.dinvoke.BootstrapUtils;

public class AnnotationAdapter implements AnnotationVisitor {

	private AnnotationVisitor av;

	private MethodDescription method;

	public AnnotationAdapter(AnnotationVisitor av, MethodDescription method) {
		this.av = av;
		this.method = method;
	}

	@Override
	public void visit(String name, Object value) {
		MethodDescription bootstrap = method.getBootstrap();
		switch (name) {
		case "className":
			if (value == null || "".equals(value)) {
				bootstrap.setOwner(BootstrapUtils.DEFAULT_BOOTSTRAP.getOwner());
			} else {
				bootstrap.setOwner((String) value);
			}
			break;
		case "methodName":
			if (value == null || "".equals(value)) {
				bootstrap.setName(BootstrapUtils.DEFAULT_BOOTSTRAP.getOwner());
			} else {
				bootstrap.setName((String) value);
			}
			break;
		case "methodType":
			if (value == null || "".equals(value)) {
				bootstrap.setOwner(BootstrapUtils.DEFAULT_BOOTSTRAP.getOwner());
			} else {
				bootstrap.setDesc((String) value);
			}
		}
	}

	public MethodDescription getMethod() {
		return method;
	}

	@Override
	public void visitEnum(String name, String desc, String value) {
		av.visitEnum(name, desc, value);
	}

	@Override
	public AnnotationVisitor visitAnnotation(String name, String desc) {
		if (name.equals("bootstrap")) {
			method.setBootstrap(new MethodDescription());
			return this;
		} else {
			return av;
		}
	}

	@Override
	public AnnotationVisitor visitArray(String name) {
		return av;
	}

	@Override
	public void visitEnd() {
		av.visitEnd();
	}
}
