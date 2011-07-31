package test;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

import regis.dinvoke.Bootstrap;
import regis.dinvoke.BootstrapUtils;
import regis.dinvoke.InvokeDynamic;

public class Test {
	@InvokeDynamic
	public static void test(String s) {
		System.out.println("i'm test");
	}

	@InvokeDynamic
	public void empty() {
		System.out.println("empty");
	}

	public void hello(String s) {
		System.out.println(s);
	}

	public static CallSite boostrap(Lookup lookup, String name,
			MethodType methodType) {
		System.out.println("construct the constant ");
		// MethodHandle m = MethodHandles.identity(Void.class);
		// System.out.println(methodType.toMethodDescriptorString());
		// System.out.println(m.type().toMethodDescriptorString());
		// System.out.println(methodType.equals(m.type()));
		// CallSite site = new ConstantCallSite(m);
		// System.out.println(site.getTarget().type());
		// return new ConstantCallSite(m);
		try {
			return new ConstantCallSite(lookup.findStatic(Test.class, name,
					methodType));
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws Throwable {
		// Test.empty();
		// Test.test("hello world");
		Lookup lookup = MethodHandles.lookup();

		Method[] ms = BootstrapUtils.class.getDeclaredMethods();
		for (Method method : ms) {
			if (method.getName().equals("bootstrap")) {
				MethodHandle mh = lookup.unreflect(method);
				System.out.println(mh.type().toMethodDescriptorString());

			}
		}

	}
}
