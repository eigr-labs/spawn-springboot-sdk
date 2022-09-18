package io.eigr.spawn.example;

import com.google.protobuf.Any;
import io.eigr.functions.protocol.Protocol;
import io.eigr.functions.protocol.actors.ActorOuterClass;
import io.eigr.spawn.springboot.starter.SpawnSystem;
import io.eigr.spawn.springboot.starter.autoconfigure.EnableSpawn;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.stream.Stream;

@Log4j2
@EnableSpawn
@SpringBootApplication
@EntityScan("io.eigr.spawn.example")
public class App {

    public static final String SPAWN_PROXY_ACTORS_ACTOR_INVOKE_URL = "http://localhost:9001/api/v1/system/test-system/actors/actor-test-01/invoke";
    public static final String SPAWN_MEDIA_TYPE = "application/octet-stream";
    public static final String SPAWN_PROXY_ACTORSYSTEM_URL = "http://localhost:9001/api/v1/system";
    private final OkHttpClient client = new OkHttpClient();
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            /*
            HashMap<String, ActorOuterClass.Actor> actors = new HashMap<>();
            for (int i = 0; i < 60000; i++) {
                String actorName = String.format("actor-test-%s", i);
                actors.put(actorName, makeActor(actorName, 1));
            }

            ActorOuterClass.Registry registry = ActorOuterClass.Registry.newBuilder()
                    .putAllActors(actors)
                    .build();

            ActorOuterClass.ActorSystem actorSystem = ActorOuterClass.ActorSystem.newBuilder()
                    .setName("test-system")
                    .setRegistry(registry)
                    .build();

            Protocol.ServiceInfo si = Protocol.ServiceInfo.newBuilder()
                    .setServiceName("jvm-sdk")
                    .setServiceVersion("0.1.1")
                    .setServiceRuntime(System.getProperty("java.version"))
                    .setProtocolMajorVersion(1)
                    .setProtocolMinorVersion(1)
                    .build();

            Protocol.RegistrationRequest registration = Protocol.RegistrationRequest.newBuilder()
                    .setServiceInfo(si)
                    .setActorSystem(actorSystem)
                    .build();

            RequestBody body = RequestBody.create(
                    registration.toByteArray(), MediaType.parse(SPAWN_MEDIA_TYPE));

            Request request = new Request.Builder()
                    .url(SPAWN_PROXY_ACTORSYSTEM_URL)
                    .post(body)
                    .build();

            log.info("Send registration request...");
            Call call = client.newCall(request);
            try (Response response = call.execute()) {
                Protocol.RegistrationResponse registrationResponse = Protocol.RegistrationResponse
                        .parseFrom(response.body().bytes());
                log.info("Registration response: {}", registrationResponse);
            }

            Thread.sleep(5000);
            */

            SpawnSystem actorSystem = ctx.getBean(SpawnSystem.class);

            for (int i = 0; i < 1000; i++) {
                String actorName = String.format("concreteActor-%s", i);
                log.info("Let's spawning Actor {}", actorName);
                actorSystem.spawn(actorName, AbstractActor.class);
            }

            for (int i = 0; i < 1000; i++) {
                String actorName = String.format("concreteActor-%s", i);
                log.info("Let's invoke {}", actorName);
                MyBusinessMessage input = MyBusinessMessage.newBuilder().setValue(1).build();
                actorSystem.invoke(actorName, "sum", input, MyBusinessMessage.class);
            }

            /*
            log.info("Let's invoke some Actor");
            for (int i = 0; i < 50000; i++) {
                MyBusinessMessage arg = MyBusinessMessage.newBuilder().setValue(i).build();

                Instant initialInvokeRequestTime = Instant.now();
                MyBusinessMessage sumResult =
                        (MyBusinessMessage) actorSystem.invoke("joe", "sum", arg, MyBusinessMessage.class);

                log.info("Actor invoke Sum Actor Action value result: {}. Request Time Elapsed: {}ms",
                        sumResult.getValue(), Duration.between(initialInvokeRequestTime, Instant.now()).toMillis());
            }

            Instant initialInvokeRequestTime = Instant.now();
            MyBusinessMessage getResult =
                    (MyBusinessMessage) actorSystem.invoke("joe", "get", MyBusinessMessage.class);
            log.info("Actor invoke Get Actor Action value result: {}. Request Time Elapsed: {}ms",
                    getResult.getValue(), Duration.between(initialInvokeRequestTime, Instant.now()).toMillis());
            */

        };

    }

    private ActorOuterClass.Actor makeActor(String name, Integer state) {

        MyBusinessMessage valueMessage = MyBusinessMessage.newBuilder()
                .setValue(state)
                .build();

        Any stateValue = Any.pack(valueMessage);

        ActorOuterClass.ActorState initialState = ActorOuterClass.ActorState.newBuilder()
                .setState(stateValue)
                .build();

        ActorOuterClass.ActorSnapshotStrategy snapshotStrategy = ActorOuterClass.ActorSnapshotStrategy.newBuilder()
                .setTimeout(ActorOuterClass.TimeoutStrategy.newBuilder().setTimeout(10000).build())
                .build();

        ActorOuterClass.ActorDeactivateStrategy deactivateStrategy = ActorOuterClass.ActorDeactivateStrategy.newBuilder()
                .setTimeout(ActorOuterClass.TimeoutStrategy.newBuilder().setTimeout(120000).build())
                .build();

        return ActorOuterClass.Actor.newBuilder()
                .setName(name)
                .setPersistent(true)
                .setState(initialState)
                .setSnapshotStrategy(snapshotStrategy)
                .setDeactivateStrategy(deactivateStrategy)
                .build();
    }
}