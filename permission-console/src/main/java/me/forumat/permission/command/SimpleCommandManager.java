package me.forumat.permission.command;

import com.iwebpp.crypto.TweetNaclFast;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SimpleCommandManager {

    @Getter private Map<String, Consumer<String[]>> commands = new HashMap<>();

}
