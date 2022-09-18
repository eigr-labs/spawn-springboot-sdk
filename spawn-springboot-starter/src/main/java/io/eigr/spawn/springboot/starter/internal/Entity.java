package io.eigr.spawn.springboot.starter.internal;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
public final class Entity {
    private String actorName;
    private Class<?> actorType;

    private Class stateType;
    private String actorBeanName;
    private boolean persistent;

    private long deactivateTimeout;

    private long snapshotTimeout;
    private Map<String, EntityMethod> commands = new HashMap<>();

    public Entity(String actorName, Class<?> actorType, Class stateType, String actorBeanName, boolean persistent, long deactivateTimeout, long snapshotTimeout, Map<String, EntityMethod> commands) {
        this.actorName = actorName;
        this.actorType = actorType;
        this.stateType = stateType;
        this.actorBeanName = actorBeanName;
        this.persistent = persistent;
        this.deactivateTimeout = deactivateTimeout;
        this.snapshotTimeout = snapshotTimeout;
        this.commands = commands;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public Class<?> getActorType() {
        return actorType;
    }

    public Class getStateType() {
        return stateType;
    }

    public String getActorBeanName() {
        return actorBeanName;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public long getDeactivateTimeout() {
        return deactivateTimeout;
    }

    public long getSnapshotTimeout() {
        return snapshotTimeout;
    }

    public Map<String, EntityMethod> getCommands() {
        return commands;
    }

    public static final class EntityMethod {
        private String name;
        private Method method;
        private Class<?> inputType;
        private Class<?> outputType;

        public EntityMethod(String name, Method method, Class<?> inputType, Class<?> outputType) {
            this.name = name;
            this.method = method;
            this.inputType = inputType;
            this.outputType = outputType;
        }

        public String getName() {
            return name;
        }

        public Method getMethod() {
            return method;
        }

        public Class getInputType() {
            return inputType;
        }

        public Class<?> getOutputType() {
            return outputType;
        }
    }

}
