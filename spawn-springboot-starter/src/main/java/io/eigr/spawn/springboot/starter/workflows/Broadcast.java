package io.eigr.spawn.springboot.starter.workflows;

import com.google.protobuf.Any;
import com.google.protobuf.GeneratedMessageV3;
import io.eigr.functions.protocol.Protocol;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public final class Broadcast<T extends GeneratedMessageV3> {

    private final  Optional<String> channel;
    private final Optional<String> command;
    private final T payload;

    private Broadcast( Optional<String> channel,  Optional<String> command, T payload) {
        this.channel = channel;
        this.command = command;
        this.payload = payload;
    }

    @NotNull
    public Broadcast<T> to(String channel, String command, T payload) {
        return new Broadcast<>(Optional.of(channel), Optional.of(command), payload);
    }

    @NotNull
    public Broadcast<T> to(String channel, T payload) {
        return new Broadcast<>(Optional.ofNullable(channel), Optional.empty(), payload);
    }

    public  Optional<String> getChannel() {
        return channel;
    }

    public Optional<String>  getCommand() {
        return command;
    }

    public T getPayload() {
        return payload;
    }

    public Protocol.Broadcast build() {
        Protocol.Broadcast.Builder builder = Protocol.Broadcast.newBuilder();
        if (this.command.isPresent()) {
            builder.setCommandName(this.command.get());
        }

        if (this.channel.isPresent()) {
            builder.setCommandName(this.channel.get());
        }

        if (Objects.isNull(payload)) {
            builder.setNoop(Protocol.Noop.newBuilder().build());
        } else {
            builder.setValue(Any.pack(payload));
        }

        return builder.build();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Broadcast{");
        sb.append("channel='").append(channel).append('\'');
        sb.append(", command=").append(command);
        sb.append(", payload=").append(payload);
        sb.append('}');
        return sb.toString();
    }
}
