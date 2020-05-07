package com.unixkitty.convenient_gadgetry;

import net.minecraftforge.common.ForgeConfigSpec;

@SuppressWarnings("CanBeFinal")
public class Config
{

    public static ForgeConfigSpec COMMON_CONFIG;
    //public static ForgeConfigSpec CLIENT_CONFIG; This will be needed for client-specific options

    /* BEGIN ENTRIES */



    /* END ENTRIES */

    /* Non-configurable */

    static
    {
        ForgeConfigSpec.Builder commonConfig = new ForgeConfigSpec.Builder();


        COMMON_CONFIG = commonConfig.build();
    }
}
