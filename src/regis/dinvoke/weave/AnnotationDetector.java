package regis.dinvoke.weave;

import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;

public class AnnotationDetector extends ClassAdapter {

	private String className;

	private String target;

	private Set<MethodDescription> found = new HashSet<MethodDescription>();

	public AnnotationDetector(ClassVisitor cv, String className,
			String target) {
		super(cv);
		this.className = className;
		this.target = target;
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name,
			final String desc, final String signature, final String[] exceptions) {

		MethodVisitor mv = cv.visitMethod(access, name, desc, signature,
				exceptions);

		if (mv != null) {
			mv = new MethodAdapter(mv) {
				public AnnotationVisitor visitAnnotation(final String adesc,
						final boolean visible) {
					AnnotationVisitor av = mv.visitAnnotation(adesc, visible);
					if (adesc.equals(target)) {
						MethodDescription method = new MethodDescription(className, name,
								desc);
						AnnotationAdapter annotationAdapter = new AnnotationAdapter(
								av, method);
						found.add(method);
						return annotationAdapter;
					}
					return av;
				}
			};
		}
		return mv;
	}

	public Set<MethodDescription> getAnnotationedMethods() {
		return found;
	}
}
