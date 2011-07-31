package regis.dinvoke;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

import org.objectweb.asm.Opcodes;

public class BootstrapUtils {

	public static CallSite bootstrap(Lookup lookup, String name,
			MethodType methodType, Object... bsmArgs) {

		try {
			String className = ((String) bsmArgs[0]).replace('/', '.');
			Class owner = Class.forName(className);
			int opcode = ((Integer) bsmArgs[1]).intValue();
			MethodHandle handler = null;
			switch (opcode) {
			case Opcodes.INVOKEINTERFACE:
			case Opcodes.INVOKEVIRTUAL:
				handler = lookup.findVirtual(owner, name, methodType);
				break;
			case Opcodes.INVOKESPECIAL:
				handler = lookup.findSpecial(owner, name, methodType, owner);
				break;
			case Opcodes.INVOKESTATIC:
				handler = lookup.findStatic(owner, name, methodType);
				break;
			}
			return new ConstantCallSite(handler);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
