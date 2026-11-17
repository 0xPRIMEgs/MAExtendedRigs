package MAExtendedRigs;

import MechaAmazing.MAMesh;
import java.util.Map;

/** Four articulated legs, pelvis/chest, neck/head, optional tail, wings, and trunk. */
public final class MAQuadrupedRig extends MAExtendedRig {
    public static final int TYPE=1002;
    public static MARigConfig defaultConfig(){return MARigConfig.builder().body(1.4f,.7f,.6f).build();}
    public MAQuadrupedRig(){this(defaultConfig());}
    public MAQuadrupedRig(MARigConfig config){super(TYPE,Layouts.quadruped(config),config);}
    /** Install authored parts and preserve their connectors. */
    public MAQuadrupedRig(Map<String,MAMesh> parts){this();setParts(parts);}
    public MAQuadrupedRig(MARigConfig config,Map<String,MAMesh> parts){this(config);setParts(parts);}
}

