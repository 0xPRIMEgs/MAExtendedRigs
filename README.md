# MAExtendedRigs 1.0.0

A separate Java rig library for MechaAmazing: **12 body families, 24 presets**, configurable proportions and appendages. Every rig is a `MARig`, using the existing core mesh, pose, animation, material, and GPU code.

`MAExtendedRigs.jar` contains only the extension classes. Put it beside your `MechaAmazing.jar` and Jaylib runtime on the classpath. The core does not depend on the extension. No edits to the core are required for this release.

## What is included

- Compiled Java 17+ extension JAR and complete Java source.
- Twelve rig families and twenty-four starting presets.
- Connector-only `.mamesh` templates, rest poses, and inspection animations for all presets.
- A desktop skeleton viewer and export utility (also precompiled in `MAExtendedRigs-Examples.jar`), tests, and build scripts.
- An unmodified copy of the tested core JAR in `lib/MechaAmazing.jar`.

This supplies articulated hierarchies and attachment frames. Finished Rimekin meshes, textures, skin weights, IK, foot placement, and creature walk/fly/swim cycles are separate work. The viewer displays colored connector scaffolds; its two-second motion is a joint exercise, not locomotion. Mesh triangle budgets are independent of the rig.

## Dependencies and installation

Use Java 17 or newer, the November 13, 2026 **per-part materials** build of MechaAmazing, and your existing compatible `com.raylib.Raylib` / JavaCPP runtime. That core build is included unchanged in the source ZIP. Avoid duplicate versions of the core on the classpath. Older core builds without per-part material APIs are not supported by this extension build.

Tested core SHA-256:

```
dd5b5dfdab8865bf8a45ac54729bd3c8f8272bdcb62454be7052924e946b6d9c
```

The extension does not bundle native graphics libraries or Jaylib. A graphics context is needed to load textures or create/use `MAGpuRig`; constructing rigs, posing them, and reading/writing connector templates do not require a window.

Example application classpaths:

```sh
# Linux/macOS
java -cp 'your-game.jar:MAExtendedRigs.jar:MechaAmazing.jar:jaylib.jar' your.game.Main
```

```bat
rem Windows
java -cp "your-game.jar;MAExtendedRigs.jar;MechaAmazing.jar;jaylib.jar" your.game.Main
```

## Quick start

```java
import MAExtendedRigs.*;
import MechaAmazing.*;
import java.nio.file.Path;

// Empty anchors are installed immediately, with all connectors in place.
MAExtendedRig creature = MARigPreset.WINGED_QUADRUPED.create();

// Load whichever named .mamesh parts are present; missing files keep anchors.
// true regenerates sockets from the configured skeleton.
creature.loadParts(Path.of("assets/creature"), true);

creature.rotation("tail0", 0, 0.25f, 0);
creature.rotation("leftWing0_0", 0, 0, 0.3f);
creature.position(0, 2, 0).update();

MAPose pose = MAPose.capture(creature);
MAIO.savePose(pose, "creature.mapose");
```

The asset folder must exist. Mesh files are named exactly after the joints, for example `pelvis.mamesh`, `chest.mamesh`, and `frontLeftUpperLeg.mamesh`. See [the generated joint maps](templates/JOINT_MAPS.md) for every preset's names, parents, socket indices, and offsets.

## Rig families

| Class | Type ID | Presets | Main articulated parts |
| --- | ---: | --- | --- |
| `MABipedRig` | 1001 | BIPED, HOPPER | Groin, abdomen, chest, arms/hands, legs/feet, neck/head; optional tail/wings |
| `MAQuadrupedRig` | 1002 | QUADRUPED, HOOFED, HEAVY, REPTILE, FROG, WINGED_QUADRUPED, WALRUS | Pelvis/chest, four legs, neck/head; optional tail/wings/trunk |
| `MABirdRig` | 1003 | BIRD, BAT | Body, neck/head, two legs, wings, wing fingers, tail |
| `MAInsectRig` | 1004 | INSECT, FLYING_INSECT | Thorax, abdomen, head, antennae, paired legs, wings |
| `MACrustaceanRig` | 1005 | CRAB | Body/head, antennae, walking legs, two claw arms and pincers |
| `MACrawlerRig` | 1006 | CENTIPEDE, ISOPOD | Head, antennae, body chain, leg pairs on each segment |
| `MASerpentRig` | 1007 | SERPENT | Head and articulated body chain |
| `MAFishRig` | 1008 | FISH, RAY, SEAHORSE | Body/head, segmented side fins, dorsal fin, tail/tail fin; optional upright neck |
| `MATentacleRig` | 1009 | TENTACLED | Body/head, shell attachment, radial segmented tentacles |
| `MASnailRig` | 1010 | SNAIL | Foot/body, shell, neck/head, antennae, tail; optional walking legs |
| `MAPlantRig` | 1011 | PLANT | Base, stem chain, crown, segmented petals and roots |
| `MASpiritRig` | 1012 | SPIRIT | Core, head, ring, orbiting satellite attachment points, wisp chain |

These are starting topologies/proportions. For example, the frog and walrus presets use the quadruped joint family; their species-specific shapes and gaits still need authored parts and animation. The extension reserves IDs 1001–1012; choose a distinct application ID for a structurally different custom rig.

`MABipedRig` is a creature biped with its own joint names/type. The original `MAHumanoidRig` stays in the core and is still the appropriate rig for existing humanoid assets and poses. Their pose files are not directly interchangeable.

## Configure a body

```java
MARigConfig config = MARigPreset.HEAVY.config().toBuilder()
    .scale(1.2f)
    .body(1.8f, 1.25f, 1.1f)  // length, width, height
    .neck(2, 0.4f)            // segment count, total length
    .legs(0.65f, 0.55f, 0.45f)// upper, lower, foot lengths
    .tail(3, 0.6f)
    .trunk(5, 1.2f)
    .build();
MAQuadrupedRig heavy = new MAQuadrupedRig(config);
```

Start from the selected preset's `config()` or the family's `defaultConfig()`, then use `toBuilder()`. The plain `MARigConfig.builder()` contains generic defaults, which may be inappropriate for a particular family.

Lengths position connectors and guide endpoints; **they do not resize imported mesh vertices**. `scale` multiplies generated connector offsets and the core's animation translation unit. Author or scale mesh geometry to match. The root pivot is the skeleton's origin, not an automatically grounded foot position.

| Setting | Used by |
| --- | --- |
| `body`, `headLength`, `scale` | Body dimensions or guide endpoints across the families |
| `neck`, `trunk` | Biped, quadruped, bird, snail, upright fish |
| `legs`, `legSplay` | Families with articulated legs |
| `armLength` | Biped arms, crustacean claw arms |
| `tail` | Biped, quadruped, bird, insect, fish, snail, spirit |
| `wings(pairs, segments, length)`, `wingDigits` | Biped, quadruped, bird, insect; digits add two bones per finger |
| `wings(..., segments, length)` | Fish side-fin count/length; fish ignores wing-pair and digit counts |
| `legPairs` | Insect, crustacean, snail; crawler uses this many pairs **per body segment** |
| `bodySegments` | Crawler body, serpent body, plant stem |
| `appendages(count, segments, length)` | Tentacles, plant petals/roots; spirit satellite count and wisp segments/length |
| `antennaSegments` | Insect, crustacean, crawler, snail |
| `verticalBody` | Fish: upright body/neck/tail layout |

Unsupported fields are ignored by a family. A zero optional count omits that chain. Lengths remain positive even when the corresponding chain is disabled. A layout is limited to 4096 joints. `tailSegments`, `bodySegments`, etc. are counts of parts, not triangles. Leaf guide endpoints describe intended extent; they do not create additional joints.

## Attach art and preserve connectors

Every new rig starts with invisible zero-triangle anchor parts. You can attach a child mesh before its parent has visible geometry.

| Method | Behavior |
| --- | --- |
| `partTemplate(name)` | Returns a fresh origin-pivot mesh with the required child connectors and no triangles |
| `setPart(name, mesh)` / constructor part map | Copies an unassembled mesh, preserving its authored pivot and connectors |
| `prepareGeometry(name, mesh)` | Copies geometry/texture, replaces child connectors with layout connectors, respects the source pivot frame |
| `setGeometry(name, mesh)` | Prepares and installs one mesh using layout connectors |
| `setGeometries(map)` | Prepares a batch before installing; null entries restore anchors |
| `clearGeometry(name)` | Replaces only that part's art with an empty anchor, keeping descendants visible |
| `setPart(name, null)` | Core removal semantics: hides that part and its descendants; descendant art remains installed |
| `loadParts(folder)` | Loads existing named files and preserves their authored connectors |
| `loadParts(folder, true)` | Loads existing named files and replaces connectors from the layout |
| `writeTemplates(folder)` | Writes all empty `.mamesh` templates; refuses to overwrite any existing target file |

`prepareGeometry` discards all source child sockets, including extra sockets, and reconstructs the required layout sockets. Pass **unassembled** source parts; do not pass a posed mesh that already has attached descendants. Keep original source parts when replacing geometry.

`loadParts` reads/prepares the full batch before installation, so invalid input does not partially replace the rig. Missing files leave current parts unchanged. `writeTemplates` preflights existing filenames, but is not a transactional filesystem operation if a disk write fails halfway through; use a fresh output directory.

The core `.mamesh` format preserves triangles/UVs and pivot/connector frames, but does not serialize runtime textures or vertex colors. Load textures separately and call `rig.setTexture(name, texture)` / `rig.setColor(name, r, g, b, a)` as needed. Texture objects are borrowed; the caller owns their lifetime.

## Pose and animation compatibility

The extension uses the core `MAPose`, `MAAnimation`, and `MAIO` APIs unchanged. Rotation angles are radians; animation time is seconds. Queue multiple changes, then call `update()` once, or let the draw path update automatically.

Poses require a matching type ID **and exact joint-name set**. Different proportions can reuse poses if their names and type match. Changing tail length preserves compatibility; changing tail segment count does not. `MAPose.ANY` skips the type check only, not the joint-name check. Compatible names do not guarantee an animation looks anatomically correct for different proportions.

```java
MAPose rest = MAPose.capture(creature);
creature.rotation("tail0", 0, 0.4f, 0);
MAPose bent = MAPose.capture(creature);
MAAnimation loop = new MAAnimation(creature.type());
loop.put(0, rest);
loop.put(1, bent);
loop.put(2, rest);

// Each frame: elapsedSeconds must be nonnegative.
loop.queue(creature, elapsedSeconds % 2.0);
creature.update();
```

The supplied `templates/<PRESET>/rest.mapose` and `joint_exercise.maanim` match that preset's default topology.

## Render through the core

```java
// After InitWindow and texture loading; atlas must be a non-null valid texture.
MAGpuRig renderer = new MAGpuRig(creature, atlas);

// Inside BeginMode3D(camera), outside any rlBegin/rlEnd block:
renderer.draw(0, 0, 0);
// Or use the CPU path instead:
creature.drawTextured(atlas);

// Shutdown before closing the graphics context:
renderer.close();
// Then unload textures owned by your application.
```

`ORDERED` is the default GPU mode and respects per-part textures and draw order. `BATCHED` groups equal textures and is intended for opaque art; it can change transparency ordering. `SINGLE` ignores part textures and uses the supplied atlas. Zero-triangle anchors do not add drawable geometry. Draw call count depends on texture groups, not just the number of joints.

## Custom attachment positions and layouts

Use `MARigLayout` to adjust individual attachment positions or add parts before constructing a rig:

```java
MAExtendedRig base = MARigPreset.QUADRUPED.create();
MARigLayout layout = base.layout().toBuilder()
    .offset("frontLeftUpperLeg", -0.5f, -0.3f, 0)
    .joint("leftHorn", "head", -0.15f, 0.2f, 0.1f)
    .tip("leftHorn", 0, 0.35f, 0)
    .build();
MAExtendedRig custom = new MAExtendedRig(2100, layout, base.config());
```

Offsets are measured from the parent pivot in its rest frame: +Y up, +Z forward, named left = -X. `frame(name, MAPivot)` sets both offset and orientation. Quaternion order is x, y, z, w. `chain(...)` adds sequential named joints; `tip(...)` only sets an authoring guide endpoint. Define parents before children; names must be filename-safe identifiers.

Custom layout coordinates are already final model units; the `MAExtendedRig` constructor does not rescale them. Its config sets the motion translation unit and supplies metadata. The constructor without config uses scale 1.

Layouts are immutable. `define` and `connector` mutation on an extended rig are rejected; create a modified layout and a new rig instead. `MAExtendedRig` is sealed with final family subclasses; customize by composition/layout rather than subclassing. This keeps constructor callbacks and saved topology predictable.

## Build and run

To inspect the precompiled viewer without a compiler, run this from the extracted folder (replace the Jaylib path; on Windows use `;` instead of `:` in the classpath):

```sh
java -cp 'MAExtendedRigs-Examples.jar:MAExtendedRigs.jar:lib/MechaAmazing.jar:/path/to/jaylib.jar' RigGallery
```

Put your Jaylib runtime at `lib/jaylib.jar`, or supply its path. Set `JAVA_HOME` to a JDK if `javac`/`jar` are not on PATH.

```sh
sh build.sh lib/MechaAmazing.jar /path/to/jaylib.jar
sh run.sh test /path/to/jaylib.jar          # No graphics display needed
sh run.sh gallery /path/to/jaylib.jar       # Desktop skeleton viewer
sh run.sh render-test /path/to/jaylib.jar   # Actual framebuffer comparisons
sh run.sh export /path/to/jaylib.jar        # Writes fresh generated-templates/
```

On Windows, use `build.bat` and `run.bat` with the same arguments. Windows scripts are included but were not executed on Windows. Native dependencies must match your platform. The viewer accepts left/right arrows to change preset, Space to toggle joint motion, G to switch CPU/GPU, R for rest, and Escape to exit.

`build.sh` / `build.bat` rebuild the library JAR; `run.sh` / `run.bat` compile the example/test sources before execution. To refresh the optional precompiled examples JAR after running either script:

```sh
jar --create --file MAExtendedRigs-Examples.jar -C build/examples RigGallery.class -C build/examples RigPreviewParts.class -C build/examples RigMotion.class -C build/examples ExportRigTemplates.class
```

See [VALIDATION.md](VALIDATION.md) for the verified build and tests, [templates/JOINT_MAPS.md](templates/JOINT_MAPS.md) for attachment maps, and [docs/Rig_Layout_Preview.png](docs/Rig_Layout_Preview.png) for rendered scaffolds.

## License

Prime License:
If you're a Christian you can use this as MIT. If not apply for it's use in an issue. Jesus is Lord.

This preserves the license notice supplied with MechaAmazing. Third-party Java/raylib dependencies retain their own licenses and are not bundled into the extension JAR.
