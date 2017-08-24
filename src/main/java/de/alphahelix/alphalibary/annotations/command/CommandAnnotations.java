package de.alphahelix.alphalibary.annotations.command;

import de.alphahelix.alphalibary.annotations.IAnnotation;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class CommandAnnotations implements IAnnotation {

    public Set<AnnotatedCommand> registerCommands(Object clazzObj) {
        Class<?> clazz = clazzObj.getClass();
        Set<AnnotatedCommand> registeredCMDs = new HashSet<>();

        Set<Method> completionMethods = new HashSet<>();

        for (Method method : clazz.getDeclaredMethods()) {
            Complete complete = method.getAnnotation(Complete.class);
            if (complete != null) completionMethods.add(method);
        }

        for (Method method : clazz.getDeclaredMethods()) {
            Command command = method.getAnnotation(Command.class);

            if (command == null) continue;

            Permission permission = method.getAnnotation(Permission.class);

            Method completeM = null;
            Complete complete = null;

            for (Method m : completionMethods) {
                if (m.getName().equals(method.getName())) {
                    completeM = m;
                    complete = m.getAnnotation(Complete.class);
                }
            }

            AnnotatedCommand annotatedCommand = new AnnotatedCommand(clazzObj, method, command, permission, completeM, complete);
            registeredCMDs.add(annotatedCommand.register());
        }

        return registeredCMDs;
    }

    @Override
    public void load(Object clazz) {
        registerCommands(clazz);
    }
}
