package regis.dinvoke.weave;

import java.util.Map;
import java.util.Set;

import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodHandle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ChangeToDInvokeMethodVisitor extends MethodAdapter {

	private Map<String, MethodEntry> registered;

	public ChangeToDInvokeMethodVisitor(MethodVisitor mv,
			Map<String, MethodEntry> registered) {
		super(mv);
		this.registered = registered;
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name,
			String desc) {
		MethodEntry method = registered.get(new MethodEntry(owner, name, desc)
				.toString());
		if (method != null) {
			MethodEntry bootstrapDesc = method.getBootstrap();
			MethodHandle bootstrap = new MethodHandle(Opcodes.MH_INVOKESTATIC,
					bootstrapDesc.getOwner(), bootstrapDesc.getName(),
					bootstrapDesc.getDesc());

			mv.visitInvokeDynamicInsn(name, desc, bootstrap, owner, opcode);
		} else {
			mv.visitMethodInsn(opcode, owner, name, desc);
		}
	}
}
