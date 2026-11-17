package MAExtendedRigs;

import MechaAmazing.MAMesh;
import java.util.Map;

/** A head and configurable continuous body chain. */
public final class MASerpentRig extends MAExtendedRig {
    public static final int TYPE=1007;
    public static MARigConfig defaultConfig(){return MARigConfig.builder().body(4,.4f,.4f).bodySegments(12).tail(0,1).build();}
    public MASerpentRig(){this(defaultConfig());}
    public MASerpentRig(MARigConfig config){super(TYPE,Layouts.serpent(config),config);}
    /** Install authored parts and preserve their connectors. */
    public MASerpentRig(Map<String,MAMesh> parts){this();setParts(parts);}
    public MASerpentRig(MARigConfig config,Map<String,MAMesh> parts){this(config);setParts(parts);}
}

