package regis.dinvoke.weave;

import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodHandle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ChangeToDInvokeMethodVisitor extends MethodAdapter {

	private Set<MethodEntry> registered = new HashSet<MethodEntry>();

	public ChangeToDInvokeMethodVisitor(MethodVisitor mv,
			Set<MethodEntry> registered) {
		super(mv);
		this.registered = registered;
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name,
			String desc) {
		if (registered.contains(new MethodEntry(owner, name, desc))) {
			MethodHandle bootstrap = new MethodHandle(
					Opcodes.MH_INVOKESTATIC,
					"Lregis/dinvoke/BootstrapUtils;",
					"bootstrap",
					"(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;");

			mv.visitInvokeDynamicInsn(name, desc, bootstrap, owner, opcode);
		} else {
			mv.visitMethodInsn(opcode, owner, name, desc);
		}
	}
}
