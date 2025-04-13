package com.foursquare.heapaudit;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.Opcodes;

class HeapMULTIARRAY extends HeapUtil {

    // Allocations by MULTIARRAY are triggered via calls to
    // visitMultiANewArrayInsn where it returns a reference to the newly
    // allocated multi-dimension array object.

    static void after(boolean debug,
                      boolean trace,
                      MethodAdapter mv,
                      String desc) {

        log(debug,
            trace,
            mv,
            "\tMULTIARRAY.after(" + desc + ")");

        Label finish = new Label();

        if (HeapSettings.conditional) {

            // STACK: [...|obj]
            visitCheck(mv,
                       finish);
            // STACK: [...|obj]

        }

        // STACK: [...|obj]
        mv.visitInsn(Opcodes.DUP);
        // STACK: [...|obj|obj]
        mv.visitLdcInsn(desc);
        // STACK: [...|obj|obj|type]
        mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                           "com/foursquare/heapaudit/HeapUtil",
                           "record",
                           "(Ljava/lang/Object;Ljava/lang/String;)V");
        // STACK: [...|obj]

        if (HeapSettings.conditional) {

            // STACK: [...|obj]
            visitFinish(mv,
                        finish);
            // STACK: [...|obj]

        }

    }

}
