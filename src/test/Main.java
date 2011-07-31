package test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.MethodType;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodHandle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.EmptyVisitor;

import regis.dinvoke.BootstrapUtils;
import regis.dinvoke.InvokeDynamic;
import regis.dinvoke.weave.AnnotationDetectClassAdapter;
import regis.dinvoke.weave.ChangeToDInvokeMethodVisitor;
import regis.dinvoke.weave.MethodEntry;

public class Main extends ClassLoader implements Opcodes {

	public static void generateExample() throws Exception {
		// Generates the bytecode corresponding to the following Java class:
		//
		// public class Example {
		// public static void main (String[] args) {
		// System.out.println("Hello world!");
		// }
		// }

		// creates a ClassWriter for the Example public class,
		// which inherits from Object
		ClassWriter cw = new ClassWriter(0);
		cw.visit(V1_7, ACC_PUBLIC, "Example", null, "java/lang/Object", null);

		// creates a MethodWriter for the (implicit) constructor
		MethodVisitor mw = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null,
				null);
		// pushes the 'this' variable
		mw.visitVarInsn(ALOAD, 0);
		// invokes the super class constructor
		mw.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
		mw.visitInsn(RETURN);
		// this code uses a maximum of one stack element and one local variable
		mw.visitMaxs(1, 1);
		mw.visitEnd();

		// creates a MethodWriter for the 'main' method
		mw = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main",
				"([Ljava/lang/String;)V", null, null);
		// pushes the 'out' field (of type PrintStream) of the System class
		mw.visitFieldInsn(GETSTATIC, "java/lang/System", "out",
				"Ljava/io/PrintStream;");
		// pushes the "Hello World!" String constant
		mw.visitLdcInsn("Hello world!");
		// invokes the 'println' method (defined in the PrintStream class)
		mw.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
				"(Ljava/lang/String;)V");

		MethodHandle bootstrap = new MethodHandle(
				Opcodes.MH_INVOKESTATIC,
				"Test",
				"boostrap",
				"(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;");
		String type = MethodType.methodType(String.class, String.class)
				.toMethodDescriptorString();
		mw.visitLdcInsn("Hello world!");
		mw.visitInvokeDynamicInsn("test", type, bootstrap);

		mw.visitInsn(RETURN);
		// this code uses a maximum of two stack elements and two local
		// variables
		mw.visitMaxs(2, 2);
		mw.visitEnd();

		// gets the bytecode of the Example class, and loads it dynamically
		byte[] code = cw.toByteArray();

		FileOutputStream fos = new FileOutputStream("bin/Example.class");
		fos.write(code);
		fos.close();
	}

	public static void main(String[] args) throws Exception {
		// OutputStream out = Files.newOutputStream(Paths.get("test"));
		//
		// Class clazz = Test.class;
		// final HashSet<MethodEntry> methods = detectAnnotationedMethods(
		// Test.class, InvokeDynamic.class);
		//
		// ClassReader reader = new ClassReader(clazz.getName());
		// ClassWriter writer = new ClassWriter(0);
		// ClassAdapter adapter = new ClassAdapter(writer) {
		// @Override
		// public MethodVisitor visitMethod(final int access,
		// final String name, final String desc,
		// final String signature, final String[] exceptions) {
		// MethodVisitor mv = cv.visitMethod(access, name, desc,
		// signature, exceptions);
		// if (mv != null) {
		// mv = new ChangeToDInvokeMethodVisitor(mv, methods);
		// }
		//
		// return mv;
		// }
		// };
		//
		// reader.accept(adapter, 0);
		// byte[] bs = writer.toByteArray();
		//
		// String path = clazz.getName().replace('.', '/') + ".class";
		// FileOutputStream fos = new FileOutputStream("inst/" + path);
		// fos.write(bs);
		// fos.close();
		new Test().empty();
		Test.test("hello");
	}
}
