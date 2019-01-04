package me.zor.enchantfix;

import jdk.internal.org.objectweb.asm.*;
import jdk.internal.org.objectweb.asm.tree.*;
import net.minecraft.launchwrapper.IClassTransformer;

import javax.swing.*;
import java.util.Map;

import static jdk.internal.org.objectweb.asm.Opcodes.ICONST_0;
import static jdk.internal.org.objectweb.asm.Opcodes.IRETURN;

public class EnchantmentTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String s1, byte[] bytes) {
            if(name.equalsIgnoreCase("adb")) /* class names are obfuscated */
            {
                ClassReader reader = new ClassReader(bytes);
                ClassWriter writer = new ClassWriter(reader, 0);

                ClassVisitor visitor = new ClassVisitor(Opcodes.ASM4, writer) {
                    @Override
                    public MethodVisitor visitMethod(int access, final String name, String desc, String signature, String[] exceptions) {
                        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
                        if (!name.contains("hasEffect")) return mv; /* but method names aren't (lol) */

                        /* visit method */
                        MethodVisitor mv2 = new MethodVisitor(Opcodes.ASM4, mv) {
                            public void visitCode() {
                                mv.visitInsn(ICONST_0); /* push 0 onto stack (false) */
                                mv.visitInsn(IRETURN); /* return stack aka false */
                            }
                        };

                        return mv2;
                    }
                };

                reader.accept(visitor, 0);
                return writer.toByteArray();
            }
        return bytes;
    }
}
