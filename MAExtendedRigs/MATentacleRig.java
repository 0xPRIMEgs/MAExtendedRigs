package MAExtendedRigs;

import MechaAmazing.MAMesh;
import java.util.Map;

/** Body/head/shell slots and radial articulated tentacles. */
public final class MATentacleRig extends MAExtendedRig {
    public static final int TYPE=1009;
    public static MARigConfig defaultConfig(){return MARigConfig.builder().body(1,1,.8f).appendages(6,5,1.5f).tail(0,1).build();}
    public MATentacleRig(){this(defaultConfig());}
    public MATentacleRig(MARigConfig config){super(TYPE,Layouts.tentacle(config),config);}
    /** Install authored parts and preserve their connectors. */
    public MATentacleRig(Map<String,MAMesh> parts){this();setParts(parts);}
    public MATentacleRig(MARigConfig config,Map<String,MAMesh> parts){this(config);setParts(parts);}
}

