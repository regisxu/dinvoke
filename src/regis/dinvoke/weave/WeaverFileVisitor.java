package regis.dinvoke.weave;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

public class WeaverFileVisitor extends SimpleFileVisitor<Path> {

	private Weaver weaver;

	private Path destDir;

	private Path src;

	public WeaverFileVisitor(Weaver weaver, Path src, Path des) {
		this.weaver = weaver;
		this.src = src;
		this.destDir = des;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			throws IOException {
		Objects.requireNonNull(file);
		Objects.requireNonNull(attrs);
		if (file.toString().endsWith(".class")) {
			byte[] bs = weaver.weave(Files.newInputStream(file,
					StandardOpenOption.READ));
			Path sub = file.subpath(src.getNameCount(), file.getNameCount());
			Path dest = destDir.resolve(sub);
			Files.createDirectories(dest.getParent());
			OutputStream out = Files.newOutputStream(dest);
			out.write(bs);
			out.close();
		}
		return FileVisitResult.CONTINUE;
	}

}
