package regis.dinvoke.weave;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.EmptyVisitor;

import regis.dinvoke.InvokeDynamic;


public class Weaver {

	private Set<MethodEntry> dynamicMethods;

	public Weaver(Set<MethodEntry> methods) {
		dynamicMethods = methods;
	}

	public byte[] weave(byte[] in) {
		return weave(new ClassReader(in));
	}

	public byte[] weave(InputStream in) throws IOException {
		return weave(new ClassReader(in));
	}

	private byte[] weave(ClassReader reader) {
		ClassWriter writer = new ClassWriter(0);
		ClassAdapter adapter = new ClassAdapter(writer) {
			@Override
			public MethodVisitor visitMethod(final int access,
					final String name, final String desc,
					final String signature, final String[] exceptions) {
				MethodVisitor mv = cv.visitMethod(access, name, desc,
						signature, exceptions);
				if (mv != null) {
					mv = new ChangeToDInvokeMethodVisitor(mv, dynamicMethods);
				}

				return mv;
			}
		};

		reader.accept(adapter, 0);
		return writer.toByteArray();
	}

	public static HashSet<MethodEntry> collectAnnotatedMethods(InputStream in) throws IOException {
		ClassReader reader = new ClassReader(in);
		ClassVisitor visitor = new EmptyVisitor();

		String annStr = InvokeDynamic.class.getName();
		annStr = annStr.replace('.', '/');
		annStr = "L" + annStr + ";";
		AnnotationDetectClassAdapter detector = new AnnotationDetectClassAdapter(
				visitor, reader.getClassName(), annStr);

		reader.accept(detector, 0);

		final HashSet<MethodEntry> methods = new HashSet<MethodEntry>(
				detector.getAnnotationedMethods());
		return methods;
	}
	
	public static void main(String[] args) {
		Map<String, String> options = new HashMap<String, String>();
		options = parseArgs(args);
		String sourcePaths = null;
		String destinationPath = null;
		for (Map.Entry<String, String> entry : options.entrySet()) {
			switch (entry.getKey()) {
			case "-s":
				sourcePaths = entry.getValue();
				break;
			case "-d":
				destinationPath = entry.getValue();
				break;
			}
		}

		final Set<MethodEntry> methods = new HashSet<MethodEntry>();
		for (String path : sourcePaths.split(File.pathSeparator)) {
			Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
						throws IOException {
					Objects.requireNonNull(file);
					Objects.requireNonNull(attrs);
					if (file.toString().endsWith(".class")) {
						HashSet<MethodEntry> set = Weaver.collectAnnotatedMethods(Files
								.newInputStream(file, StandardOpenOption.READ));
						methods.addAll(set);
					}
					return FileVisitResult.CONTINUE;
				}
			});
		}

		Weaver weaver = new Weaver(methods);
		for (String path : sourcePaths.split(File.pathSeparator)) {
			WeaverFileVisitor weaverFileVisitor = new WeaverFileVisitor(weaver,
					Paths.get(path), Paths.get(destinationPath));
			Files.walkFileTree(Paths.get(path), weaverFileVisitor);
		}
	}

	private static Map<String, String> parseArgs(String[] args) {
		// TODO Auto-generated method stub
		return null;
	}
}
