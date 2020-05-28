package com.unixkitty.convenient_gadgetry;

import net.minecraftforge.common.ForgeConfigSpec;

@SuppressWarnings("CanBeFinal")
public class Config
{

    public static ForgeConfigSpec COMMON_CONFIG;
    //public static ForgeConfigSpec CLIENT_CONFIG; This will be needed for client-specific options

    /* BEGIN ENTRIES */

    public static final String CATEGORY_MISC = "miscellaneous";

    public static ForgeConfigSpec.BooleanValue grinderPlayPopSound;

    /* END ENTRIES */

    /* Non-configurable */

    static
    {
        ForgeConfigSpec.Builder commonConfig = new ForgeConfigSpec.Builder();

        commonConfig.push(CATEGORY_MISC);
        grinderPlayPopSound = commonConfig.comment("Should the grinder block play a pop sound when something gets fully ground up?").define("grinderPlayPopSound", true);
        commonConfig.pop();

        COMMON_CONFIG = commonConfig.build();
    }
}
