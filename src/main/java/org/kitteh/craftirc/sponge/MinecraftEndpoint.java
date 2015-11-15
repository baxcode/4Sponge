/*
 * * Copyright (C) 2015 Matt Baxter http://kitteh.org
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.kitteh.craftirc.sponge;

import org.kitteh.craftirc.endpoint.Endpoint;
import org.kitteh.craftirc.endpoint.TargetedMessage;
import org.kitteh.craftirc.util.MinecraftPlayer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Abstract root of Minecraft {$link}endpoints.
 */
public abstract class MinecraftEndpoint extends Endpoint {
    public static final String RECIPIENT_NAMES = "RECIPIENT_NAMES";

    private final SpongeIRC plugin;

    public MinecraftEndpoint(@Nonnull SpongeIRC plugin) {
        this.plugin = plugin;
        this.plugin.getGame().getEventManager().registerListeners(plugin, this);
    }

    @Nonnull
    protected SpongeIRC getPlugin() {
        return this.plugin;
    }

    @Override
    protected void preProcessReceivedMessage(@Nonnull TargetedMessage message) {
        Set<MinecraftPlayer> players = this.playerCollectionToMinecraftPlayer(this.plugin.getGame().getServer().getOnlinePlayers());
        message.getCustomData().put(MinecraftEndpoint.RECIPIENT_NAMES, players);
    }

    @Nullable
    protected String getStringFromStringOrText(Object o) {
        if (o instanceof String) {
            return (String) o;
        }
        if (o instanceof Text) {
            return Texts.toPlain((Text) o);
        }
        return null;
    }

    @Nonnull
    protected Set<MinecraftPlayer> playerCollectionToMinecraftPlayer(@Nonnull Collection<Player> collection) {
        return collection.stream().map(player -> new MinecraftPlayer(player.getName(), player.getUniqueId())).collect(Collectors.toCollection(HashSet::new));
    }
}