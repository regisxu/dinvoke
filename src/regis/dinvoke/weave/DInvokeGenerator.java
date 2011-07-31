package regis.dinvoke.weave;

import java.util.Map;

import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodHandle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class DInvokeGenerator extends MethodAdapter {

	private Map<String, MethodDescription> registered;

	public DInvokeGenerator(MethodVisitor mv,
			Map<String, MethodDescription> registered) {
		super(mv);
		this.registered = registered;
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name,
			String desc) {
		MethodDescription method = registered.get(new MethodDescription(owner, name, desc)
				.toString());
		if (method != null) {
			MethodDescription bootstrapDesc = method.getBootstrap();
			MethodHandle bootstrap = new MethodHandle(Opcodes.MH_INVOKESTATIC,
					bootstrapDesc.getOwner(), bootstrapDesc.getName(),
					bootstrapDesc.getDesc());

			mv.visitInvokeDynamicInsn(name, desc, bootstrap, owner, opcode);
		} else {
			mv.visitMethodInsn(opcode, owner, name, desc);
		}
	}
}
