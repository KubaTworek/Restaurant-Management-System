package pl.jakubtworek.common;


import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommandBus {
    private final Map<Class<? extends Command>, CommandHandler<?>> handlers = new HashMap<>();

    public CommandBus(List<CommandHandler<?>> commandHandlers) {
        commandHandlers.forEach(handler -> handlers.put(resolveCommandType(handler), handler));
    }

    private <T extends Command> Class<T> resolveCommandType(CommandHandler<?> handler) {
        ParameterizedType parameterizedType = (ParameterizedType) handler.getClass().getGenericInterfaces()[0];
        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    public void dispatch(Command command) {
        CommandHandler<Command> handler = (CommandHandler<Command>) handlers.get(command.getClass());
        if (handler != null) {
            handler.handle(command);
        } else {
            throw new UnsupportedOperationException("No handler found for command: " + command.getClass());
        }
    }
}
