package dev.triumphteam.gui.builder.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.triumphteam.gui.components.exception.GuiException;
import dev.triumphteam.gui.components.util.SkullUtil;
import dev.triumphteam.gui.components.util.VersionHelper;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Field;
import java.util.UUID;

public final class SkullBuilder extends BaseItemBuilder<SkullBuilder> {

    private static final Field PROFILE_FIELD;

    static {
        Field field;

        try {
            final SkullMeta skullMeta = (SkullMeta) SkullUtil.skull().getItemMeta();
            field = skullMeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            field = null;
        }

        PROFILE_FIELD = field;
    }

    SkullBuilder() {
        super(SkullUtil.skull());
    }

    SkullBuilder(final @NotNull ItemStack itemStack) {
        super(itemStack);
        if (!SkullUtil.isPlayerSkull(itemStack)) {
            throw new GuiException("SkullBuilder requires the material to be a PLAYER_HEAD/SKULL_ITEM!");
        }
    }

    /**
     * Sets the skull texture using a BASE64 string
     *
     * @param texture The base64 texture
     * @param profileId The unique id of the profile
     * @return {@link SkullBuilder}
     */
    @NotNull
    @Contract("_, _ -> this")
    public SkullBuilder texture(@NotNull final String texture, @NotNull final UUID profileId) {
        if (!SkullUtil.isPlayerSkull(getItemStack())) return this;

        if (VersionHelper.IS_PLAYER_PROFILE_API) {
            final String textureUrl = SkullUtil.getSkinUrl(texture);

            if (textureUrl == null) {
                return this;
            }
            final SkullMeta skullMeta = (SkullMeta) getMeta();
            setMeta(skullMeta);
            return this;
        }

        if (PROFILE_FIELD == null) {
            return this;
        }

        final SkullMeta skullMeta = (SkullMeta) getMeta();
        final GameProfile profile = new GameProfile(profileId, "");
        profile.getProperties().put("textures", new Property("textures", texture));

        try {
            PROFILE_FIELD.set(skullMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            ex.printStackTrace();
        }

        setMeta(skullMeta);
        return this;
    }

    /**
     * Sets the skull texture using a BASE64 string
     *
     * @param texture The base64 texture
     * @return {@link SkullBuilder}
     */
    @NotNull
    @Contract("_ -> this")
    public SkullBuilder texture(@NotNull final String texture) {
        return texture(texture, UUID.randomUUID());
    }

    /**
     * Sets skull owner via bukkit methods
     *
     * @param player {@link OfflinePlayer} to set skull of
     * @return {@link SkullBuilder}
     */
    @NotNull
    @Contract("_ -> this")
    public SkullBuilder owner(@NotNull final OfflinePlayer player) {
        if (!SkullUtil.isPlayerSkull(getItemStack())) return this;

        final SkullMeta skullMeta = (SkullMeta) getMeta();

        if (VersionHelper.IS_SKULL_OWNER_LEGACY) {
            skullMeta.setOwner(player.getName());
        } else {
            skullMeta.setOwningPlayer(player);
        }

        setMeta(skullMeta);
        return this;
    }

}
