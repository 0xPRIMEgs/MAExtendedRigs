package MAExtendedRigs;

import MechaAmazing.MAMesh;
import java.util.Map;

/** Foot/body/shell with neck, antennae, and optional walking legs. */
public final class MASnailRig extends MAExtendedRig {
    public static final int TYPE=1010;
    public static MARigConfig defaultConfig(){return MARigConfig.builder().body(1.2f,.65f,.5f).legPairs(0).tail(3,.6f).neck(2,.55f).antennaSegments(2).build();}
    public MASnailRig(){this(defaultConfig());}
    public MASnailRig(MARigConfig config){super(TYPE,Layouts.snail(config),config);}
    /** Install authored parts and preserve their connectors. */
    public MASnailRig(Map<String,MAMesh> parts){this();setParts(parts);}
    public MASnailRig(MARigConfig config,Map<String,MAMesh> parts){this(config);setParts(parts);}
}

