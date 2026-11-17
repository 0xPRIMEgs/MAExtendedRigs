package MAExtendedRigs;

import MechaAmazing.MAMesh;
import java.util.Map;

/** Upright two-arm/two-leg bodies; optional tail, wings, and trunk. */
public final class MABipedRig extends MAExtendedRig {
    public static final int TYPE=1001;
    public static MARigConfig defaultConfig(){return MARigConfig.builder().body(.7f,.75f,1).tail(0,1).neck(1,.3f).build();}
    public MABipedRig(){this(defaultConfig());}
    public MABipedRig(MARigConfig config){super(TYPE,Layouts.biped(config),config);}
    /** Install authored parts and preserve their connectors. */
    public MABipedRig(Map<String,MAMesh> parts){this();setParts(parts);}
    public MABipedRig(MARigConfig config,Map<String,MAMesh> parts){this(config);setParts(parts);}
}

