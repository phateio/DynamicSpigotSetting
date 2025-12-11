# CLAUDE.md - AI Assistant Guide for DynamicSpigotSetting

## Project Overview

**DynamicSpigotSetting** is a Minecraft Spigot/Paper plugin that dynamically adjusts server performance settings based on real-time TPS (Ticks Per Second) monitoring. The plugin automatically modifies item and experience orb merge radii to help maintain server performance during high load situations.

- **Plugin Name**: DynamicSpigotSetting
- **Version**: 1.3-mc1_21_11
- **Package**: cf.catworlds.dynamicspigotsetting
- **Author**: WhiteCat
- **Minecraft Version**: 1.21.11
- **Java Version**: 21
- **Build Tool**: Maven
- **API**: Spigot/Paper API with NMS (Net Minecraft Server) access

## Contributing Guidelines

**IMPORTANT**: This project follows standardized contributing guidelines documented in `CONTRIBUTING.md`.

**Before making any changes**:
1. **Verify CONTRIBUTING.md is up-to-date**: `curl -s https://denpaio.github.io/CONTRIBUTING.md | diff CONTRIBUTING.md -`
2. **If outdated, sync first**: `curl -o CONTRIBUTING.md https://denpaio.github.io/CONTRIBUTING.md`
3. **Follow all standards** defined in CONTRIBUTING.md

**Key Requirements**:
- **Java Style**: Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- **Commit Messages**: Use [Conventional Commits](https://www.conventionalcommits.org/) format (e.g., `feat:`, `fix:`, `docs:`, `refactor:`)
- **Code Comments**: English preferred, follow existing context
- **Documentation**: English preferred
- **Before Committing**:
  - Run linters/formatters (google-java-format for Java)
  - Ensure all tests pass
  - Write self-documenting code

**Source of Truth**: The CONTRIBUTING.md file in this repository is synced from [denpaio.github.io/CONTRIBUTING.md](https://denpaio.github.io/CONTRIBUTING.md). Do not modify CONTRIBUTING.md directly; all changes must be made in the source repository.

## Core Functionality

The plugin monitors server TPS and dynamically adjusts:
1. **Item Merge Radius**: Controls how close items need to be to merge into stacks
2. **Experience Orb Merge Radius**: Controls how close experience orbs need to be to merge

When TPS drops, the plugin increases merge radii to reduce entity count and improve performance.

## Project Structure

```
DynamicSpigotSetting/
├── .github/
│   └── workflows/
│       └── release.yml          # GitHub Actions for releases
├── CONTRIBUTING.md              # Contributing guidelines (synced from source)
├── CLAUDE.md                    # AI Assistant guide (this file)
├── src/
│   └── main/
│       ├── java/cf/catworlds/dynamicspigotsetting/
│       │   ├── MainClass.java           # Plugin entry point
│       │   ├── Setting.java             # Configuration management with annotations
│       │   ├── command/
│       │   │   └── MainCommand.java     # Command handler with tab completion
│       │   ├── text/
│       │   │   ├── BasicText.java       # Enum for localized messages
│       │   │   ├── TextFormater.java    # Text formatting engine
│       │   │   └── TextHelper.java      # Text formatting helper
│       │   └── utils/
│       │       ├── ConfigHelper.java    # YAML config utilities
│       │       └── ServerHelper.java    # NMS wrapper for server access
│       └── resources/
│           └── plugin.yml               # Plugin metadata (uses Maven filtering)
├── pom.xml                              # Maven build configuration
└── .gitignore                           # Git ignore rules
```

## Key Components

### 1. MainClass.java (src/main/java/cf/catworlds/dynamicspigotsetting/MainClass.java)

**Purpose**: Plugin entry point extending JavaPlugin

**Key Methods**:
- `onEnable()`: Initializes plugin (loads text, config, commands, starts task)
- `reload()`: Cancels timer, reloads all configurations, restarts task
- `loadText()`: Loads localized messages from `lang/message.yml`
- `loadConfig()`: Loads plugin configuration with defaults
- `loadCommand()`: Registers the `/DynamicSpigotSetting` command
- `startTask()`: Creates BukkitRunnable that runs periodically to adjust merge radii

**Timer Logic**:
- Runs every `Setting.timerSec` seconds (default: 30)
- Gets current TPS using `ServerHelper.getTPS()`
- Iterates through TPS thresholds to determine appropriate merge radii
- Applies settings via `ServerHelper.setItemMerge()` and `ServerHelper.setExpMerge()`
- Logs actions when debug mode is enabled

### 2. Setting.java (src/main/java/cf/catworlds/dynamicspigotsetting/Setting.java)

**Purpose**: Configuration management using annotation-based reflection

**Key Features**:
- Uses `@SettingInfo` annotation to map fields to config paths
- Automatically loads and validates config values
- Parses TPS:Radius string lists into double arrays
- Provides type-safe error handling with fallback to defaults

**Fields**:
- `debug`: Enable/disable debug logging
- `timerSec`: How often to check TPS and adjust settings (seconds)
- `itemMergeList/expMergeList`: List of "TPS:Radius" pairs
- `itemMerge_TPS/Radius`: Parsed arrays for item merge thresholds
- `expMerge_TPS/Radius`: Parsed arrays for exp merge thresholds

**Configuration Format**:
```yaml
Debug: false
Timer_sec: 30
ItemMerge_TPS_Radius:
  - "20:0"    # Above 20 TPS: radius 0 (default)
  - "15:2"    # Below 15 TPS: radius 2
  - "10:2.5"  # Below 10 TPS: radius 2.5
ExpMerge_TPS_Radius:
  - "20:0"
  - "15:1"
  - "10:3.5"
```

### 3. ServerHelper.java (src/main/java/cf/catworlds/dynamicspigotsetting/utils/ServerHelper.java)

**Purpose**: NMS wrapper for accessing and modifying server internals

**IMPORTANT - NMS Access**: This class uses Net Minecraft Server (NMS) code which is version-specific and requires special handling:

**Methods**:
- `getTPS()`: Gets current TPS using Paper API (`Bukkit.getServer().getTPS()[0]`)
- `getItemMerge()`: Reads item merge radius from spigot config
- `setItemMerge(double)`: Sets item merge radius for all worlds
- `getExpMerge()`: Reads exp merge radius from spigot config
- `setExpMerge(double)`: Sets exp merge radius for all worlds

**Version Compatibility Note**:
- Recent fix switched from direct NMS field access to Paper's `getTPS()` API
- Uses `@SuppressWarnings("deprecation")` for spigotConfig access
- Iterates through all worlds via `MinecraftServer.getServer().getAllLevels()`

### 4. MainCommand.java (src/main/java/cf/catworlds/dynamicspigotsetting/command/MainCommand.java)

**Purpose**: Command handler implementing TabExecutor

**Commands**:
- `/dss item [radius]`: Get/set item merge radius
- `/dss exp [radius]`: Get/set exp merge radius
- `/dss reload`: Reload plugin configuration

**Features**:
- Tab completion for subcommands
- Error handling for invalid numbers
- Sends formatted messages using TextHelper

### 5. Text System (src/main/java/cf/catworlds/dynamicspigotsetting/text/)

**Architecture**: Three-class system for localization and formatting

**BasicText.java**:
- Enum defining all localizable messages
- Each entry has default text (Chinese by default) and format keys
- Format keys define placeholders (e.g., `${TPS}`, `${Radius}`)

**TextFormater.java**:
- Handles variable substitution in text templates
- Supports multi-line messages (List<String>)
- Replaces placeholders with actual values

**TextHelper.java**:
- Static wrapper for text formatting
- Loads messages from `lang/message.yml`
- Applies ChatColor codes (& color codes)
- Provides `format(BasicText, Object...)` method

**Usage Example**:
```java
TextHelper.format(BasicText.ItemMergeCommand, 2.5)
// Output: "成功調整 物品合併半徑為 2.5" (in green with yellow bold radius)
```

### 6. ConfigHelper.java (src/main/java/cf/catworlds/dynamicspigotsetting/utils/ConfigHelper.java)

**Purpose**: Utility for YAML file operations

**Methods**:
- `loadConfig(File)`: Loads or creates new YAML configuration
- `saveConfig(FileConfiguration, File)`: Saves config with auto-directory creation

## Build System

### Maven Configuration (pom.xml)

**Dependencies**:
1. **Paper API** (provided scope):
   - Artifact: `io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT`
   - Used for Bukkit/Spigot API access and Paper-specific features like `getTPS()`

2. **Spigot NMS** (provided scope):
   - Artifact: `org.spigotmc:spigot:1.21.11-R0.1-SNAPSHOT:remapped-mojang`
   - Used for direct server internals access (MinecraftServer, WorldServer)

**Build Process**:

1. **Maven Compiler** (v3.13.0):
   - Source/Target: Java 21
   - Source directory: `src/main/java`

2. **SpecialSource Maven Plugin** (v2.0.3):
   - **First Execution** (remap-obf): Remaps from Mojang mappings to obfuscated
   - **Second Execution** (remap-spigot): Remaps from obfuscated to Spigot mappings
   - Produces three JARs:
     - `DynamicSpigotSetting-{version}.jar` (original)
     - `DynamicSpigotSetting-{version}-remapped-obf.jar` (intermediate)
     - `DynamicSpigotSetting-{version}-remapped.jar` (final Spigot-compatible)

**Resource Filtering**:
- Enabled for `src/main/resources`
- Allows Maven properties in `plugin.yml`:
  - `${name}` → project.name
  - `${version}` → project.version

### Build Commands

```bash
# Clean and build
mvn clean package

# Just compile
mvn compile

# Install to local Maven repository
mvn install

# Clean build directory
mvn clean
```

**Output**: `target/DynamicSpigotSetting-{version}-remapped.jar` is the deployable plugin

## GitHub Actions Workflow

**File**: `.github/workflows/release.yml`

**Trigger**: When a GitHub release is created

**Steps**:
1. Checkout repository
2. Set up JDK 21 (Temurin distribution) with Maven cache
3. **Build Spigot with BuildTools** (30-minute timeout):
   - Downloads latest BuildTools.jar
   - Builds Spigot 1.21.11 with remapped sources
   - Required for NMS dependencies
4. Build with Maven (`mvn clean package`)
5. Find final JAR (excludes `remapped-obf.jar` and `original-` prefix)
6. Upload JAR to GitHub release

**Recent Changes** (from git history):
- Added Paper API dependency for `getTPS()` method
- Fixed NoSuchFieldError for `recentTps` field
- Refined BuildTools integration with logging and verification

## Development Workflows

### Setting Up Development Environment

1. **Prerequisites**:
   - JDK 21 (Temurin or similar)
   - Maven 3.x
   - Git
   - IDE with Maven support (IntelliJ IDEA recommended)

2. **Clone and Build**:
   ```bash
   git clone <repository-url>
   cd DynamicSpigotSetting
   mvn clean package
   ```

3. **IDE Setup**:
   - Import as Maven project
   - Set Java 21 as project SDK
   - Enable annotation processing for `@SettingInfo`

4. **Local Testing**:
   - Copy `target/DynamicSpigotSetting-*-remapped.jar` to test server's `plugins/` folder
   - Requires Spigot or Paper 1.21.11 server

### Making Code Changes

**Common Change Patterns**:

1. **Adding New Settings**:
   - Add field to `Setting.java` with `@SettingInfo` annotation
   - Set default value in `loadSetting()` method
   - Access via `Setting.fieldName` elsewhere

2. **Adding New Commands**:
   - Add to `FirstLevel` list in `MainCommand.java`
   - Add condition in `onCommand()` method
   - Add entry to tab completion in `onTabComplete()`

3. **Adding Localized Messages**:
   - Add enum entry to `BasicText.java` with format keys and default text
   - Use via `TextHelper.format(BasicText.YourMessage, args)`

4. **Modifying Server Settings**:
   - Add getter/setter methods to `ServerHelper.java`
   - Access `MinecraftServer.getServer()` for server instance
   - Use `getAllLevels()` to iterate worlds

### Testing Checklist

Before committing changes:

- [ ] Code compiles without errors: `mvn compile`
- [ ] Build produces JAR: `mvn package`
- [ ] Plugin loads on test server without errors
- [ ] Commands work with tab completion
- [ ] Configuration loads and saves correctly
- [ ] Reload command works properly
- [ ] Debug logging produces expected output
- [ ] No console errors during operation

### Git Workflow

**Branch Strategy**:
- Main branch: Stable releases
- Feature branches: `claude/*` prefix for AI-assisted development
- Always develop on designated branch, never push to main directly

**Commit Message Style** (from git log):
- Use conventional commits: `feat:`, `fix:`, `refactor:`, `docs:`
- Be descriptive: "Add Paper API dependency and use proper getTPS() method"
- Reference issues when applicable

**Pushing Changes**:
```bash
git add .
git commit -m "feat: your descriptive message"
git push -u origin <branch-name>
```

## Architecture Patterns

### 1. Annotation-Based Configuration

`Setting.java` uses a custom annotation pattern:
- `@SettingInfo(path = "Config.Path")` marks fields for auto-loading
- Reflection iterates fields at runtime
- Type-safe with error messages for mismatches
- Centralizes configuration logic

**Benefits**:
- DRY: No repetitive config.get() calls
- Type-safe with compile-time checking
- Easy to add new settings

### 2. Enum-Based Localization

`BasicText.java` uses enum pattern:
- Each message is a typed constant
- Default text embedded in code
- Format keys ensure type-safe substitution
- Overridable via external YAML

**Benefits**:
- Compile-time checking of message references
- Centralized message management
- Falls back to defaults if file missing

### 3. Static Helper Pattern

`ServerHelper`, `ConfigHelper`, `TextHelper` are static utility classes:
- No instantiation needed
- Pure functions for common operations
- Centralize NMS access in one place

**Benefits**:
- Simple API
- Easy to mock for testing
- Reduces coupling

### 4. Scheduled Task Pattern

Uses Bukkit's `BukkitRunnable`:
- Encapsulates periodic logic
- Automatically scheduled on server thread
- Cancellable for clean reload

**Pattern**:
```java
new BukkitRunnable() {
    @Override
    public void run() {
        // Periodic logic here
    }
}.runTaskTimer(plugin, initialDelay, period);
```

## Important Technical Notes

### NMS (Net Minecraft Server) Usage

**What is NMS?**
- Direct access to Minecraft server internals
- Not part of stable Bukkit/Spigot API
- **Version-specific**: Breaks between Minecraft versions
- Requires special build process (SpecialSource remapping)

**NMS Classes Used**:
- `net.minecraft.server.MinecraftServer`: Server instance
- `MinecraftServer.getAllLevels()`: Access all loaded worlds
- `WorldServer.spigotConfig`: Per-world Spigot configuration

**Why Use NMS Here?**
- Spigot configuration (item/exp merge) not exposed in Bukkit API
- `@SuppressWarnings("deprecation")` indicates this is non-API usage

**Migration Strategy**:
- Prefer Paper API when available (`getTPS()` was migrated)
- Wrap all NMS access in `ServerHelper` for easy version updates
- Document version compatibility in comments

### Version Compatibility

**Current Version**: 1.21.11
- Built against Spigot 1.21.11-R0.1-SNAPSHOT
- Uses Mojang mappings (remapped-mojang classifier)

**When Updating Minecraft Version**:
1. Update `<spigotVersion>` in `pom.xml`
2. Update BuildTools version in `release.yml`
3. Test NMS code in `ServerHelper.java`
4. Rebuild and test thoroughly

**API Changes to Watch**:
- `MinecraftServer` structure changes
- `spigotConfig` field availability
- Paper API additions that could replace NMS

### Maven Remapping Process

**Why Remapping?**
- Minecraft code is obfuscated
- Spigot uses custom mappings (different from Mojang's)
- Plugin must match server's mapping

**Remapping Steps**:
1. **Develop**: Write against Mojang mappings (readable names)
2. **remap-obf**: Convert Mojang → obfuscated (matches vanilla)
3. **remap-spigot**: Convert obfuscated → Spigot (matches server)

**Result**: Plugin JAR compatible with Spigot/Paper servers

### Resource Filtering Gotcha

`plugin.yml` uses Maven variables:
```yaml
name: ${name}
version: ${version}
```

**Important**: These are replaced at **build time**, not runtime
- Changes to `pom.xml` require rebuild
- Don't manually edit these in `plugin.yml`

## Configuration Files

### Plugin Configuration (config.yml)

Auto-generated in `plugins/DynamicSpigotSetting/config.yml`:

```yaml
Debug: false                    # Enable debug logging
Timer_sec: 30                   # Check interval in seconds
ItemMerge_TPS_Radius:           # Item merge thresholds
  - "20:0"                      # Above 20 TPS: default radius (0)
  - "15:2"                      # Below 15 TPS: radius 2
  - "10:2.5"                    # Below 10 TPS: radius 2.5
ExpMerge_TPS_Radius:            # Exp orb merge thresholds
  - "20:0"
  - "15:1"
  - "10:3.5"
```

**Format**: `"TPS:Radius"` pairs
- **TPS**: Threshold value (server TPS)
- **Radius**: Merge radius to apply below that TPS
- Order matters: List from high to low TPS

**Logic**: Plugin finds first threshold where `currentTPS > threshold` and applies that radius

### Localization File (lang/message.yml)

Auto-generated in `plugins/DynamicSpigotSetting/lang/message.yml`:

Messages support:
- Color codes with `&` (e.g., `&a` = green, `&e&l` = yellow bold)
- Variables with `${Variable}` syntax
- Multi-line messages (YAML list format)

**Example Customization**:
```yaml
ItemMergeCommand:
  - "&aSuccessfully set item merge radius to &e&l${Radius}"
ReloadSuccess:
  - "&aConfiguration reloaded!"
```

## Common AI Assistant Tasks

### 1. Adding a New Setting

**Example**: Add max-entities-per-chunk threshold

```java
// In Setting.java
@SettingInfo(path = "MaxEntitiesPerChunk")
static public int maxEntitiesPerChunk;

// In loadSetting() method (set default):
maxEntitiesPerChunk = 100;

// Use elsewhere:
if (entityCount > Setting.maxEntitiesPerChunk) {
    // Take action
}
```

### 2. Adding a New Command

**Example**: Add `/dss status` command

```java
// In MainCommand.java

// 1. Add to FirstLevel list:
private static final List<String> FirstLevel =
    Arrays.asList("item", "exp", "reload", "status");

// 2. Add handler in onCommand():
else if (args[0].equalsIgnoreCase(FirstLevel.get(3))) {
    double tps = ServerHelper.getTPS();
    sender.sendMessage("§aTPS: " + tps);
    sender.sendMessage("§aItem Radius: " + ServerHelper.getItemMerge());
    sender.sendMessage("§aExp Radius: " + ServerHelper.getExpMerge());
    return true;
}
```

### 3. Adding a New Message

**Example**: Add "settings applied" message

```java
// In BasicText.java
SettingsApplied(
    new String[]{"ItemRadius", "ExpRadius"},
    "&aSettings applied: Item=&e${ItemRadius}&a, Exp=&e${ExpRadius}"
),

// Usage:
TextHelper.format(BasicText.SettingsApplied, itemRadius, expRadius)
```

### 4. Modifying Server Behavior

**Example**: Add view distance adjustment

```java
// In ServerHelper.java
@SuppressWarnings("deprecation")
static public void setViewDistance(int distance) {
    MinecraftServer.getServer().getAllLevels().forEach(
        ws -> ws.spigotConfig.viewDistance = distance
    );
}

static public int getViewDistance() {
    return MinecraftServer.getServer()
        .getAllLevels().iterator().next().spigotConfig.viewDistance;
}
```

**Then integrate into main timer** in `MainClass.startTask()`.

### 5. Debugging Issues

Enable debug mode in config:
```yaml
Debug: true
```

This logs TPS and radius changes every cycle:
```
[WARNING] 目前的 TPS 為 18.5, 成功調整 物品合併半徑為 2.0
```

**Common Debug Points**:
- `MainClass.startTask()`: Timer execution
- `Setting.loadSetting()`: Config parsing
- `ServerHelper.getTPS()`: TPS reading
- Command execution in `MainCommand.onCommand()`

## Code Style Conventions

### Naming Conventions

- **Classes**: PascalCase (e.g., `MainClass`, `ServerHelper`)
- **Methods**: camelCase (e.g., `loadConfig`, `getTPS`)
- **Static Fields**: camelCase (e.g., `timerSec`, `itemMerge_TPS`)
- **Constants**: PascalCase for enums, UPPER_SNAKE_CASE for final constants
- **Packages**: lowercase (e.g., `cf.catworlds.dynamicspigotsetting`)

### Code Organization

- **One class per file**: Follow Java conventions
- **Static imports**: Avoided except for common utilities
- **Access modifiers**: Use `static public` instead of `public static` (project style)
- **Field organization**: Annotated fields first, then derived fields

### Formatting

- **Indentation**: 4 spaces (no tabs in Java code)
- **Braces**: Opening brace on same line
- **Line length**: Generally kept under 120 characters
- **Comments**: Javadoc for public APIs, inline for complex logic

### Error Handling

- **Catch-and-log**: Print stack traces for debugging
- **Fallback values**: Always provide defaults
- **User feedback**: Send error messages to command sender
- **Silent failures**: Only for reflection-based optional features

**Example Pattern**:
```java
try {
    // risky operation
} catch (Exception e) {
    sender.sendMessage("Error message");
    e.printStackTrace();
}
```

## Dependencies and Version Management

### Current Versions

- **Minecraft**: 1.21.11
- **Java**: 21
- **Maven Compiler**: 3.13.0
- **SpecialSource**: 2.0.3
- **Paper API**: 1.21.11-R0.1-SNAPSHOT
- **Spigot**: 1.21.11-R0.1-SNAPSHOT

### Updating Dependencies

**For Minecraft Version Updates**:
1. Update `<spigotVersion>` property in `pom.xml`
2. Update `--rev` parameter in `.github/workflows/release.yml`
3. Rebuild and test NMS compatibility
4. Update version in `<version>` tag to match

**For Plugin Version Updates**:
1. Edit `<version>` in `pom.xml`
2. Tag release: `git tag v1.x-mcX_XX_XX`
3. Push tag: `git push origin --tags`
4. Create GitHub release to trigger build

## Testing and Quality Assurance

### Manual Testing Checklist

**Plugin Loading**:
- [ ] Plugin loads without errors
- [ ] `config.yml` generated with defaults
- [ ] `lang/message.yml` generated with defaults
- [ ] No exceptions in console

**Commands**:
- [ ] `/dss item` shows current radius
- [ ] `/dss item 2.5` sets radius and confirms
- [ ] `/dss exp` shows current radius
- [ ] `/dss exp 3.0` sets radius and confirms
- [ ] `/dss reload` reloads config without errors
- [ ] Tab completion suggests: item, exp, reload
- [ ] Invalid arguments return usage message

**Dynamic Adjustments**:
- [ ] Monitor console logs with Debug: true
- [ ] Verify TPS is read correctly
- [ ] Verify merge radius changes based on TPS
- [ ] Check timer runs at configured interval
- [ ] Reload cancels old timer and starts new one

**Configuration**:
- [ ] Edit config, reload, verify changes applied
- [ ] Invalid TPS:Radius format handled gracefully
- [ ] Timer_sec change affects timer period
- [ ] Language changes in message.yml applied

### Server Compatibility Testing

Test on:
- [ ] Latest Spigot 1.21.11
- [ ] Latest Paper 1.21.11
- [ ] With other plugins (check conflicts)
- [ ] With low TPS simulation
- [ ] After server restart (config persistence)

## Troubleshooting Guide

### Build Issues

**Problem**: `mvn package` fails with "Cannot resolve dependencies"

**Solution**:
1. Ensure `~/.m2/repository` has Spigot artifacts
2. Run BuildTools manually:
   ```bash
   java -jar BuildTools.jar --rev 1.21.11 --remapped
   ```
3. Retry build

**Problem**: "Error remapping artifact"

**Solution**:
- Check Spigot version matches in all places (pom.xml, BuildTools)
- Clear `target/` and rebuild
- Verify Maven cache: `mvn dependency:purge-local-repository`

### Runtime Issues

**Problem**: Plugin fails to load with ClassNotFoundException

**Solution**:
- Ensure using `-remapped.jar` not `-remapped-obf.jar`
- Verify server is Spigot/Paper, not Craftbukkit
- Check Java version matches (21)

**Problem**: NoSuchFieldError or NoSuchMethodError

**Solution**:
- NMS version mismatch
- Rebuild against correct Spigot version
- Check for Paper API alternatives to NMS

**Problem**: Commands not working

**Solution**:
- Check `plugin.yml` command registration
- Verify permissions: `dynamicspigotsetting.test` (default: op)
- Check console for command registration errors

**Problem**: Config not saving

**Solution**:
- Check file permissions on `plugins/DynamicSpigotSetting/`
- Look for IOException in console
- Verify disk space

### Performance Issues

**Problem**: Timer not running

**Solution**:
- Check for exceptions in `startTask()`
- Verify `Setting.timerSec` is positive
- Ensure plugin enabled successfully

**Problem**: Settings not applying

**Solution**:
- Enable debug mode to see what's happening
- Check TPS reading: `ServerHelper.getTPS()`
- Verify threshold arrays populated correctly

## Security Considerations

### Command Permissions

Default permission: `dynamicspigotsetting.test` (op only)

**Important**: Allowing non-ops to change merge settings could:
- Impact server performance
- Be used griefing (extreme values)

**Recommendation**: Keep as op-only or create tiered permissions:
```yaml
permissions:
  dynamicspigotsetting.view:     # View current settings
    default: true
  dynamicspigotsetting.modify:   # Change settings
    default: op
  dynamicspigotsetting.reload:   # Reload config
    default: op
```

### Configuration Validation

Current validation is minimal. Consider adding:
- Range checks for merge radii (0-10 reasonable)
- Range checks for timer interval (10-300 seconds)
- TPS threshold order validation (descending)

### NMS Access Risks

Direct server modification can:
- Crash server if done incorrectly
- Cause data corruption if saving state
- Create security vulnerabilities

**Mitigation**:
- Only modify runtime config values (itemMerge, expMerge)
- Never modify world data or player data
- Wrap all NMS in try-catch blocks
- Test thoroughly on non-production servers

## Integration with Other Plugins

### Compatibility Considerations

**Likely Compatible**:
- World management plugins (Multiverse, etc.)
- Economy plugins
- Chat plugins
- Most gameplay plugins

**Potential Conflicts**:
- Other performance optimization plugins that modify same settings
- Plugins that hook into entity merge events
- Custom server software (Forge, Fabric hybrids)

**Best Practices**:
- Load order usually doesn't matter (no dependencies)
- Test with popular plugin combinations
- Document known conflicts in README

### API for Other Plugins

Currently no public API. To add one:

1. Create `DynamicSpigotSettingAPI` interface
2. Implement in `MainClass`
3. Register with ServicesManager:
   ```java
   getServer().getServicesManager().register(
       DynamicSpigotSettingAPI.class,
       this,
       this,
       ServicePriority.Normal
   );
   ```

## Future Enhancement Ideas

### Potential Features

1. **More Dynamic Settings**:
   - View distance adjustment
   - Mob spawn limits
   - Redstone throttling
   - Hopper speeds

2. **Advanced Monitoring**:
   - TPS history graph
   - Alert when TPS drops
   - Per-world TPS tracking

3. **Web Dashboard**:
   - Real-time monitoring
   - Remote configuration
   - Historical charts

4. **Machine Learning**:
   - Learn optimal settings over time
   - Predict TPS drops
   - Auto-tune thresholds

5. **Metrics Export**:
   - Prometheus integration
   - Grafana dashboards
   - InfluxDB support

### Code Improvements

1. **Testing**:
   - Unit tests for Setting parsing
   - Mock Bukkit for command testing
   - Integration tests

2. **Logging**:
   - Replace `getLogger().warning()` with proper levels
   - Add configurable log verbosity
   - Structured logging (JSON)

3. **Configuration**:
   - GUI config editor (/dss config)
   - Per-world settings
   - Time-based profiles (different settings at different times)

4. **Performance**:
   - Cache TPS readings
   - Async config loading
   - Reduce reflection usage

## Additional Resources

### Spigot/Paper Documentation

- [Spigot Plugin Development](https://www.spigotmc.org/wiki/spigot-plugin-development/)
- [Paper API Docs](https://papermc.io/javadocs)
- [Bukkit API Reference](https://hub.spigotmc.org/javadocs/bukkit/)

### Maven and Build Tools

- [Maven Getting Started](https://maven.apache.org/guides/getting-started/)
- [SpecialSource](https://github.com/md-5/SpecialSource)
- [SpigotMC BuildTools](https://www.spigotmc.org/wiki/buildtools/)

### NMS and Remapping

- [Spigot NMS Tutorial](https://www.spigotmc.org/wiki/spigot-nms-and-minecraft-versions/)
- [Mojang Mappings](https://github.com/Mojang/mappings)

---

## Quick Reference for AI Assistants

### File Locations

- Main class: `src/main/java/cf/catworlds/dynamicspigotsetting/MainClass.java`
- Settings: `src/main/java/cf/catworlds/dynamicspigotsetting/Setting.java`
- NMS wrapper: `src/main/java/cf/catworlds/dynamicspigotsetting/utils/ServerHelper.java`
- Commands: `src/main/java/cf/catworlds/dynamicspigotsetting/command/MainCommand.java`
- Messages: `src/main/java/cf/catworlds/dynamicspigotsetting/text/BasicText.java`

### Key Patterns to Remember

1. Settings use `@SettingInfo` annotation for auto-loading
2. All NMS access goes through `ServerHelper` with `@SuppressWarnings("deprecation")`
3. Messages use enum-based `BasicText` with `TextHelper.format()`
4. Commands implement `TabExecutor` with explicit tab completion
5. Timer uses `BukkitRunnable.runTaskTimer()` pattern

### Common Gotchas

- Must use `-remapped.jar` output, not other variants
- `plugin.yml` has Maven variables, rebuild after pom.xml changes
- NMS is version-specific, may break on Minecraft updates
- Paper API preferred over NMS when available
- Config validation is minimal, add checks when accepting user input

### Build and Deploy

```bash
mvn clean package
# Use: target/DynamicSpigotSetting-{version}-remapped.jar
```

### When Making PRs

1. Work on `claude/*` prefixed branch
2. Test on actual Spigot/Paper server
3. Ensure `mvn clean package` succeeds
4. Update version if needed
5. Follow conventional commit messages
6. Push to feature branch, create PR to main

---

**Last Updated**: 2025-12-11
**For**: AI Assistant Claude
**Project Version**: 1.3-mc1_21_11
