package MAExtendedRigs;

import MechaAmazing.MAMesh;
import java.util.Map;

/** Paired walking legs plus independently animated claws and pincers. */
public final class MACrustaceanRig extends MAExtendedRig {
    public static final int TYPE=1005;
    public static MARigConfig defaultConfig(){return MARigConfig.builder().body(.9f,1.3f,.4f).legPairs(4).legs(.2f,.3f,.12f).legSplay(.5f).armLength(.7f).tail(0,1).build();}
    public MACrustaceanRig(){this(defaultConfig());}
    public MACrustaceanRig(MARigConfig config){super(TYPE,Layouts.crustacean(config),config);}
    /** Install authored parts and preserve their connectors. */
    public MACrustaceanRig(Map<String,MAMesh> parts){this();setParts(parts);}
    public MACrustaceanRig(MARigConfig config,Map<String,MAMesh> parts){this(config);setParts(parts);}
}

