package regis.dinvoke.weave;

import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;

public class AnnotationDetectClassAdapter extends ClassAdapter {

	private String className;
	
	private String target;

	private List<MethodEntry> found = new LinkedList<MethodEntry>();

	public AnnotationDetectClassAdapter(ClassVisitor cv, String className, String target) {
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
					if (adesc.equals(target)) {
						found.add(new MethodEntry(className, name, desc));
					}
					return mv.visitAnnotation(adesc, visible);
				}
			};
		}
		return mv;
	}
	
	public List<MethodEntry> getAnnotationedMethods() {
		return found;
	}
}
