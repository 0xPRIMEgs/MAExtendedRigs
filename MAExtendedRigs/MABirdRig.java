package MAExtendedRigs;

import MechaAmazing.MAMesh;
import java.util.Map;

/** Two legs and configurable wing chains; wing digits support bat membranes. */
public final class MABirdRig extends MAExtendedRig {
    public static final int TYPE=1003;
    public static MARigConfig defaultConfig(){return MARigConfig.builder().body(.8f,.55f,.75f).legs(.25f,.45f,.3f).wings(1,3,1.4f).tail(2,.6f).build();}
    public MABirdRig(){this(defaultConfig());}
    public MABirdRig(MARigConfig config){super(TYPE,Layouts.bird(config),config);}
    /** Install authored parts and preserve their connectors. */
    public MABirdRig(Map<String,MAMesh> parts){this();setParts(parts);}
    public MABirdRig(MARigConfig config,Map<String,MAMesh> parts){this(config);setParts(parts);}
}

