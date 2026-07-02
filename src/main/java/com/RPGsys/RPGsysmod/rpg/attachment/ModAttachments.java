package com.RPGsys.RPGsysmod.rpg.attachment;

import com.RPGsys.RPGsysmod.ExampleMod;
import com.RPGsys.RPGsysmod.rpg.data.RPGData;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(
                    net.neoforged.neoforge.registries.NeoForgeRegistries.ATTACHMENT_TYPES,
                    ExampleMod.MODID
            );

    public static final Supplier<AttachmentType<RPGData>> RPG_DATA =
            ATTACHMENTS.register(
                    "rpg_data",
                    () -> AttachmentType
                            .serializable(() -> new RPGData(0, false))
                            .copyOnDeath()
                            .build()
            );
}
