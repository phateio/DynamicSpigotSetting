import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    `java-library`
    alias(libs.plugins.yml.bukkit)
    alias(libs.plugins.paperweight.userdev)
}

val versionPlugin = "1.3"
val versionMinecraft = "1.21.10"

group = "io.github.phateio"
version = "$versionPlugin-mc${versionMinecraft.replace('.', '_')}"


repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("$versionMinecraft-R0.1-SNAPSHOT")
}

paperweight {
    reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

    javaLauncher = javaToolchains.launcherFor {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }
}

bukkit {
    name = project.name
    version = project.version.toString()
    main = "io.github.phateio.dynamicspigotsetting.MainClass"

    apiVersion = "1.13"

    author = "WhiteCat"

    commands {
        register("DynamicSpigotSetting") {
            description = "main command"
            aliases = listOf("dss")

            usage = """
                /<command> <item|exp|> radius
                /<command> reload
            """.trimIndent()

            permission = "dynamicspigotsetting.manage"
        }
    }

    permissions {
        register("dynamicspigotsetting.manage") {
            default = BukkitPluginDescription.Permission.Default.OP
        }
    }
}
