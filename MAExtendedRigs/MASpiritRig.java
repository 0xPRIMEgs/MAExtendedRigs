package MAExtendedRigs;

import MechaAmazing.MAMesh;
import java.util.Map;

/** Floating core with head, articulated wisp, orbit ring and satellites. */
public final class MASpiritRig extends MAExtendedRig {
    public static final int TYPE=1012;
    public static MARigConfig defaultConfig(){return MARigConfig.builder().body(.8f,.8f,.8f).appendages(4,3,.8f).tail(0,1).build();}
    public MASpiritRig(){this(defaultConfig());}
    public MASpiritRig(MARigConfig config){super(TYPE,Layouts.spirit(config),config);}
    /** Install authored parts and preserve their connectors. */
    public MASpiritRig(Map<String,MAMesh> parts){this();setParts(parts);}
    public MASpiritRig(MARigConfig config,Map<String,MAMesh> parts){this(config);setParts(parts);}
}

