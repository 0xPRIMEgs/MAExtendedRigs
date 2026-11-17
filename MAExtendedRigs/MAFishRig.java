package MAExtendedRigs;

import MechaAmazing.MAMesh;
import java.util.Map;

/** Body/head, paired fins, dorsal fin, tail chain and tail fin; supports vertical body layout. */
public final class MAFishRig extends MAExtendedRig {
    public static final int TYPE=1008;
    public static MARigConfig defaultConfig(){return MARigConfig.builder().body(1.3f,.5f,.6f).tail(4,1.2f).wings(0,2,.5f).build();}
    public MAFishRig(){this(defaultConfig());}
    public MAFishRig(MARigConfig config){super(TYPE,Layouts.fish(config),config);}
    /** Install authored parts and preserve their connectors. */
    public MAFishRig(Map<String,MAMesh> parts){this();setParts(parts);}
    public MAFishRig(MARigConfig config,Map<String,MAMesh> parts){this(config);setParts(parts);}
}

