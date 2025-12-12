import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    `java-library`
    alias(libs.plugins.yml.bukkit)
}

group = "cf.catworlds"
version = "1.3-mc1_21_10"


repositories {
    mavenLocal()
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    compileOnly(libs.io.papermc.paper.paper.api)
    compileOnly(libs.org.spigotmc.spigot)
}

bukkit {
    name = project.name
    version = project.version.toString()
    main = "cf.catworlds.dynamicspigotsetting.MainClass"

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

java.sourceCompatibility = JavaVersion.VERSION_21

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}
